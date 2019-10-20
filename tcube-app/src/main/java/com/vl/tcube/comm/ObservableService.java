package com.vl.tcube.comm;

import java.util.List;

public interface ObservableService {

    void addCommunicationObserver(CommunicationObserver observer);
    List<CommunicationObserver> getCommunicationObservers();
    void start();
}
