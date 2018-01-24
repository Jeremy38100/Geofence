package com.roche.geofence.geofence;

import com.roche.geofence.user.User;

public interface GeofenceObserver {
    public void checkGeofence(User user);
}
