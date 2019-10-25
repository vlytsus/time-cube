package com.vl.tcube;

import com.vl.tcube.activity.Activity;
import com.vl.tcube.activity.ActivityFactory;
import com.vl.tcube.activity.TimeTrackingService;
import com.vl.tcube.comm.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;

public class Main extends Application {

    static final Logger logger = LoggerFactory.getLogger(Main.class);
    static final Logger activityLog = LoggerFactory.getLogger("ACTIVITY_LOG");
    private TextArea time = new TextArea();
    private TextArea log = new TextArea();
    private TimeTrackingService timeService;
    private ObservableService gyroSerialListenerService;

    @Override
    public void init() throws Exception {
        super.init();
        AppConfig config = new AppConfig();
        timeService = new TimeTrackingService(new ActivityFactory());
        gyroSerialListenerService = //new WorkdaySimulatorService();
                new GyroSerialListenerService(timeService, config.getPreferPort());
        new TextAreaAppender().setTextArea(log);

        gyroSerialListenerService.addCommunicationObserver(new CommunicationObserver() {
            @Override
            public void onCubePositionMessage(CubePositionMessage msg) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        timeService.startActivity(msg.getxPos(), msg.getyPos(), msg.getzPos());
                        Activity currentActivity = timeService.getCurrentActivity();
                        activityLog.info(currentActivity.getType().name());
                        time.setText("");
                        time.appendText("  Work: ");
                        time.appendText(formatDuration(timeService.getWorkDuration()));
                        time.appendText("   Total: ");
                        time.appendText(formatDuration(timeService.getTotalDuration()));
                        time.appendText("\r\n");
                        time.appendText("Chores: ");
                        time.appendText(formatDuration(timeService.getChoresDuration()));
                        time.appendText("\r\n");
                        time.appendText(" Learn: ");
                        time.appendText(formatDuration(timeService.getLearnDuration()));
                        time.appendText("\r\n");
                        time.appendText("  Rest: ");
                        time.appendText(formatDuration(timeService.getRestDuration()));
                    }
                });
            }
        });
        gyroSerialListenerService.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Application Start");
        StackPane root = new StackPane();
        /*Parent root = FXMLLoader.load(getClass().getResource("tcube.fxml"));*/
        primaryStage.setTitle("Time Cube");
        primaryStage.setScene(new Scene(root, 440, 420));
        primaryStage.setResizable(false);

        HBox hb = new HBox(10);
        VBox vb = new VBox(10);
        hb.getChildren().addAll(vb);

        log.setEditable(false);
        time.setEditable(false);
        time.setPrefHeight(90);
        log.setPrefHeight(300);
        time.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        log.setFont(Font.font("Monospaced", FontWeight.NORMAL, 14));
        vb.getChildren().addAll(time, log);

        root.getChildren().add(hb);
        hb.setAlignment(Pos.CENTER);
        vb.setAlignment(Pos.CENTER);
        primaryStage.show();
    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
