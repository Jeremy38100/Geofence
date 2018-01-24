package com.roche.geofence.geofence;

import com.roche.geofence.user.User;
import com.roche.geofence.world.Coordinate;

public class Geofence implements GeofenceObserver{
    private Coordinate coordinate;
    private double radius;
    private String name;

    public Geofence(Coordinate coordinate, double radius, String name) {
        this.coordinate = coordinate;
        this.radius = radius;
        this.name = name;
    }

    public boolean isCollisionGeofence(Geofence geofence2) {
        return calculateDistancePoints(geofence2.coordinate, coordinate) <= geofence2.radius + radius;
    }

    public boolean isCollisionPoint(Coordinate coord) {
        return calculateDistancePoints(coord, coordinate) <= this.radius;
    }

    public double getArea() {
        return Math.PI * (radius*radius);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public double getRadius() {
        return radius;
    }

    public String getName() {
        return name;
    }

    @Override
    public void checkGeofence(User user) {
        if(isCollisionPoint(user.getCoordinate())) {
            System.out.println(user.getName() + user.getCoordinate().toString() + " INSIDE " + this.toString());
        }
    }

    @Override
    public String toString() {
        return name + "(" + coordinate.toString() + "," + radius + ")";
    }

    private double calculateDistancePoints(Coordinate a, Coordinate b) {
        return Math.sqrt(Math.pow((b.getX() - a.getX()),2) + Math.pow((b.getY() - a.getY()),2));
    }
}
