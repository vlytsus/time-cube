package com.vl.tcube.comm;

public abstract class UpdateEvent implements Runnable {
    private CubePositionMessage message;
    public void setMessage(CubePositionMessage message){
       this.message = message;
    }
}
