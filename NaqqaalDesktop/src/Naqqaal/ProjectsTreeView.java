package Naqqaal;

import java.io.File;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Lenovo 520
 */
public final class ProjectsTreeView extends AnchorPane {

    public ProjectsTreeView(String dirctoryPath) {
        File file = new File(dirctoryPath);
        TreeItem parentTree = new TreeItem();
        parentTree.setExpanded(true);
        TreeView<String> treeView = new TreeView<>(parentTree);

        treeView.setShowRoot(false);
        createTree(file, parentTree);
//        parentTree.get

//        AnchorPane tree_AnchorPane = new AnchorPane();
        AnchorPane.setBottomAnchor(treeView, 0.0);
        AnchorPane.setTopAnchor(treeView, 0.0);
        AnchorPane.setLeftAnchor(treeView, 0.0);
        AnchorPane.setRightAnchor(treeView, 0.0);

        getChildren().add(treeView);

    }

    private void createTree(File root_file, TreeItem parent) {
        if (root_file.isDirectory()) {
            TreeItem node = new TreeItem(root_file.getName());

            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("images/folder.png")));
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            node.setGraphic(imageView);

            parent.getChildren().add(node);
            for (File f : root_file.listFiles()) {

                TreeItem placeholder = new TreeItem(); // Add TreeItem to make parent expandable even it has no child yet.
                node.getChildren().add(placeholder);

                // When parent is expanded continue the recursive
                node.addEventHandler(TreeItem.branchExpandedEvent(), new EventHandler() {
                    @Override
                    public void handle(Event event) {
                        createTree(f, node); // Continue the recursive as usual
                        node.getChildren().remove(placeholder); // Remove placeholder
                        node.removeEventHandler(TreeItem.branchExpandedEvent(), this); // Remove event
                    }
                });

            }
        } else {
            String extension = "";

            int i = root_file.getName().lastIndexOf('.');
            if (i > 0) {
                extension = root_file.getName().substring(i + 1);
            }
            if (extension.equals("txt") || extension.equals("srt")) {

                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("images/textFile_icon.png")));
                imageView.setFitWidth(25);
                imageView.setFitHeight(25);
                TreeItem node = new TreeItem(root_file.getName(), imageView);

                node.setGraphic(imageView);
                parent.getChildren().add(node);
            } else if (extension.equals("mp4") || extension.equals("mkv")) {

                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("images/video_icon.png")));
                imageView.setFitWidth(25);
                imageView.setFitHeight(25);
                TreeItem node = new TreeItem(root_file.getName(), imageView);

                node.setGraphic(imageView);
                parent.getChildren().add(node);
            } else if (extension.equals("wav") || extension.equals("mp3")) {

                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("images/audio_icon.png")));
                imageView.setFitWidth(25);
                imageView.setFitHeight(25);
                TreeItem node = new TreeItem(root_file.getName(), imageView);

                node.setGraphic(imageView);
                parent.getChildren().add(node);
            }
        }
    }
}
