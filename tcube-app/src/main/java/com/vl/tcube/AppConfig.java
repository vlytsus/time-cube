package com.vl.tcube;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    public static Properties readConfig() throws IOException {
        InputStream inputStream = null;
        try {
            try{
                inputStream = new FileInputStream(APP_CONFIG);
            } catch (FileNotFoundException e){
                inputStream = AppConfig.class.getClassLoader().getResourceAsStream(APP_CONFIG);
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;

        } catch (Exception ex){
            logger.error("readConfig error: ", ex);
            throw ex;
        } finally {
            if(inputStream != null){
                inputStream.close();
            }
        }
    }
}
