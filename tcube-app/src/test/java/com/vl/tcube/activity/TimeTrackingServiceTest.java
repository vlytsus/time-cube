package com.vl.tcube.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeTrackingServiceTest {

    @Mock Activity activity1;
    @Mock Activity activity2;
    @Mock ActivityFactory activityFactory;

    private TimeTrackingService testedClass;
    @BeforeEach
    void setUp() {
        testedClass = new TimeTrackingService(activityFactory);
        lenient().when(activity1.getType()).thenReturn(ActivityType.WORK);
        lenient().when(activity1.getDuration()).thenReturn(Duration.ofMinutes(1));
        lenient().when(activity2.getType()).thenReturn(ActivityType.REST);
        lenient().when(activity2.getDuration()).thenReturn(Duration.ofMinutes(1));
    }

    @Test
    void startActivity_xyz() {
        //given
        when(activityFactory.createActivity(any())).thenReturn(activity1);

        //when
        testedClass.startActivity(10, 10, 10);

        //then
        verify(activityFactory).createActivity(ActivityType.WORK);
        assertEquals(1, testedClass.getActivities().size());
        assertEquals(activity1, testedClass.getActivities().get(0));
    }

    @Test
    void startActivity_FirstTime() {
        //given
        when(activityFactory.createActivity(any())).thenReturn(activity1);

        //when
        testedClass.startActivity(ActivityType.WORK);

        //then
        verify(activityFactory).createActivity(ActivityType.WORK);
        assertEquals(1, testedClass.getActivities().size());
        assertEquals(activity1, testedClass.getActivities().get(0));
    }

    @Test
    void startActivity_NextTimes() {
        //given
        when(activityFactory.createActivity(any())).thenReturn(activity1).thenReturn(activity2);

        //when
        testedClass.startActivity(ActivityType.WORK);
        testedClass.startActivity(ActivityType.WORK);

        //then
        verify(activity1).prolong();
        verify(activity2, never()).prolong();

        assertEquals(1, testedClass.getActivities().size());//if activity type is not changed then ruse previous activity
    }

    @Test
    void getActivities() {
        //when
        List<Activity> activities = testedClass.getActivities();
        //then
        assertNotNull(activities);
    }

}