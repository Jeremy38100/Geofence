package com.roche.geofence.geofence;

import com.roche.geofence.user.User;
import com.roche.geofence.world.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Geofence implements GeofenceObserver{
    private Coordinate coordinate;
    private double radius;
    private String name;
    private List<UserGeofence> usersHistory = new ArrayList<>();

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
        this.updateUserHistory(user, isCollisionPoint(user.getCoordinate()));
    }

    @Override
    public String toString() {
        return name + "(" + coordinate.toString() + "," + radius + ")";
    }

    private double calculateDistancePoints(Coordinate a, Coordinate b) {
        return Math.sqrt(Math.pow((b.getX() - a.getX()),2) + Math.pow((b.getY() - a.getY()),2));
    }

    private void updateUserHistory(User user, boolean isCollisionPoint) {
        UserGeofence userHistory = getUserInsidePreviousMove(user);
        if(isCollisionPoint) {
//            System.out.println(user.getName() + user.getCoordinate().toString() + " INSIDE " + this.toString());
            if (userHistory != null && !userHistory.isTriggered()) {
                if (userHistory.isUserInsideLongEnough(user.getLastMove())) {
                    userHistory.setTriggered(true);
                    System.out.println(user.getName() + ", " + this.getName() + ", " + user.getLastMove().getTimestamp());
                }
            } else {
                usersHistory.add(new UserGeofence(user, user.getLastMove().getTimestamp()));
            }
        } else {
            usersHistory.remove(userHistory);
        }
    }

    private UserGeofence getUserInsidePreviousMove(User user) {
        for (UserGeofence userHistory : usersHistory) {
            if(userHistory.isUser(user)) {
                return userHistory;
            }
        }
        return null;
    }
}
