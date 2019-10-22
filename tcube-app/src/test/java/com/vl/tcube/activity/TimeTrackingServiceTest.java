package com.vl.tcube.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeTrackingServiceTest {
    @Mock Activity activityUndef;
    @Mock Activity activity1;
    @Mock Activity activity2;
    @Mock ActivityFactory activityFactory;

    private TimeTrackingService testedClass;
    @BeforeEach
    void setUp() {
        lenient().when(activityUndef.getType()).thenReturn(ActivityType.UNDEFINED);
        lenient().when(activityUndef.getStartTime()).thenReturn(LocalDateTime.of(2019, 1, 1, 14, 5));
        lenient().when(activityFactory.createActivity()).thenReturn(activityUndef);
        lenient().when(activity1.getType()).thenReturn(ActivityType.WORK);
        lenient().when(activity1.getStartTime()).thenReturn(LocalDateTime.of(2019, 1, 1, 14, 10));
        lenient().when(activity2.getType()).thenReturn(ActivityType.REST);
        lenient().when(activity2.getStartTime()).thenReturn(LocalDateTime.of(2019, 1, 1, 14, 15));
        testedClass = new TimeTrackingService(activityFactory);
    }

    @Test
    void getActivities_one_activity_is_always_created() {
        //when
        List<Activity> activities = testedClass.getActivities();
        //then
        assertEquals(1, testedClass.getActivities().size());
        assertEquals(activityUndef, testedClass.getActivities().get(0));
    }

    @Test
    void startActivity_xyz() {
        //given
        when(activityFactory.createActivity(any())).thenReturn(activity1);

        //when
        testedClass.startActivity(10, 10, 10);

        //then
        verify(activityFactory).createActivity(ActivityType.WORK);
        assertEquals(2, testedClass.getActivities().size());
        assertEquals(activity1, testedClass.getActivities().get(1));
    }

    @Test
    void startActivity_FirstTime() {
        //given
        when(activityFactory.createActivity(any())).thenReturn(activity1);

        //when
        testedClass.startActivity(ActivityType.WORK);

        //then
        verify(activityFactory).createActivity(ActivityType.WORK);
        assertEquals(2, testedClass.getActivities().size());
        assertEquals(activityUndef, testedClass.getActivities().get(0));
        assertEquals(activity1, testedClass.getActivities().get(1));
    }

    @Test
    void startActivity_Next_Same_Type() {
        //given
        when(activityFactory.createActivity(any())).thenReturn(activity1).thenReturn(activity2);

        //when
        testedClass.startActivity(ActivityType.WORK);
        testedClass.startActivity(ActivityType.WORK);

        //then
        assertEquals(2, testedClass.getActivities().size());//if activity type is not changed then ruse previous activity
    }

    @Test
    void startActivity_Next_Other_Type() {
        //given
        when(activityFactory.createActivity(any())).thenReturn(activity1).thenReturn(activity2);

        //when
        testedClass.startActivity(ActivityType.WORK);
        testedClass.startActivity(ActivityType.REST);

        //then
        assertEquals(3, testedClass.getActivities().size());//if activity type is not changed then ruse previous activity
    }
}