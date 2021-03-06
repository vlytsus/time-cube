package com.vl.tcube;


import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TextAreaAppender extends AppenderSkeleton {

    public static final int MAX_TEXT_LENGTH = 5000;
    private static TextArea textArea;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    @Override
    protected void append(LoggingEvent loggingEvent) {
        if(textArea != null) {
            String time = LocalDateTime.ofInstant(Instant.ofEpochMilli(loggingEvent.getTimeStamp()), ZoneId.systemDefault()).format(formatter);
            Platform.runLater(() -> {
                if(textArea.getText().length() > MAX_TEXT_LENGTH) {
                    textArea.setText("Clean up...\r\n");
                }
                textArea.appendText(time + " : " + loggingEvent.getRenderedMessage() + "\r\n");
            });
        }
    }

    public void setTextArea(TextArea textArea){
        this.textArea = textArea;
    }

    public void close() {
    }

    public boolean requiresLayout() {
        return false;
    }
}