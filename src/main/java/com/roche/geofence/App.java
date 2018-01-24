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
        World world = new World(500, 20, 50, 200);
//        System.out.println(System.currentTimeMillis()-start);
        WorldJSONLogger.logJSONWorld(world, "dataViz/data.json");

        List<User> users = new ArrayList<>();
        users.add(new User("User1", world));
        users.add(new User("User2", world));

        for (User user: users) {
            user.startTracking();
        }

        for (int i = 0; i < 1000; i++) {
            for (User user: users) {
                user.stepClock();
            }
        }

        UserPositionsLogger.logJSONUserPositions(users, "dataViz/moves.json");
    }
}
