package com.vl.tcube;

import com.vl.tcube.activity.Activity;
import com.vl.tcube.activity.ActivityFactory;
import com.vl.tcube.activity.TimeTrackingService;
import com.vl.tcube.comm.CommunicationObserver;
import com.vl.tcube.comm.CubePositionMessage;
import com.vl.tcube.comm.ObservableService;
import com.vl.tcube.comm.WorkdaySimulatorService;
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
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main extends Application {

    static final Logger logger = LoggerFactory.getLogger(Main.class);
    static final Logger activityLog = LoggerFactory.getLogger("ACTIVITY_LOG");
    private TextField choresField = new TextField ();
    private TextField workField = new TextField ();
    private TextField restField = new TextField ();
    private TextField learnField = new TextField ();
    private TextArea textArea = new TextArea();

    private TimeTrackingService timeService = new TimeTrackingService(new ActivityFactory());
    private ObservableService gyroSerialListenerService = new WorkdaySimulatorService();
    //new GyroSerialListenerService(timeService);

    @Override
    public void init() throws Exception {
        super.init();
        gyroSerialListenerService.addCommunicationObserver(new CommunicationObserver() {
            @Override
            public void onCubePositionMessage(CubePositionMessage msg) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        timeService.startActivity(msg.getxPos(), msg.getyPos(), msg.getzPos());
                        Activity currentActivity = timeService.getCurrentActivity();
                        activityLog.info(currentActivity.getType().name());
                        if(currentActivity != null){
                            textArea.setText(LocalDateTime.now() + " : " + currentActivity.getType().name() + "\r\n" + textArea.getText());
                        }
                        choresField.setText(formatDuration(timeService.getChoresDuration()));
                        workField.setText(formatDuration(timeService.getWorkDuration()));
                        restField.setText(formatDuration(timeService.getRestDuration()));
                        learnField.setText(formatDuration(timeService.getLearnDuration()));
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
        primaryStage.setScene(new Scene(root, 600, 500));

        HBox hb = new HBox(10);
        VBox vb = new VBox(10);
        hb.getChildren().addAll(vb);

        HBox workBox = new HBox();
        workBox.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(workBox);
        Label workLabel = new Label("Work:");
        workLabel.setPrefWidth(55);
        workBox.getChildren().addAll(workLabel, workField);

        HBox choresBox = new HBox();
        choresBox.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(choresBox);
        Label choresLabel = new Label("Chores:");
        choresLabel.setPrefWidth(55);
        choresBox.getChildren().addAll(choresLabel, choresField);

        HBox restBox = new HBox();
        restBox.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(restBox);
        Label restLabel = new Label("Rest:");
        restLabel.setPrefWidth(55);
        restBox.getChildren().addAll(restLabel, restField);

        HBox learnBox = new HBox();
        learnBox.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(learnBox);
        Label learnLabel = new Label("Learn:");
        learnLabel.setPrefWidth(55);
        learnBox.getChildren().addAll(learnLabel, learnField);

        HBox textBox = new HBox();
        textBox.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(textBox);
        textBox.setPrefWidth(450);
        textArea.setPrefWidth(350);
        textBox.getChildren().add(textArea);

        root.getChildren().add(hb);
        vb.setAlignment(Pos.CENTER);

        primaryStage.setWidth(450);
        primaryStage.show();
    }

    private String formatDuration(Duration duration) {
        return String.format("%dh : %02dm",
                duration.toHours(),
                duration.toMinutes());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
