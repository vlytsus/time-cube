package com.vl.tcube.comm;

import com.vl.tcube.activity.ActivityType;
import com.vl.tcube.activity.TimeTrackingService;
import com.vl.tcube.comm.err.CommunicationClosedException;
import com.vl.tcube.comm.err.UnexpectedMessageException;
import gnu.io.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

public class GyroSerialListenerService extends Service<Void> implements ObservableService {

    public static final char MESSAGE_START_MARKER = '<';
    public static final int MESSAGE_MAX_LEN = 14;
    public static final int END_OF_STREAM = -1;
    public static final char END_OF_MESSAGE_MARKER = '>';
    public static final int COMMUNICATION_BAUD_RATE = 9600;
    public static final int PORT_TYPE = 2000;

    private TimeTrackingService timeService;
    private List<CommunicationObserver> observers = new ArrayList<>();
    private Runnable updater;

    public GyroSerialListenerService(TimeTrackingService timeService){
        this.timeService = timeService;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                InputStream in = null;
                CommPort port = null;
                try {
                    while ( !isCancelled() ) {
                        if(in == null) {
                            closeConnection(in, port);
                            port = tryToConnectToFirstAvailablePort();
                            in = port.getInputStream();
                            continue;
                        }

                        int ch = in.read();
                        if(ch == END_OF_STREAM){
                            Thread.yield();
                            continue;
                        }
                        if(ch != MESSAGE_START_MARKER){//skip all characters until first message start
                            continue;
                        }else{
                            ch = in.read();
                            if(ch == END_OF_STREAM){//double check that we are not reached end of stream
                                Thread.yield();
                                continue;
                            }
                        }
                        StringBuilder sb = new StringBuilder(MESSAGE_MAX_LEN);
                        sb.append((char)ch);
                        while ( ( ch = in.read()) != END_OF_STREAM ){
                            if(ch == END_OF_MESSAGE_MARKER){
                                break;
                            }else{
                                sb.append((char)ch);
                                if(sb.length() > MESSAGE_MAX_LEN){
                                    //that message is probably broken. Just skip and wait for another
                                    break;
                                }
                            }
                        }
                        String message = sb.toString();
                        CubePositionMessage msg = parseToken(in, message);
                        notifyObservers(msg);
                        log(msg.getxPos(), msg.getyPos(), msg.getzPos());
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                } finally {
                    closeConnection(in, port);
                }
                log("Communication ended");
                return null;
            }
        };
    }

    @Override
    public void addCommunicationObserver(CommunicationObserver observer){
        observers.add(observer);
    }

    @Override
    public List<CommunicationObserver> getCommunicationObservers(){
        return observers;
    }

    private void notifyObservers(CubePositionMessage msg){
        for(CommunicationObserver observer : observers){
            observer.onCubePositionMessage(msg);
        }
    }
    private void closeConnection(InputStream in, CommPort port) {
        if(in != null){
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(port != null){
            port.close();
        }
    }

    private CommPort tryToConnectToFirstAvailablePort() throws UnsupportedCommOperationException, PortInUseException, IOException, NoSuchPortException {
        InputStream in;
        String portName = getPortName();
        if(portName == null){
            throw new CommunicationClosedException();
        }
        log("Try to connect to", portName);
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        CommPort commPort = portIdentifier.open(this.getClass().getName(), PORT_TYPE);

        log("Connected to", portName);
        return commPort;
    }
    
    private void log(Object... args){
        //TODO replace with logger
        for(Object param : args){
            System.out.print(param);
            System.out.print(": ");
        }
        System.out.println();
    }

    private String getPortName(){
        CommPortIdentifier cpi = null;
        Enumeration ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements()) {
            try {
                cpi = (CommPortIdentifier) ports.nextElement();
                return cpi.getName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private CubePositionMessage parseToken(InputStream in, String message) throws IOException {
        StringTokenizer st = new StringTokenizer(message, ":");

        if("HLT".equals("")){
            in.close();
            throw new CommunicationClosedException();
        }
        try {
            Integer xVal;
            Integer yVal;
            Integer zVal;
            if(st.hasMoreTokens()) {
                String token = st.nextToken();
                xVal = Integer.valueOf(token);
            } else {
                return null;
            }
            if(st.hasMoreTokens()) {
                String token = st.nextToken();
                yVal = Integer.valueOf(token);
            } else {
                return null;
            }
            if(st.hasMoreTokens()) {
                String token = st.nextToken();
                zVal = Integer.valueOf(token);
            } else {
                return null;
            }
            CubePositionMessage msg = new CubePositionMessage(xVal, yVal, zVal);
            log(ActivityType.getActivityType(xVal, yVal, zVal).name());
            return msg;

        } catch(NumberFormatException ignore) {
            throw new UnexpectedMessageException("Invalid message received: " + message);
        }
    }
}
