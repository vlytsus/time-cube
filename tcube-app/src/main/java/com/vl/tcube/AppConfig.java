package com.vl.tcube;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private Properties properties;
    private final static String APP_CONFIG = "app.properties";
    private final static String PREFER_PORT_CONFIG = "prefer.port";

    static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    public AppConfig(){
        try {
            properties = readConfig();
        } catch (IOException e) {
            logger.error("readCongig: ", e);
        }
    }

    public Properties getProperties(){
        return properties;
    }

    public String getPreferPort(){
        return properties.getProperty(PREFER_PORT_CONFIG);
    }

    private Properties readConfig() throws IOException {
        try {
            properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(APP_CONFIG);
            properties.load(inputStream);
            return properties;

        } catch (Exception ex){
            logger.error("readConfig error: ", ex);
            throw ex;
        }
    }
}
