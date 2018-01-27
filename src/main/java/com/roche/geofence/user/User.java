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
    private int maxNextMoveStep = 50;

    private int worldSize;

    private long currentClock;
    private int nextMoveClock;

    private List<GeofenceObserver> observers = new ArrayList<>();

    public User(String name, World world) {
        this.name = name;
        this.worldSize = world.getSize();
        this.coordinate = new Coordinate();

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
        System.out.println(name + coordinate.toString() + ", " + currentClock);
    }

    private void move() {
        coordinate.setX(getRandomValueInGridInRange(coordinate.getX()));
        coordinate.setY(getRandomValueInGridInRange(coordinate.getY()));
        coordinatesHistory.add(new Position(new Coordinate(coordinate.getX(), coordinate.getY()), currentClock));
    }

    private void planNextMove() {
        nextMoveClock = getRandomNextClockMove(currentClock+1);
    }

    private int getRandomValueInGridInRange(int oldValue) {
        Random r = new Random();
        int minValue = ((oldValue - maxNextMoveStep >= 0) ? (oldValue-maxNextMoveStep) : 0);
        int maxValue = ((oldValue + maxNextMoveStep <= worldSize) ? (oldValue+maxNextMoveStep) : worldSize);
        return (int) (minValue + (maxValue - minValue) * r.nextDouble());
    }

    private int getRandomNextClockMove(long minClock) {
        Random r = new Random();
        return (int) (minClock + (maxNextClock) * r.nextDouble()) + 1;
    }
}