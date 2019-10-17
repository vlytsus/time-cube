package com.vl.tcube;

import javafx.application.Application;
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
import org.ardulink.core.Link;
import org.ardulink.core.convenience.Links;

public class Main extends Application {

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


        Label label1 = new Label("Total:");
        TextField textField1 = new TextField ();

        Label label2 = new Label("Work:");
        TextField textField2 = new TextField ();

        Label label3 = new Label("Rest:");
        TextField textField3 = new TextField ();

        Label label4 = new Label("Learn:");
        TextField textField4 = new TextField ();

        HBox hb = new HBox(10);

        VBox vb = new VBox(10);
        vb.getChildren().addAll(label1, textField1);
        vb.getChildren().addAll(label2, textField2);
        vb.getChildren().addAll(label3, textField3);
        vb.getChildren().addAll(label4, textField4);

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
