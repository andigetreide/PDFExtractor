package org.andiiiiiiiiii.pdfextractor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;


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

    PrintStream outputTextStream;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // TODO: add UTF-8 support
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

        // TODO: instead of getscene.getwindow mainstage should be directly accessible here may be with an fx:id???
        File selectedFile = fileChooser.showOpenDialog(inputPDFFileText.getScene().getWindow());

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

        // TODO: instead of getscene.getwindow mainstage should be directly accessible here may be with an fx:id???
        File selectedDirectory = directoryChooser.showDialog(inputPDFFileText.getScene().getWindow());

        // File selectedDirectory = directoryChooser.showDialog(stage.getScene().getWindow());

        if (selectedDirectory != null) {
            outputDirectoryText.setText(selectedDirectory.getPath());
        }
    }

    @FXML
    private void onPreferences() {

    }

    @FXML
    private void onQuit() {

    }

    @FXML
    private void onWhatIsThis() {

    }

    @FXML
    private void onAbout() {

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
