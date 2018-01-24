package com.roche.geofence.user;

import com.roche.geofence.world.Coordinate;

public class Position {
    Coordinate coordinate;
    long timestamp;

    public Position(Coordinate coordinate, long timestamp) {
        this.coordinate = coordinate;
        this.timestamp = timestamp;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
