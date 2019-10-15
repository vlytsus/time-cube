package com.vl.tcube.activity;

import java.util.ArrayList;
import java.util.List;

public class TimeTrackingService {
    private List<Activity> activities = new ArrayList();
    private ActivityFactory activityFactory;

    public TimeTrackingService(ActivityFactory activityFactory){
        this.activityFactory = activityFactory;
    }

    public void startActivity(int xLevel, int yLevel, int zLevel){
        System.out.println(xLevel + ":" + yLevel + ":" + zLevel);
        ActivityType type = ActivityType.getActivityType(xLevel, yLevel, zLevel);
        startActivity(type);
    }

    public void startActivity(ActivityType activityType){
        stopCurrentActivity();
        Activity newActivity = activityFactory.createActivity(activityType);
        activities.add(newActivity);
    }

    public List<Activity> getActivities(){
        return activities;
    }

    private void stopCurrentActivity(){
        if(!activities.isEmpty()) {
            Activity currentActivity = activities.get(activities.size() - 1);
            currentActivity.stop();
        }
    }
}
