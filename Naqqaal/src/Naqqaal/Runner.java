package Naqqaal;

import java.io.File;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Lenovo 520
 */
public class Runner extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private InitialScreenAnchorPane initialScreenAnchorPane;
    private NewProjectStage newProjectScreen;
    static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        Runner.primaryStage = primaryStage;

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    System.exit(0);
                } else if (alert.getResult() == ButtonType.CANCEL) {
                    event.consume();
                }

            }
        });
//        Project project = new Project("", "E:/Sample_English_Video.mp4");
//        MainScreenBorderPane mainScreenBorderPane = new MainScreenBorderPane(project);
        initialScreenAnchorPane = new InitialScreenAnchorPane();

        initialScreenAnchorPane.getNewProjectButton().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                newProjectScreen = new NewProjectStage();
                newProjectScreen.show();
            }
        });
        initialScreenAnchorPane.getOpenProjectButton().setOnMousePressed(new OpenProjectEventHandler());
        initialScreenAnchorPane.getRecentProjectButton().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Project project = Project.readProjectFile("E:\\ASUG test files\\pakistan\\Project.ser");
                MainScreenBorderPane mainScreenBorderPane = new MainScreenBorderPane(project);
                Scene scene = new Scene(mainScreenBorderPane, 1920, 990);
                primaryStage.setResizable(true);
                primaryStage.setTitle("Naqqaal 1.0 - " + "E:\\ASUG test files\\pakistan\\Project.ser [HARD CODED PATH]");
                primaryStage.setScene(scene);
                primaryStage.setMaximized(true);
            }
        });

        Scene scene = new Scene(initialScreenAnchorPane, 600, 400);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Naqqaal 1.0 - Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
