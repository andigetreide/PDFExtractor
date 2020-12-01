package org.andiiiiiiiiii.pdfextractor.messageboxes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AboutBox {

    public void show(String text) {
        Stage stage= new Stage();
        stage.setWidth(350);
        Button button = new Button("OK");

        Label label = new Label();
        label.setText(text);
        label.setAlignment(Pos.CENTER);

        button.setOnAction(e -> stage.close());

        VBox root = new VBox();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(label, button);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.showAndWait();
    }



}
