package com.vl.tcube.activity;

public class ActivityFactory {
    public Activity createActivity(ActivityType activityType){
        return new Activity(activityType);
    }

    public Activity createActivity(){
        return new Activity();
    }
}
