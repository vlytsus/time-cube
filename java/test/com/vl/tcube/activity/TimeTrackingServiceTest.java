package com.vl.tcube.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

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
    }

    @Test
    void startActivity_FirstTime() {
        //given
        when(activityFactory.createActivity(any())).thenReturn(activity1);

        //when
        testedClass.startActivity(Activity.Type.BILLABLE);

        //then
        verify(activityFactory).createActivity(Activity.Type.BILLABLE);
        assertEquals(1, testedClass.getActivities().size());
        assertEquals(activity1, testedClass.getActivities().get(0));
    }

    @Test
    void startActivity_NextTimes() {
        //given
        when(activityFactory.createActivity(any())).thenReturn(activity1).thenReturn(activity2);
        testedClass.startActivity(Activity.Type.BILLABLE);

        //when
        testedClass.startActivity(Activity.Type.BILLABLE);

        //then
        verify(activity1).stop();
        verify(activity2, never()).stop();
        assertEquals(2, testedClass.getActivities().size());
    }

    @Test
    void getActivities() {
        //when
        List<Activity> activities = testedClass.getActivities();
        //then
        assertNotNull(activities);
    }

}