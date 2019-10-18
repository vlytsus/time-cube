package com.vl.tcube.comm;

public class CubePositionMessage {
    private Integer xPos;
    private Integer yPos;
    private Integer zPos;

    public CubePositionMessage(Integer xPos, Integer yPos, Integer zPos){
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }

    public Integer getxPos() {
        return xPos;
    }

    public void setxPos(Integer xPos) {
        this.xPos = xPos;
    }

    public Integer getyPos() {
        return yPos;
    }

    public void setyPos(Integer yPos) {
        this.yPos = yPos;
    }

    public Integer getzPos() {
        return zPos;
    }

    public void setzPos(Integer zPos) {
        this.zPos = zPos;
    }
}
