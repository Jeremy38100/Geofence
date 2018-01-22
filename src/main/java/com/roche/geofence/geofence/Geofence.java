package com.roche.geofence.geofence;

public class Geofence implements GeofenceObserver{
    private int x;
    private int y;
    private double radius;
    private String name;

    public Geofence(int x, int y, double radius, String name) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.name = name;
    }

    public boolean isCollisionGeofence(Geofence geofence2) {
        int distanceCenter = (this.x-geofence2.x)*(this.x-geofence2.x) + (this.y-geofence2.y)*(this.y-geofence2.y);
        return (distanceCenter <= (this.radius + geofence2.radius)*(this.radius + geofence2.radius));
    }

    public boolean isCollisionPoint(int x, int y) {
        int distanceCenter = (x-this.x)*(x-this.x) + (y-this.y)*(y-this.y);
        return (distanceCenter <= this.radius*this.radius);
    }

    public boolean isCollisionPointWithMargin(int x, int y, double minRadius) {
        int distanceCenter = (x-this.x)*(x-this.x) + (y-this.y)*(y-this.y);
        return (distanceCenter <= (this.radius+minRadius)*(this.radius+minRadius));
    }

    public double getArea() {
        return Math.PI * (radius*radius);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public String getName() {
        return name;
    }

    @Override
    public void checkGeofence() {
        ;
    }
}
