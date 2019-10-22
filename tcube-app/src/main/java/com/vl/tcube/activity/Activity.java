package com.vl.tcube.activity;

import java.time.LocalDateTime;

public class Activity {

    private ActivityType type;
    private LocalDateTime startTime = LocalDateTime.now();

    public Activity() {
        this.type = ActivityType.UNDEFINED;
    }

    public Activity(ActivityType activityType) {
        this.type = activityType;
    }

    public ActivityType getType() {
        return type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
