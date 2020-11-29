package org.andiiiiiiiiii.pdfextractor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
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

    private Stage stage;

    // don't forget to set the stage from the main progrma before using this controller!
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    PrintStream outputTextStream;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // TODO: add UTF-8 support, does Java internally work wth 16 bit characters?  UTF-16???
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int i) /* throws IOException */ {
                outputText.appendText(String.format("%c", (char) i));
            }
        };
        outputTextStream = new PrintStream(outputStream /*, true, StandardCharsets.UTF_8 */);

        addPageNumbersCheckbox.disableProperty().bind(exportSingleTextfileCheckbox.selectedProperty().not());

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

        try {
            fileChooser.setInitialDirectory(new File(new File(inputPDFFileText.getText()).getParent()));
        } catch(Exception e)  {}

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

        try {
            directoryChooser.setInitialDirectory(new File(new File(inputPDFFileText.getText()).getParent()));
        } catch (Exception e) {}

        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            outputDirectoryText.setText(selectedDirectory.getPath());
        }
    }

    @FXML
    private void onPreferences() {

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
        // outputText.appendText("Hi");
        // outputTextStream.print("Helloäüé");
        // outputTextStream.print("Hello");

        Extractor extractor = new Extractor(
                overwriteFilesCheckbox.isSelected(),
                exportImagesCheckbox.isSelected(),
                exportTextPerPageCheckbox.isSelected(),
                exportSingleTextfileCheckbox.isSelected(),
                addPageNumbersCheckbox.isSelected()
        );

        extractor.extract(
                inputPDFFileText.getText(),
                outputDirectoryText.getText(),
                outputTextStream,
                outputTextStream
        );

    }

}
