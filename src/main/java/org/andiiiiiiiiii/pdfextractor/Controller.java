package org.andiiiiiiiiii.pdfextractor;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.andiiiiiiiiii.pdfextractor.messageboxes.AboutBox;


import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public TextField outputDirectoryText;
    @FXML
    public TextField inputPDFFileText;
    @FXML
    public TextArea outputText;
    @FXML
    public CheckBox overwriteFilesCheckbox;
    @FXML
    public CheckBox exportImagesCheckbox;
    @FXML
    public CheckBox exportTextPerPageCheckbox;
    @FXML
    public CheckBox exportSingleTextfileCheckbox;
    @FXML
    public CheckBox addPageNumbersCheckbox;
    @FXML
    public ProgressBar progressBar;

    private Stage stage;

    // don't forget to set the stage from the main program before using this controller!
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    PrintStream outputTextStream;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // TODO: add UTF-8 support, does Java internally work with 16 bit characters?  UTF-16???
        // from https://stackoverflow.com/questions/36638617/javafx-textarea-update-immediately
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int i) /* throws IOException */ {
                if (Platform.isFxApplicationThread()) {
                    outputText.appendText(String.format("%c", (char) i));
                } else {
                    Platform.runLater(() -> outputText.appendText(String.format("%c", (char) i)));
                }
            }
        };

        outputTextStream = new PrintStream(outputStream /*, true, StandardCharsets.UTF_8 */);

        addPageNumbersCheckbox.disableProperty().bind(exportSingleTextfileCheckbox.selectedProperty().not());

        // from https://stackoverflow.com/questions/17799160/javafx-textarea-and-autoscroll
        outputText.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                                Object newValue) {
                outputText.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });
    }


    @FXML
    private void onNew() {
        outputDirectoryText.setText("");
        inputPDFFileText.setText("");
        outputText.setText("");
    }

    @FXML
    private void onOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PDF File");

        if (new File(inputPDFFileText.getText()).getParent() != null)
            fileChooser.setInitialDirectory(new File(new File(inputPDFFileText.getText()).getParent()));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            inputPDFFileText.setText(selectedFile.getPath());
        }
    }

    @FXML
    private void onSaveAs() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Set target directory");

        if (new File(inputPDFFileText.getText()).getParent() != null)
            directoryChooser.setInitialDirectory(new File(new File(inputPDFFileText.getText()).getParent()));

        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            outputDirectoryText.setText(selectedDirectory.getPath());
        }
    }

    @FXML
    private void onQuit() {
        ((Stage) outputText.getScene().getWindow()).close();
    }

    @FXML
    private void onWhatIsThis() {
        String text = "PDF Extractor automatically extracts\n" +
                "all images and text from a PDF file.\n" +
                "Hopefully, the user interface is rather self-explaining. :-)";

        AboutBox aboutBox = new AboutBox();
        aboutBox.show(text);
    }

    @FXML
    private void onAbout() {
        String text = "PDF Extractor 0.1\n" +
            "contains some OSS...\n" +
            "By Andi";

        AboutBox aboutBox = new AboutBox();
        aboutBox.show(text);
    }

    @FXML
    private void onStart() {

        Extractor extractor = new Extractor(
                overwriteFilesCheckbox.isSelected(),
                exportImagesCheckbox.isSelected(),
                exportTextPerPageCheckbox.isSelected(),
                exportSingleTextfileCheckbox.isSelected(),
                addPageNumbersCheckbox.isSelected()
        );

        Thread thread = new Thread(() -> {
            progressBar.setDisable(false);
            progressBar.progressProperty().bind(extractor.progressProperty());
            extractor.extract(
                    inputPDFFileText.getText(),
                    outputDirectoryText.getText(),
                    outputTextStream,
                    outputTextStream
            );
            progressBar.progressProperty().unbind();
            progressBar.progressProperty().set(0);
            progressBar.setDisable(true);
        });
        thread.start();

    }

}
