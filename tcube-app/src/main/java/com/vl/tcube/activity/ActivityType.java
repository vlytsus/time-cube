package com.vl.tcube.activity;

import java.io.InputStream;
import java.util.Properties;

public enum ActivityType {
    REST,
    WORK,
    SHORES,
    LEARN,
    UNDEFINED;

    private final static String APP_CONFIG = "app.properties";
    private int xStart;
    private int xEnd;
    private int yStart;
    private int yEnd;
    private int zStart;
    private int zEnd;

    ActivityType() {
        Properties prop = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(APP_CONFIG);
            if(inputStream != null) {
                prop.load(inputStream);

                xStart = readIntProperty(prop, this.name() + ".x.start");
                xEnd = readIntProperty(prop, this.name() +   ".x.end");

                yStart = readIntProperty(prop, this.name() + ".y.start");
                yEnd = readIntProperty(prop, this.name() +   ".y.end");

                zStart = readIntProperty(prop, this.name() + ".z.start");
                zEnd = readIntProperty(prop, this.name() +   ".z.end");
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private int readIntProperty(Properties prop, String name){
        if(prop.containsKey(name)) {
            return Integer.valueOf(prop.getProperty(name));
        } else {
            return 0;
        }
    }

    public static ActivityType getActivityType(int x, int y, int z){
        for (ActivityType type : values()){
            if( type.xStart <= x && x <= type.xEnd &&
                type.yStart <= y && y <= type.yEnd &&
                type.zStart <= z && z <= type.zEnd) {

                return type;
            }
        }
        return UNDEFINED;
    }

    public int getxStart() {
        return xStart;
    }

    public int getxEnd() {
        return xEnd;
    }

    public int getyStart() {
        return yStart;
    }

    public int getyEnd() {
        return yEnd;
    }

    public int getzStart() {
        return zStart;
    }

    public int getzEnd() {
        return zEnd;
    }
}