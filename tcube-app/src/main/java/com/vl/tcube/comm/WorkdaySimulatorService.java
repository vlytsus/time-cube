package com.vl.tcube.comm;

import com.vl.tcube.activity.ActivityType;

import java.util.ArrayList;
import java.util.List;

/**
 * That class simulates workday.
 * 50% of time is work.
 * 10% are shores.
 * 10% - learn
 * 10% is rest
 * and sometime happens undefined states because cube is flipped
 */
public class WorkdaySimulatorService extends Thread implements ObservableService {

    public static final int DEFAULT_X = 100;
    public static final int DEFAULT_Z = 100;

    int yPos;

    private List<CommunicationObserver> observers = new ArrayList<>();

    @Override
    public void addCommunicationObserver(CommunicationObserver observer) {
        observers.add(observer);
    }

    @Override
    public List<CommunicationObserver> getCommunicationObservers() {
        return observers;
    }

    @Override
    public void run(){
        for(int i = 0; i< 100; i++){

            simulateWork(i);
            simulateShores(i);
            simulateLearn(i);
            simulateRest(i);
            simulateUndefined(i);

            CubePositionMessage message = new CubePositionMessage(DEFAULT_X, yPos, DEFAULT_Z);
            notifyAll(message);

            pause();

            if(i == 99){//restart
                i = 0;
            }
        }
    }

    private void notifyAll(CubePositionMessage message) {
        for(CommunicationObserver observer : observers){
            observer.onCubePositionMessage(message);
        }
    }

    private void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void simulateWork(int i) {
        if(i > 0 && i < 50) {
            yPos = ActivityType.WORK.getyStart() + 1;
        }
    }

    private void simulateShores(int i) {
        if(i > 60 && i < 70) {
            yPos = ActivityType.SHORES.getyStart() + 1;
        }
    }

    private void simulateLearn(int i) {
        if(i > 70 && i < 80) {
            yPos = ActivityType.LEARN.getyStart() + 1;
        }
    }

    private void simulateRest(int i) {
        if(i > 80 && i < 100) {
            yPos = ActivityType.REST.getyStart() + 1;
        }
    }

    private void simulateUndefined(int i) {
        if(i%10 == 0){
            yPos = Integer.MAX_VALUE;
        }
    }
}
