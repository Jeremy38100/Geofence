package com.roche.geofence.world;

import java.util.Random;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate() {
        x= 0;
        y= 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRandomCoordinatesInRange(int min, int max) {
        Random r = new Random();
        this.x = (int) (min + ((max - min) * r.nextDouble()));
        this.y = (int) (min + ((max - min) * r.nextDouble()));
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
