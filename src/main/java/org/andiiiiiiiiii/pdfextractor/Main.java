package org.andiiiiiiiiii.pdfextractor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage)  {
        try
        {
            /*
            This was the original line, but getClass().getResource("PDFExtractor.fxml") returns null for unknown reasons:
            Parent root = FXMLLoader.load(getClass().getResource("PDFExtractor.fxml"));
            According to stackoverflow, the .getClassLoader() is needed.  From stackoverflow:
            This solved it for me, too. Anybody know why getClassLoader() makes a difference? – Phillip Feb 21 '16 at 19:03
            If you are using an IDE, the problem may be that the IDE does not know that it needs to copy the .fxml file into the output directory alongside the class files during a build, which is where getClass().getResource() will look for it (it won't look in your source tree!). For instance, if you are using IntelliJ, you may need to add something like ";?.fxml;?.css" to your File|Settings|Compiler settings to tell it to copy the files during a build. See stackoverflow.com/questions/23421325/… for more information.
            */
            // Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("PDFExtractor.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("PDFExtractor.fxml"));
            Parent root = loader.load();
            ((Controller)loader.getController()).setStage(primaryStage);

            primaryStage.setTitle("PDF Extractor");
            primaryStage.setScene(new Scene(root /*, 300, 275*/));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    // TODO: Command line parameters not supported yet
    // TODO: Scroll pane, make output text area much bigger and more responsive, introduce line breaks (i.e. autowrap)
    public static void main(String[] args) {

        if (args.length > 0) {
            System.out.println("Started with arguments, arguments not implemented yet, therefore quitting... bye!");
            System.exit(-1);
        }

        launch(args);
    }

}
