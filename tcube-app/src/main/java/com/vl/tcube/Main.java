package com.vl.tcube;

import com.vl.tcube.activity.Activity;
import com.vl.tcube.activity.ActivityFactory;
import com.vl.tcube.activity.ActivityType;
import com.vl.tcube.activity.TimeTrackingService;
import com.vl.tcube.comm.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Logger activityLog = LoggerFactory.getLogger("ACTIVITY_LOG");
    public static final String NEW_LINE = "\r\n";
    public static final String SIMULATOR_PORT = "SIMULATOR";
    public static final int TITLE_LEFT_SPAN = 10;
    private TextArea time = new TextArea();
    private TextArea log = new TextArea();
    private TimeTrackingService timeService;
    private ObservableService portListenerService;

    @Override
    public void init() throws Exception {
        super.init();
        initTextAreaLogAppender();
        timeService = new TimeTrackingService(new ActivityFactory());
        initPortListenerService();
    }

    private void initPortListenerService() {
        AppConfig config = new AppConfig();
        logger.info("Application Start...");
        logger.info("Prefered port: " + config.getPreferPort());
        printActivityConfig(ActivityType.WORK);
        printActivityConfig(ActivityType.CHORES);
        printActivityConfig(ActivityType.LEARN);
        printActivityConfig(ActivityType.REST);
        portListenerService = SIMULATOR_PORT.equals(config.getPreferPort()) ?
                new WorkdaySimulatorService() : new SerialListenerService(timeService, config.getPreferPort());

        portListenerService.addCommunicationObserver(new CommunicationObserver() {
            @Override
            public void onCubePositionMessage(CubePositionMessage msg) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        timeService.startActivity(msg.getxPos(), msg.getyPos(), msg.getzPos());
                        Activity currentActivity = timeService.getCurrentActivity();
                        logger.info(currentActivity.getType().getTitle() + " (x:" + msg.getxPos() + " y:" + msg.getyPos() + " z:" + msg.getzPos() + ")");
                        activityLog.info(currentActivity.getType().getTitle());
                        time.setText("");
                        time.appendText(formatTitle(ActivityType.WORK));
                        time.appendText(formatDuration(timeService.getWorkDuration()));
                        time.appendText("   Total: ");
                        time.appendText(formatDuration(timeService.getTotalDuration()));
                        time.appendText(NEW_LINE);
                        time.appendText(formatTitle(ActivityType.CHORES));
                        time.appendText(formatDuration(timeService.getChoresDuration()));
                        time.appendText(NEW_LINE);
                        time.appendText(formatTitle(ActivityType.LEARN));
                        time.appendText(formatDuration(timeService.getLearnDuration()));
                        time.appendText(NEW_LINE);
                        time.appendText(formatTitle(ActivityType.REST));
                        time.appendText(formatDuration(timeService.getRestDuration()));
                    }
                });
            }
        });
        portListenerService.start();
    }

    private void initTextAreaLogAppender() {
        new TextAreaAppender().setTextArea(log);
    }

    private String formatTitle(ActivityType type) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i< TITLE_LEFT_SPAN - type.getTitle().length(); i++ ){
            sb.append(' ');
        }
        sb.append(type.getTitle());
        sb.append(": ");
        return sb.toString();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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

    private void printActivityConfig(ActivityType type) {
        logger.info(formatTitle(type)
                 + " (" + type.getxStart() + " > X > " + type.getxEnd()
                + ") (" + type.getyStart() + " > Y > " + type.getyEnd()
                + ") (" + type.getzStart() + " > Z > " + type.getzEnd() + ")");
    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
