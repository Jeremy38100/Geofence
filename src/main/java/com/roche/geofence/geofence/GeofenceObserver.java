package com.roche.geofence.geofence;

import com.roche.geofence.user.User;

public interface GeofenceObserver {
    void checkGeofence(User user);
}
