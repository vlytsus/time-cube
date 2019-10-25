package com.vl.tcube.activity;

import com.vl.tcube.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private Duration choresDuration;
    private Duration restDuration;
    private LocalDateTime lastUpdate = LocalDateTime.now();
    static final Logger logger = LoggerFactory.getLogger(TimeTrackingService.class);

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
        activities.add(activityFactory.createActivity());
        resetDurations();
    }

    public void startActivity(int xLevel, int yLevel, int zLevel){
        ActivityType type = ActivityType.getActivityType(xLevel, yLevel, zLevel);
        startActivity(type);
    }

    public void startActivity(ActivityType newActivityType){
        Activity previousActivity = getCurrentActivity();
        if(previousActivity.getType() != newActivityType){
            Activity newActivity = activityFactory.createActivity(newActivityType);
            activities.add(newActivity);
        } else {
            updateDuration(previousActivity.getType(), Duration.between(lastUpdate, LocalDateTime.now()));
        }
        lastUpdate = LocalDateTime.now();
    }

    private void resetDurations() {
        workDuration = Duration.ofMinutes(0);
        learnDuration = Duration.ofMinutes(0);
        choresDuration = Duration.ofMinutes(0);
        restDuration = Duration.ofMinutes(0);
    }

    private void updateDuration(ActivityType activityType, Duration delta){
        logger.debug("updateDuration activityType=" + activityType.name() + " delta=" + delta);
        switch(activityType){
            case WORK:
                workDuration =  workDuration.plus(delta);
                break;
            case LEARN:
                learnDuration = learnDuration.plus(delta);
                break;
            case CHORES:
                choresDuration = choresDuration.plus(delta);
                break;
            case REST:
                restDuration = restDuration.plus(delta);
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

    public Duration getWorkDuration() { return workDuration;
    }

    public Duration getLearnDuration() {
        return learnDuration;
    }

    public Duration getChoresDuration() {
        return choresDuration;
    }

    public Duration getRestDuration() {
        return restDuration;
    }

    public Duration getTotalDuration() {
        return restDuration
                .plus(choresDuration)
                .plus(learnDuration)
                .plus(workDuration);
    }
}
