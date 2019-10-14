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
        startActivity(convertLevelsToActiviyType(xLevel, yLevel, zLevel));
    }

    public void startActivity(Activity.Type activityType){
        stopCurrentActivity();
        Activity newActivity = activityFactory.createActivity(activityType);
        activities.add(newActivity);
    }

    public List<Activity> getActivities(){
        return activities;
    }

    private Activity.Type convertLevelsToActiviyType(int xLevel, int yLevel, int zLevel){
        if(xLevel < 50 && yLevel < 50) {
            return Activity.Type.BILLABLE;
        } else {
            return Activity.Type.BREAK;
        }
    }

    private void stopCurrentActivity(){
        if(!activities.isEmpty()) {
            Activity currentActivity = activities.get(activities.size() - 1);
            currentActivity.stop();
        }
    }
}
