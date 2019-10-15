package com.vl.tcube.activity;

import java.time.Duration;
import java.time.LocalDateTime;

public class Activity {

    private ActivityType type;
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime;

    public Activity(ActivityType activityType) {
        this.type = activityType;
    }

    public ActivityType getType() {
        return type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return Duration.between(endTime, startTime);
    }

    public void stop(){
        endTime = LocalDateTime.now();
    }
}
