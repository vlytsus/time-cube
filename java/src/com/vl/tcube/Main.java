package com.vl.tcube;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        StackPane root = new StackPane();
        /*Parent root = FXMLLoader.load(getClass().getResource("tcube.fxml"));*/
        primaryStage.setTitle("Time Cube");
        primaryStage.setScene(new Scene(root, 300, 250));

        final TextArea textArea = new TextArea();
        Button btn = new Button();
        btn.setText("Connect");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                textArea.setText("Start connection to Time Cube...");
            }
        });

        root.getChildren().add(textArea);
        root.getChildren().add(btn);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
