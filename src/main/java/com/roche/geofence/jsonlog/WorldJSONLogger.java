package com.roche.geofence.jsonlog;

import com.roche.geofence.Geofence;
import com.roche.geofence.world.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class WorldJSONLogger {

    public static void logJSONWorld(World world, String fileName) {
        JSONObject obj = new JSONObject();
        obj.put("size", world.getSize());

        JSONArray geofencesJSON = new JSONArray();
        for (Geofence geofence : world.geofenceList) {
            JSONObject geofenceJSON = new JSONObject();
            geofenceJSON.put("x", geofence.getX());
            geofenceJSON.put("y", geofence.getY());
            geofenceJSON.put("radius", geofence.getRadius());
            geofenceJSON.put("name", geofence.getName());
            geofencesJSON.add(geofenceJSON);
        }
        obj.put("geofences", geofencesJSON);

        try (FileWriter file = new FileWriter(fileName)) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}