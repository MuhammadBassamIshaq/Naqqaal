/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Naqqaal;

import java.io.File;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

/**
 *
 * @author Lenovo 520
 */
public class OpenProjectEventHandler implements EventHandler {

    @Override
    public void handle(Event event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".ser", "*.ser");

        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showOpenDialog(Runner.primaryStage);

        if (file != null) {
            String filePath = file.toPath().toString();
            Project project = Project.readProjectFile(filePath);
            MainScreenBorderPane mainScreenBorderPane = new MainScreenBorderPane(project);
            Scene scene = new Scene(mainScreenBorderPane, 1920, 990);
            Runner.primaryStage.setResizable(true);
            Runner.primaryStage.setTitle("Naqqaal 1.0 - " + file.getName());
            Runner.primaryStage.setScene(scene);
            Runner.primaryStage.setMaximized(true);

        }
    }

}
