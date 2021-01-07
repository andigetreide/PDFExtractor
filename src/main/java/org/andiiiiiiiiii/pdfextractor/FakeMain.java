package org.andiiiiiiiiii.pdfextractor;

// FakeMain does not extend Application (like the "real" Main)
// Therefore it can be started with java -jar swt.jar
// ("-add-modules --add-modules javafx.controls,javafx.fxml -p /path/to/javafx-sdk/lib" not needed any more)
// (if we have a at jar containing all dependencies, like the JavaFX runtime components, etc.)
// see also https://www.youtube.com/watch?v=HuFOCEHh8Zg
public class FakeMain {

    public static void main(String[] args) {
        Main.realMain(args);
    }

}

