package com.vl.tcube;

import com.google.common.collect.Queues;
import com.vl.tcube.activity.Activity;
import com.vl.tcube.activity.ActivityFactory;
import com.vl.tcube.activity.TimeTrackingService;
import com.vl.tcube.comm.CommunicationObserver;
import com.vl.tcube.comm.CubePositionMessage;
import com.vl.tcube.comm.SerialListenerService;
import com.vl.tcube.comm.UpdateEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import com.google.common.collect.EvictingQueue;

import java.util.Queue;

public class Main extends Application {

    private TextField shoresField = new TextField ();
    private TextField workField = new TextField ();
    private TextField restField = new TextField ();
    private TextField learnField = new TextField ();

    private TimeTrackingService timeService = new TimeTrackingService(new ActivityFactory());
    private SerialListenerService listener = new SerialListenerService(timeService);

    @Override
    public void init() throws Exception {
        super.init();
        listener.addCommunicationObserver(new CommunicationObserver() {
            @Override
            public void onCubePositionMessage(CubePositionMessage msg) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        timeService.startActivity(msg.getxPos(), msg.getyPos(), msg.getzPos());
                        shoresField.setText("" + timeService.getShoresDuration().getSeconds()/3600);
                        workField.setText("" + timeService.getWorkDuration().getSeconds()/3600);
                        restField.setText("" + timeService.getRestDuration().getSeconds()/3600);
                        learnField.setText("" + timeService.getLearnDuration().getSeconds()/3600);
                    }
                });
            }
        });
        listener.start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        StackPane root = new StackPane();
        /*Parent root = FXMLLoader.load(getClass().getResource("tcube.fxml"));*/
        primaryStage.setTitle("Time Cube");
        primaryStage.setScene(new Scene(root, 600, 500));

        final TextArea textArea = new TextArea();
        Button btn = new Button();
        btn.setText("Connect");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                textArea.setText("Start connection to Time Cube...");
            }
        });


        Label label1 = new Label("Shores:");
        //TextField totalField = new TextField ();
        shoresField.textProperty();

        Label label2 = new Label("Work:");
        //TextField workField = new TextField ();

        Label label3 = new Label("Rest:");
        //TextField restField = new TextField ();

        Label label4 = new Label("Learn:");
        //TextField learnField = new TextField ();

        HBox hb = new HBox(10);
        VBox vb = new VBox(10);
        vb.getChildren().addAll(label1, shoresField);
        vb.getChildren().addAll(label2, workField);
        vb.getChildren().addAll(label3, restField);
        vb.getChildren().addAll(label4, learnField);

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
