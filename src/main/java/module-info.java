module com.example.snakeai {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.snakeai to javafx.fxml;
    exports com.example.snakeai;
}