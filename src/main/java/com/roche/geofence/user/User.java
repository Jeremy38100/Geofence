package com.roche.geofence.user;

import com.roche.geofence.geofence.GeofenceObserver;
import com.roche.geofence.world.Coordinate;
import com.roche.geofence.world.World;

import java.util.*;

public class User {

    private String name;
    private Coordinate coordinate;
    private List<Position> coordinatesHistory = new ArrayList<>();

    private final int maxNextClock = 10;
    private final int stdDeviationDistance = 25;
    private final double stdDeviationDirection = Math.PI/5;

    private int worldSize;

    private long currentClock;
    private int nextMoveClock;

    private List<GeofenceObserver> observers = new ArrayList<>();

    public User(String name, World world) {
        this.name = name;
        this.worldSize = world.getSize();
        this.coordinate = new Coordinate();
        this.coordinatesHistory.add(new Position(new Coordinate(), 0));
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

    private void sendPosition() {
        for (GeofenceObserver observer: observers) {
            observer.checkGeofence(this);
        }
    }

    private void move() {
        double nextDistance = getNextDistance();
        double nextDirection = getNextDirectionToStayInWorld(nextDistance);
        coordinate.setX(getNextX(nextDirection, nextDistance));
        coordinate.setY(getNextY(nextDirection, nextDistance));

        coordinatesHistory.add(new Position(new Coordinate(coordinate.getX(), coordinate.getY()), nextDirection, currentClock));
    }

    private void planNextMove() {
        nextMoveClock = getRandomNextClockMove(currentClock+1);
    }

    private double getNextDirectionToStayInWorld(double nextDistance) {
        Random r = new Random();
        double nextDirection = getLastMove().getDesiredDirection() + r.nextGaussian()*stdDeviationDirection;
        int nextX = getNextX(nextDirection, nextDistance);
        int nextY = getNextY(nextDirection, nextDistance);

        // While because the last instruction (random direction) may retrun a direction whit a nextMove outside the world
        while (nextX < 0 || nextY < 0 || nextX > worldSize || nextY > worldSize) {
            if(nextY ==0) {
                nextDirection = (nextX < (worldSize/2)) ? 0 : Math.PI;
            } else if (nextX == 0) {
                nextDirection = (nextY < (worldSize/2)) ? -Math.PI/2 : Math.PI/2;
            } else if(nextX < (worldSize/2) && nextY < (worldSize/2)) {
                nextDirection = Math.PI/4;
            } else if (nextX > (worldSize/2) && nextY < (worldSize/2)) {
                nextDirection = Math.PI/2 + Math.PI/4;
            } else if(nextX < (worldSize/2) && nextY > (worldSize/2)) {
                nextDirection = -Math.PI/4;
            } else if(nextX > (worldSize/2)  && nextY > (worldSize/2)) {
                nextDirection = Math.PI + Math.PI/4;
            }
            nextX = getNextX(nextDirection, nextDistance);
            nextY = getNextY(nextDirection, nextDistance);
        }
        return nextDirection;
    }

    private int getNextX(double nextDirection, double nextDistance) {
        return (int) (Math.cos(nextDirection) * nextDistance + coordinate.getX());
    }

    private int getNextY(double nextDirection, double nextDistance) {
        return (int) (Math.sin(nextDirection) * nextDistance + coordinate.getY());
    }

    private int getRandomNextClockMove(long minClock) {
        Random r = new Random();
        return (int) (minClock + (maxNextClock) * r.nextDouble()) + 1;
    }

    private double getNextDistance() {
        Random r = new Random();
        return Math.abs(r.nextGaussian()*stdDeviationDistance);
    }

    public Position getLastMove() {
        if(coordinatesHistory.size()>0) {
            return coordinatesHistory.get(coordinatesHistory.size()-1);
        }
        return null;
    }
}