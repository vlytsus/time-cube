package com.vl.tcube.activity;

import com.vl.tcube.AppConfig;

import java.util.Properties;

public enum ActivityType {
    REST,
    WORK,
    CHORES,
    LEARN,
    UNDEFINED;

    private final static String APP_CONFIG = "app.properties";
    private int xStart;
    private int xEnd;
    private int yStart;
    private int yEnd;
    private int zStart;
    private int zEnd;
    private String title;

    ActivityType() {
        Properties prop;
        try {
            prop = AppConfig.readConfig();
            xStart = readIntProperty(prop, name() + ".x.start");
            xEnd = readIntProperty(prop, name() +   ".x.end");

            yStart = readIntProperty(prop, name() + ".y.start");
            yEnd = readIntProperty(prop, name() +   ".y.end");

            zStart = readIntProperty(prop, name() + ".z.start");
            zEnd = readIntProperty(prop, name() +   ".z.end");

            title = prop.containsKey(name() + ".title") ? prop.getProperty(name() + ".title") : name();

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

    public String getTitle(){
        return title;
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