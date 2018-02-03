package com.roche.geofence.user;

import com.roche.geofence.world.Coordinate;

public class Position {
    Coordinate coordinate;
    double desiredDirection;
    long timestamp;

    public Position(Coordinate coordinate,long timestamp) {
        this.coordinate = coordinate;
        this.timestamp = timestamp;
        desiredDirection = Math.PI/4;
    }

    public Position(Coordinate coordinate, double direction, long timestamp) {
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
