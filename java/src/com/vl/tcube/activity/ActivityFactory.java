package com.vl.tcube.activity;

public class ActivityFactory {
    public Activity createActivity(Activity.Type activityType){
        return new Activity(activityType);
    }
}
