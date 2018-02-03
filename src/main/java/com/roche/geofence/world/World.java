package com.roche.geofence.world;

import com.roche.geofence.geofence.Geofence;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private int size;
    private List<Geofence> geofenceList = new ArrayList<>();

    public World(int size, int nbGeofences, double minRadius, double maxRadius) {
        this.size = size;
        generateGeofenceList(nbGeofences, minRadius, maxRadius);
    }

    public int getSize() {
        return size;
    }

    public List<Geofence> getGeofenceList() {
        return geofenceList;
    }

    private void generateGeofenceList(int nbGeofences, double minRadius, double maxRadius) {
        int geofenceId = 0;
        for (int i = 0; i < nbGeofences; i++) {
            geofenceList.add(generateGeofence(minRadius, maxRadius, geofenceId));
            geofenceId++;
        }
    }

    private Geofence generateGeofence(double minRadius, double maxRadius, int geofenceId) {
        Geofence geofence;
        Coordinate coordinate;
        do {
            coordinate = generateGeofenceOrigin(minRadius);
        } while (isCenterInsideAtLeastOneGeofence(coordinate));
        maxRadius = generateMaxRadiusInGridFromCoordinates(coordinate, maxRadius);
        geofence = new Geofence(coordinate, getRandomRadius(minRadius, maxRadius), "Geofence-" + geofenceId);
        return geofence;
    }

    private Coordinate generateGeofenceOrigin(double minRadius) {
        return new Coordinate(getRandomValueInGrid(minRadius), getRandomValueInGrid(minRadius));
    }

    private boolean isCenterInsideAtLeastOneGeofence(Coordinate center) {
        for (Geofence geofence : geofenceList) {
            if(geofence.isCollisionPoint(center)) {
                return true;
            }
        }
        return false;
    }

    private double generateMaxRadiusInGridFromCoordinates(Coordinate coordinate, double maxRadius) {
        if(coordinate.getX() < maxRadius) maxRadius = coordinate.getX();
        if(coordinate.getY() < maxRadius) maxRadius = coordinate.getY();
        if(size - coordinate.getX() < maxRadius) maxRadius = size - coordinate.getX();
        if(size - coordinate.getY() < maxRadius) maxRadius = size - coordinate.getY();

        return maxRadius;
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
