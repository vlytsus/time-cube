package com.vl.tcube.activity;

import javafx.application.Platform;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TimeTrackingService {
    private final List<Activity> activities;
    private Duration workDuration;
    private Duration learnDuration;
    private Duration shoresDuration;
    private Duration restDuration;

    private ActivityFactory activityFactory;

    public TimeTrackingService(ActivityFactory activityFactory){
        this.activities = new ArrayList<>();
        this.activityFactory = activityFactory;

        workDuration = Duration.ofMinutes(1);
        learnDuration = Duration.ofMinutes(1);
        shoresDuration = Duration.ofMinutes(1);
        restDuration = Duration.ofMinutes(1);
    }

    public void startActivity(int xLevel, int yLevel, int zLevel){
        System.out.println(xLevel + ":" + yLevel + ":" + zLevel);
        ActivityType type = ActivityType.getActivityType(xLevel, yLevel, zLevel);
        startActivity(type);
    }

    public void startActivity(ActivityType activityType){
        Activity currentActivity = getCurrentActivity();
        if(currentActivity != null){
            currentActivity.stop();
            updateStatistic(currentActivity);
        }

        Activity newActivity = activityFactory.createActivity(activityType);
        Platform.runLater(() -> activities.add(newActivity));
    }

    private void updateStatistic(Activity currentActivity) {
        Duration currentDuration = currentActivity.getDuration();
        switch(currentActivity.getType()){
            case WORK:
                getWorkDuration().plus(currentDuration);
                break;
            case LEARN:
                getLearnDuration().plus(currentDuration);
                break;
            case SHORES:
                getShoresDuration().plus(currentDuration);
                break;
            case REST:
                getRestDuration().plus(currentDuration);
                break;
        }
    }

    public List<Activity> getActivities(){
        return activities;
    }

    private Activity getCurrentActivity(){
        if(!activities.isEmpty()) {
            return activities.get(activities.size() - 1);
        }
        return null;
    }

    public Duration getWorkDuration() {
        return workDuration;
    }

    public Duration getLearnDuration() {
        return learnDuration;
    }

    public Duration getShoresDuration() {
        return shoresDuration;
    }

    public Duration getRestDuration() {
        return restDuration;
    }
}
