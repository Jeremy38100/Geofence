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
    private double stdDeviationDirection = Math.PI/5;
    private double previousTurnDirection = 0;

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
        double nextDistance = getNextDistance();
        double nextDirection = getNextDirectionToStayInWorld(nextDistance);
        coordinate.setX(getNextX(nextDirection, nextDistance));
        coordinate.setY(getNextY(nextDirection, nextDistance));

        coordinatesHistory.add(new Position(new Coordinate(coordinate.getX(), coordinate.getY()), nextDirection, currentClock));
    }

    private int getNextX(double nextDirection, double nextDistance) {
        return (int) (Math.cos(nextDirection) * nextDistance + coordinate.getX());
    }

    private int getNextY(double nextDirection, double nextDistance) {
        return (int) (Math.sin(nextDirection) * nextDistance + coordinate.getY());
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

    private double getNextDistance() {
        Random r = new Random();
        return Math.abs(r.nextGaussian()*stdDeviationDistance);
    }

    private double getNextDirectionToStayInWorld(double nextDistance) {
        Random r = new Random();
        double nextDirection = getLastMove().getDesiredDirection() + r.nextGaussian()*stdDeviationDirection;
        int nextX = getNextX(nextDirection, nextDistance);
        int nextY = getNextY(nextDirection, nextDistance);

        double turn = (r.nextBoolean() ? 1 : -1) * Math.PI/4;
        while (nextX < 0 || nextY < 0 || nextX > worldSize || nextY > worldSize) {
            nextDirection = nextDirection + turn;

            nextX = getNextX(nextDirection, nextDistance);
            nextY = getNextY(nextDirection, nextDistance);
        } ;
        return nextDirection;
    }

    private int getRandomNextClockMove(long minClock) {
        Random r = new Random();
        return (int) (minClock + (maxNextClock) * r.nextDouble()) + 1;
    }
}