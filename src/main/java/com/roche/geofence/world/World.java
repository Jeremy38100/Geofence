package com.roche.geofence.world;

import com.roche.geofence.geofence.Geofence;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private int size;
    public List<Geofence> geofenceList = new ArrayList<>();

    public World(int size, int percentCoverage, double minRadius, double maxRadius) {
        this.size = size;
        generateGeofenceList(percentCoverage, minRadius, maxRadius);
    }

    public int getSize() {
        return size;
    }

    public double getGeofencesArea() {
        double area = 0;
        for (Geofence geofence : geofenceList) {
            area += geofence.getArea();
        }
        return area;
    }

    private void generateGeofenceList(int percentCoverage, double minRadius, double maxRadius) {
        geofenceList = new ArrayList();
        double worldArea = size*size;
        double geofencesArea = 0;
        int geofenceId = 0;
        while (geofencesArea <= worldArea * percentCoverage/100) {
            geofenceList.add(generateGeofence(minRadius, maxRadius, geofenceId));
            geofencesArea += geofenceList.get(geofenceList.size() - 1).getArea();
            geofenceId++;
        }
    }

    private Geofence generateGeofence(double minRadius, double maxRadius, int geofenceId) {
        Geofence geofence;
        int[] coordinate = generateGeofenceOrigin(minRadius);
        maxRadius = generateMaxRadiusInGridFromCoordinates(coordinate, maxRadius);
        geofence = new Geofence(coordinate[0], coordinate[1], getRandomRadius(minRadius, maxRadius), "Geofence-" + geofenceId);
        return geofence;
    }

    private int[] generateGeofenceOrigin(double minRadius) {
        int x;
        int y;
        do {
            x = getRandomValueInGrid(minRadius);
            y = getRandomValueInGrid(minRadius);
        } while (isCollisionWithAtLeastOneGeofence(x, y, minRadius));
        int[] geofenceOrigin = {x,y};
        return geofenceOrigin;
    }

    private double generateMaxRadiusInGridFromCoordinates(int[] coordinate, double maxRadius) {
        if(coordinate[0] < maxRadius) maxRadius = coordinate[0];
        if(coordinate[1] < maxRadius) maxRadius = coordinate[1];
        if(size - coordinate[0] < maxRadius) maxRadius = size - coordinate[0];
        if(size - coordinate[1] < maxRadius) maxRadius = size - coordinate[1];

        return maxRadius;
    }

    private boolean isCollisionWithAtLeastOneGeofence(int x, int y, double minRadius) {
        for (Geofence geofence : geofenceList) {
            if(geofence.isCollisionPointWithMargin(x, y, minRadius)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCollisionWithAtLeastOneGeofence(Geofence geofenceToTest) {
        for (Geofence geofence : geofenceList) {
            if(geofence.isCollisionGeofence(geofenceToTest)) {
                return true;
            }
        }
        return false;
    }

    private int getRandomValueInGrid(double minRadius) {
        Random r = new Random();
        int minValue = (int) minRadius;
        int maxValue = (int) (size-minRadius) - 1;
        return r.nextInt(maxValue-minValue) + minValue;
    }

    private double getRandomRadius(double minRadius, double maxRadius) {
        Random r = new Random();
        return (minRadius + (maxRadius - minRadius) * r.nextDouble());
    }

}
