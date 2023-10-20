module com.example.learn2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires jlayer;

    opens com.example.learn2 to javafx.fxml, javafx.controls, javafx.graphics, javafx.base, javafx.media;

    exports com.example.learn2;
    exports com.example.learn2.TranslateController;
    opens com.example.learn2.TranslateController to javafx.base, javafx.controls, javafx.fxml, javafx.graphics, javafx.media;
}