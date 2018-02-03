package com.roche.geofence.user;

import com.roche.geofence.world.Coordinate;

public class Position {
    private Coordinate coordinate;
    private double desiredDirection;
    private long timestamp;

    Position(Coordinate coordinate, long timestamp) {
        this.coordinate = coordinate;
        this.timestamp = timestamp;
        desiredDirection = Math.PI/4;
    }

    Position(Coordinate coordinate, double direction, long timestamp) {
        this.coordinate = coordinate;
        this.timestamp = timestamp;
        this.desiredDirection = direction;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getDesiredDirection() {
        return desiredDirection;
    }
}
