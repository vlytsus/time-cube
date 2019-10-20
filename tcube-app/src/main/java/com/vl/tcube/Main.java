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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private TextField shoresField = new TextField ();
    private TextField workField = new TextField ();
    private TextField restField = new TextField ();
    private TextField learnField = new TextField ();
    private TextArea textArea = new TextArea();

    private TimeTrackingService timeService = new TimeTrackingService(new ActivityFactory());
    private ObservableService gyroSerialListenerService = new WorkdaySimulatorService();//new GyroSerialListenerServiceImpl(timeService);

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
                        if(currentActivity != null){
                            textArea.setText(currentActivity.getDuration() + " " + currentActivity.getType().name() + "\r\n" + textArea.getText());
                        }
                        shoresField.setText("" + timeService.getShoresDuration());
                        workField.setText("" + timeService.getWorkDuration());
                        restField.setText("" + timeService.getRestDuration());
                        learnField.setText("" + timeService.getLearnDuration());
                    }
                });
            }
        });
        gyroSerialListenerService.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        StackPane root = new StackPane();
        /*Parent root = FXMLLoader.load(getClass().getResource("tcube.fxml"));*/
        primaryStage.setTitle("Time Cube");
        primaryStage.setScene(new Scene(root, 600, 500));

        Button btn = new Button();
        btn.setText("Connect");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                textArea.setText("Start connection to Time Cube...");
            }
        });

        HBox hb = new HBox(10);
        VBox vb = new VBox(10);
        vb.getChildren().addAll(new Label("Shores:"), shoresField);
        vb.getChildren().addAll(new Label("Work:"), workField);
        vb.getChildren().addAll(new Label("Rest:"), restField);
        vb.getChildren().addAll(new Label("Learn:"), learnField);

        hb.getChildren().addAll(vb);
        vb.getChildren().add(textArea);
        vb.getChildren().add(btn);
        root.getChildren().add(hb);
        hb.setAlignment(Pos.CENTER);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
