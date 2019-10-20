package com.vl.tcube.activity;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * That class keeps history of activity changes
 * and statistic about whole activity duration
 */
public class TimeTrackingService {
    private List<Activity> activities;
    private Duration workDuration;
    private Duration learnDuration;
    private Duration shoresDuration;
    private Duration restDuration;

    private ActivityFactory activityFactory;

    public TimeTrackingService(ActivityFactory activityFactory){
        this.activityFactory = activityFactory;
        reset();
    }

    /**
     * This function might be used
     * to reset data & statistics when
     * new day begins
     */
    public void reset(){
        activities = new ArrayList<>();
        workDuration = Duration.ofMinutes(1);
        learnDuration = Duration.ofMinutes(1);
        shoresDuration = Duration.ofMinutes(1);
        restDuration = Duration.ofMinutes(1);
    }

    public void startActivity(int xLevel, int yLevel, int zLevel){
        ActivityType type = ActivityType.getActivityType(xLevel, yLevel, zLevel);
        startActivity(type);
    }

    public void startActivity(ActivityType activityType){
        Activity currentActivity = getCurrentActivity();
        if(currentActivity != null && currentActivity.getType() == activityType){
            currentActivity.prolong();
        } else {
            currentActivity = activityFactory.createActivity(activityType);
            activities.add(currentActivity);
        }
        updateStatistic(currentActivity);
    }

    private void updateStatistic(Activity currentActivity) {
        Duration currentDuration = currentActivity.getDuration();
        switch(currentActivity.getType()){
            case WORK:
                workDuration = workDuration.plus(currentDuration);
                break;
            case LEARN:
                learnDuration = learnDuration.plus(currentDuration);
                break;
            case SHORES:
                shoresDuration = shoresDuration.plus(currentDuration);
                break;
            case REST:
                restDuration = restDuration.plus(currentDuration);
                break;
        }
    }

    public List<Activity> getActivities(){
        return activities;
    }

    public Activity getCurrentActivity(){
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
