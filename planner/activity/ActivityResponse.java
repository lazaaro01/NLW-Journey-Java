package com.rocketseat.planner.activity;

import java.util.Arrays;
import java.util.UUID;

public record ActivityResponse(UUID activityId) {
    public void save(Activity newActivity) {

    }

    public Arrays findByTripId(UUID tripId) {
        return null;
    }
}
