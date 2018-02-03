package com.roche.geofence;

import com.roche.geofence.jsonlog.UserPositionsLogger;
import com.roche.geofence.jsonlog.WorldJSONLogger;
import com.roche.geofence.user.User;
import com.roche.geofence.world.World;

import java.util.ArrayList;
import java.util.List;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Creating World" );
//        long start = System.currentTimeMillis();
        World world = new World(500, 10, 50, 100);
//        System.out.println(System.currentTimeMillis()-start);
        WorldJSONLogger.logJSONWorld(world, "dataViz/data.json");

        List<User> users = new ArrayList<>();
        users.add(new User("User1", world));
        users.add(new User("User2", world));
        users.add(new User("User3", world));
        users.add(new User("User4", world));

        for (User user: users) {
            user.startTracking();
        }

        for (int i = 0; i < 500; i++) {
            for (User user: users) {
                user.stepClock();
            }
        }

        System.out.println(users.get(0).getCoordinatesHistory().get(0).getCoordinate().toString());
        UserPositionsLogger.logJSONUserPositions(users, "dataViz/moves.json");
    }
}
