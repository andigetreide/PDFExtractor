package org.andiiiiiiiiii.pdfextractor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage)  {
        try
        {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("PDFExtractor.fxml"));
            //Parent root = FXMLLoader.load(getClass().getResource("PDFExtractor.fxml"));  // this is the original line

            /*
            TODO: still don't know why the .getClassLoader() is needed.  From stackoverflow:
            This solved it for me, too. Anybody know why getClassLoader() makes a difference? – Phillip Feb 21 '16 at 19:03
            If you are using an IDE, the problem may be that the IDE does not know that it needs to copy the .fxml file into the output directory alongside the class files during a build, which is where getClass().getResource() will look for it (it won't look in your source tree!). For instance, if you are using IntelliJ, you may need to add something like ";?.fxml;?.css" to your File|Settings|Compiler settings to tell it to copy the files during a build. See stackoverflow.com/questions/23421325/… for more information.
            */

            primaryStage.setTitle("PDF Extractor");
            primaryStage.setScene(new Scene(root /*, 300, 275*/));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        //PaneDemo.start(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }

}
