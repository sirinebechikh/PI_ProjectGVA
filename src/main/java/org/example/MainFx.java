package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class MainFx extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin_dashboard.fxml"));
        AnchorPane root = loader.load();

        // Set the scene with the root layout (Admin interface)
        Scene scene = new Scene(root);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);

        // Show the primary stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
