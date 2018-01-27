package com.roche.geofence.geofence;

import com.roche.geofence.user.Position;
import com.roche.geofence.user.User;

public class UserGeofence {
    private User user;
    private long clockFirstStepInsideGeofence;
    private boolean isTriggered;

    public UserGeofence(User user, long clockFirstStepInsideGeofence) {
        this.user = user;
        this.clockFirstStepInsideGeofence = clockFirstStepInsideGeofence;
        this.isTriggered = false;
    }

    public boolean isUser(User user) {
        return this.user.getName() == user.getName();
    }

    public boolean isUserInsideLongEnough(Position currentPosition) {
        return currentPosition.getTimestamp() - clockFirstStepInsideGeofence > 10;
    }

    public boolean isTriggered() {
        return isTriggered;
    }

    public void setTriggered(boolean triggered) {
        isTriggered = triggered;
    }
}
