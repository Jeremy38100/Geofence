package com.roche.geofence.user;

import com.roche.geofence.geofence.GeofenceObserver;
import com.roche.geofence.world.Coordinate;
import com.roche.geofence.world.World;

import java.util.*;

public class User {

    private String name;
    private Coordinate coordinate;
    private List<Position> coordinatesHistory = new ArrayList<>();

    private int maxNextClock = 10;
    private int stdDeviationDistance = 25;

    private int worldSize;

    private long currentClock;
    private int nextMoveClock;

    private List<GeofenceObserver> observers = new ArrayList<>();

    public User(String name, World world) {
        this.name = name;
        this.worldSize = world.getSize();
        this.coordinate = new Coordinate();
        this.coordinatesHistory.add(new Position(coordinate, 0));
        observers.addAll(world.getGeofenceList());
    }

    public void startTracking() {
        currentClock = 0;
        planNextMove();
    }

    public void stepClock() {
        currentClock++;
        if(currentClock == nextMoveClock) {
            move();
            sendPosition();
            planNextMove();
        }
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public String getName() {
        return name;
    }

    public List<Position> getCoordinatesHistory() {
        return coordinatesHistory;
    }

    public Position getLastMove() {
        if(coordinatesHistory.size()>0) {
            return coordinatesHistory.get(coordinatesHistory.size()-1);
        }
        return null;
    }

    private void sendPosition() {
        for (GeofenceObserver observer: observers) {
            observer.checkGeofence(this);
        }
//        System.out.println(name + coordinate.toString() + ", " + currentClock);
    }

    private void move() {
        double nextDirection = getRandomDirection();
        double nextDistance = getRandomDistance();
        coordinate.setX(correctDirection(getNextX(nextDirection, nextDistance)));
        System.out.println("---");
        coordinate.setY(correctDirection(getNextY(nextDirection, nextDistance)));
        System.out.println("-----------");
        double newCap = //getLastMove().getDirection();
                (getLastMove().getCoordinate().getX() - coordinate.getX() != 0) ?
                Math.atan(
                (getLastMove().getCoordinate().getY() - coordinate.getY()) /
                (getLastMove().getCoordinate().getX() - coordinate.getX())
        )%(Math.PI*2) : (Math.PI/2);
//        if(coordinate.getX() > worldSize) {
//            if(newCap < Math.PI) {
//                newCap = Math.PI - newCap;
//            } else {
//                newCap = newCap - Math.PI;
//            }
//            newCap = newCap - Math.PI / 2;
//        };
        if(coordinate.getX()>450) newCap = Math.PI;
        System.out.println("***************");
        System.out.println("previousCap: " + getLastMove().getDirection());
        System.out.println("newCap: " + newCap);
        System.out.println("***************");
        coordinatesHistory.add(new Position(new Coordinate(coordinate.getX(), coordinate.getY()), newCap, currentClock));
    }

    private int getNextX(double nextDirection, double nextDistance) {
//        System.out.println("direction: " + nextDirection);
//        System.out.println("distance: " + nextDistance);
        return rangeValueInWorld((int) (Math.cos(nextDirection)*nextDistance + coordinate.getX()));
    }

    private int getNextY(double nextDirection, double nextDistance) {
        return rangeValueInWorld((int) (Math.sin(nextDirection)*nextDistance + coordinate.getY()));
    }

    private int rangeValueInWorld(int value) {
        if(value < 0) value = 0;
        if(value > worldSize) value = worldSize;
        return value;
    }

    private int correctDirection(int nextValue) {
        int distanceToWall = (nextValue <= worldSize/2) ? nextValue : (worldSize - nextValue);
        int vectorToCenter = (worldSize/2) - nextValue;
        Float coefficientVectorToCenter = (distanceToWall==0) ? 1 : 1/Float.valueOf(distanceToWall);
        int newValue = (int) (nextValue + (vectorToCenter/10 * coefficientVectorToCenter));
        System.out.println("nextValue: " + nextValue);
        System.out.println("distanceToWall: " + distanceToWall);
        System.out.println("coefficientVectorToCenter: " + coefficientVectorToCenter);
        System.out.println("vectorToCenter: " + vectorToCenter);
        System.out.println("newValue: " + newValue);
        return newValue;
    }

    private void planNextMove() {
        nextMoveClock = getRandomNextClockMove(currentClock+1);
    }

    private double getRandomDistance() {
        Random r = new Random();
        return Math.abs(r.nextGaussian()*stdDeviationDistance);
    }

    private double getRandomDirection() {
        Random r = new Random();
        return getLastMove().getDesiredDirection() + r.nextGaussian()*(Math.PI/16);
    }

    private int getRandomNextClockMove(long minClock) {
        Random r = new Random();
        return (int) (minClock + (maxNextClock) * r.nextDouble()) + 1;
    }
}