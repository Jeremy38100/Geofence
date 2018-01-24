package com.roche.geofence.jsonlog;

import com.roche.geofence.user.Position;
import com.roche.geofence.user.User;
import com.roche.geofence.world.Coordinate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.jws.soap.SOAPBinding;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UserPositionsLogger {

    public static void logJSONUserPositions(List<User> users, String fileName) {
        JSONObject obj = new JSONObject();
        JSONArray usersJSON = new JSONArray();
        for(User user: users) {
            usersJSON.add(userPositions(user));
        }
        obj.put("users", usersJSON);
        try (FileWriter file = new FileWriter(fileName)) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JSONObject userPositions(User user) {
        JSONObject userJSON = new JSONObject();
        JSONArray coordinatesJSON = new JSONArray();
        for (Position position: user.getCoordinatesHistory()) {
            JSONObject coordinateJSON = new JSONObject();
            coordinateJSON.put("x", position.getCoordinate().getX());
            coordinateJSON.put("y", position.getCoordinate().getY());
            coordinateJSON.put("timestamp", position.getTimestamp());
            coordinatesJSON.add(coordinateJSON);
        }
        userJSON.put("positions", coordinatesJSON);
        userJSON.put("userName", user.getName());
        return userJSON;
    }
}
