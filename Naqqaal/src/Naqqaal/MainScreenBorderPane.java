/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Naqqaal;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.StatusBar;

/**
 *
 * @author Lenovo 520
 */
public class MainScreenBorderPane extends BorderPane {

    private MenuBar menuBar;
    private SplitPane mainSplitPane;
    private Project project;
    private BorderPane videoTextBorderPane;
    private RangeSliderAnchorPane rangeSliderAnchorPane;
    private VideoControlsAnchorPane videoControlsAnchorPane;
    private SplitPane video_text_SplitPane;
    private VideoAreaPane videoAreaPane;
    private TextEditorAnchorPane textEditorAnchorPane;
    private StatusBar statusBar;
    private ProjectsTreeView projectsTreeView;
    public static Subtitle currentSubtitle;
    Timer timer = null;
    private boolean videoSliderClicked = false;
    private int indexTillScanned = 0; // for find functionality

    public MainScreenBorderPane(Project project_) {

        this.project = project_;

        menuBar = new MenuBar();
        videoTextBorderPane = new BorderPane();
        mainSplitPane = new SplitPane();
        rangeSliderAnchorPane = new RangeSliderAnchorPane();
        videoControlsAnchorPane = new VideoControlsAnchorPane();
        video_text_SplitPane = new SplitPane();
        videoAreaPane = new VideoAreaPane(this.project.getMediaPath());
        textEditorAnchorPane = new TextEditorAnchorPane(this.project.getSubtitleArrayList(), this.project.getSpeakerNamesArrayList());
        statusBar = new StatusBar();
        projectsTreeView = new ProjectsTreeView("E:\\Laptop Data\\Movies");

        prepareMenuBar();

        rangeSliderAnchorPane.getRangeSlider().lowValueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                String timeRange = rangeSliderAnchorPane.getTimeRangeLabel().getText();
                String upperValue = timeRange.split("-->")[1].trim();
                int lowValue = (int) videoControlsAnchorPane.getVideoSlider().getValue()
                        + (int) rangeSliderAnchorPane.getRangeSlider().getLowValue();
                if (lowValue >= 0) {
                    rangeSliderAnchorPane.getTimeRangeLabel().setText(
                            millisToHMSM_String(lowValue) + " --> " + upperValue);
                } else {
                    rangeSliderAnchorPane.getTimeRangeLabel().setText(
                            millisToHMSM_String(0) + " --> " + upperValue);
                }

            }
        });
        rangeSliderAnchorPane.getRangeSlider().highValueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                String timeRange = rangeSliderAnchorPane.getTimeRangeLabel().getText();
                String lowerValue = timeRange.split("-->")[0].trim();
                int highValue = ((int) videoControlsAnchorPane.getVideoSlider().getValue())
                        + ((int) rangeSliderAnchorPane.getRangeSlider().getHighValue());
                int totalVideoDuration = (int) videoControlsAnchorPane.getVideoSlider().getMax();
                if (highValue <= totalVideoDuration) {
                    rangeSliderAnchorPane.getTimeRangeLabel().setText(lowerValue + " --> "
                            + millisToHMSM_String(highValue));
                } else {
                    rangeSliderAnchorPane.getTimeRangeLabel().setText(lowerValue + " --> "
                            + millisToHMSM_String(totalVideoDuration));

                }
            }
        });
        textEditorAnchorPane.getAddTimeStamp_Button().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                double totalStartMillis = videoControlsAnchorPane.getVideoSlider().getValue()
                        + rangeSliderAnchorPane.getRangeSlider().getLowValue();
                double totalStopMillis = videoControlsAnchorPane.getVideoSlider().getValue()
                        + rangeSliderAnchorPane.getRangeSlider().getHighValue();

                double startTime = totalStartMillis;
                double startHours = startTime / 3600000;
                int startHoursInt = (int) startHours;
                double startMins = ((startHours - startHoursInt) * 60);
                int startMinsInt = (int) startMins;
                double startSec = ((startMins - startMinsInt) * 60);
                int startSecInt = (int) startSec;
//                double startMilli = ((startSec - startSecInt) * 1000);
//                int startMilliInt = (int) startMilli;
                // milli seconds are extracted from range slider time range label otherwise milli seconds were not consistent (for accuracy purposes)
                String startMillis = rangeSliderAnchorPane.getTimeRangeLabel().getText().split("-->")[0].split(",")[1].trim();
                int startMilliInt = Integer.parseInt(startMillis);
                currentSubtitle.setStartHours(startHoursInt);
                currentSubtitle.setStartMin((int) startMins);
                currentSubtitle.setStartSec(startSecInt);
                currentSubtitle.setStartMilliSec(startMilliInt);
                currentSubtitle.setTotalStartMillis((int) totalStartMillis);

                double StopTime = totalStopMillis;
                double StopHours = StopTime / 3600000;
                int StopHoursInt = (int) StopHours;
                double StopMins = ((StopHours - StopHoursInt) * 60);
                int StopMinsInt = (int) StopMins;
                double StopSec = ((StopMins - StopMinsInt) * 60);
                int StopSecInt = (int) StopSec;
//                double StopMilli = ((StopSec - StopSecInt) * 1000);
//                int StopMilliInt = (int) StopMilli;
// milli seconds are extracted from range slider time range label otherwise milli seconds were not consistent (for accuracy purposes)
                String stopMillis = rangeSliderAnchorPane.getTimeRangeLabel().getText().split("-->")[1].split(",")[1].trim();
                int stopMilliInt = Integer.parseInt(stopMillis);

                currentSubtitle.setStopHours(StopHoursInt);
                currentSubtitle.setStopMin((int) StopMins);
                currentSubtitle.setStopSec(StopSecInt);
                currentSubtitle.setStopMilliSec(stopMilliInt);
                currentSubtitle.setTotalStopMillis((int) totalStopMillis);

                TextNode textNode = textEditorAnchorPane.getListView().getItems().get(currentSubtitle.getId() - 1);
                if (startTime >= 0) {
                    textNode.getLabelStartHours().setText(startHoursInt + "");
                    textNode.getLabelStartMin().setText((int) startMins + "");
                    textNode.getTextfieldStartSec().setText(startSecInt + "");
                    textNode.getTextfieldStartMilli().setText(startMilliInt + "");
                } else {
                    textNode.getLabelStartHours().setText(0 + "");
                    textNode.getLabelStartMin().setText(0 + "");
                    textNode.getTextfieldStartSec().setText(0 + "");
                    textNode.getTextfieldStartMilli().setText(0 + "");
                }

                textNode.getLabelStopHours().setText(StopHoursInt + "");
                textNode.getLabelStopMin().setText((int) StopMins + "");
                textNode.getTextfieldStopSec().setText(StopSecInt + "");
                textNode.getTextfieldStopMilli().setText(stopMilliInt + "");

            }
        });
        videoControlsAnchorPane.getVideoSlider().setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                videoSliderClicked = false;
            }
        });
        videoControlsAnchorPane.getVideoSlider().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // if video slider clicked do:
                videoSliderClicked = true;
                // 1) seek video to that time
                videoAreaPane.getMediaPlayer().seek(Duration.millis(videoControlsAnchorPane.getVideoSlider().getValue()));
                // 2) search for the subtitle at that time and highlight that subtitle in listView
//                Subtitle subtitle = Subtitle.searchSubtitleByTime((int) Duration.seconds(
//                        videoControlsAnchorPane.getVideoSlider().getValue()).toMillis(), project.getSubtitleArrayList());
//                if (subtitle != null) {
//                    // if subtitle found at that time
//                    textEditorAnchorPane.getListView().getSelectionModel().select(subtitle.getId() - 1);
//                    textEditorAnchorPane.getListView().scrollTo(subtitle.getId() - 1);
//                } else {
//
//                    textEditorAnchorPane.getListView().getSelectionModel().select(project.getSubtitleArrayList().size() - 1);
//                    textEditorAnchorPane.getListView().scrollTo(project.getSubtitleArrayList().size() - 1);
//                }
//
//                // 3) change the current subtitle to subtitle
//                currentSubtitle = subtitle;

                // 4) change the time at time Labels on video controls anchorPane
                double currentTime = videoAreaPane.getMediaPlayer().getCurrentTime().toMillis();
                double currentHours = currentTime / 3600000;
                int currentHoursInt = (int) currentHours;
                double currentMins = ((currentHours - currentHoursInt) * 60);
                int currentMinsInt = (int) currentMins;
                int currentSec = (int) ((currentMins - currentMinsInt) * 60);
                videoControlsAnchorPane.getCurrentTimeLabel().setText(currentHoursInt + ":" + ((int) currentMins) + ":" + currentSec);

                // 5) update range slider anchor pane
                prepareRangeSliderPane();
            }
        });
        videoControlsAnchorPane.getVolumeSlider().setValue(videoAreaPane.getMediaPlayer().getVolume() * 100); // mediaPlayer.getVolume() gives value between 0 and 1 but max value for slider is set to 100
        videoControlsAnchorPane.getVolumeSlider().valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                videoAreaPane.getMediaPlayer().setVolume(videoControlsAnchorPane.getVolumeSlider().getValue() / 100);
            }
        });
        videoControlsAnchorPane.getPlayPauseButton().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                videoControlsAnchorPane.getStopButton().setDisable(false);
                ToggleButton playPauseButton = videoControlsAnchorPane.getPlayPauseButton();
                boolean selected = playPauseButton.isSelected();
                if (!selected) {
                    videoAreaPane.getMediaPlayer().play();
                    playPauseButton.setText("⏸");
                    playPauseButton.setTooltip(new Tooltip("Pause"));
                    rangeSliderAnchorPane.setDisable(true);
                } else {
                    videoAreaPane.getMediaPlayer().pause();
                    playPauseButton.setText("▶");
                    playPauseButton.setTooltip(new Tooltip("Play"));
                    rangeSliderAnchorPane.setDisable(false);
                    prepareRangeSliderPane();
                }
            }
        });
        videoControlsAnchorPane.getStopButton().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                videoAreaPane.getMediaPlayer().stop();
                ToggleButton playPauseButton = videoControlsAnchorPane.getPlayPauseButton();
                playPauseButton.setText("▶");
                playPauseButton.setSelected(false);
                videoControlsAnchorPane.getStopButton().setDisable(true);
            }
        });
        videoControlsAnchorPane.getSlowButton().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double rate = videoAreaPane.getMediaPlayer().getRate();
                if (rate != 0.25) {
                    videoAreaPane.getMediaPlayer().setRate(rate - 0.25);
                }
                showSpeedPopup(videoAreaPane.getMediaPlayer().getRate() + "x", videoControlsAnchorPane.getSlowButton());
            }
        });
        videoControlsAnchorPane.getFastButton().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double rate = videoAreaPane.getMediaPlayer().getRate();
                if (rate != 2) {
                    videoAreaPane.getMediaPlayer().setRate(rate + 0.25);
                }
                showSpeedPopup(videoAreaPane.getMediaPlayer().getRate() + "x", videoControlsAnchorPane.getFastButton());
            }
        });
        videoControlsAnchorPane.getSkipBackwardButton().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                videoAreaPane.getMediaPlayer().seek(Duration.millis(videoControlsAnchorPane.getVideoSlider().getValue() - 5000));
            }
        });
        videoControlsAnchorPane.getSkipForwardButton().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                videoAreaPane.getMediaPlayer().seek(Duration.millis(videoControlsAnchorPane.getVideoSlider().getValue() + 5000));
            }
        });
        videoAreaPane.getMediaPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {

            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
//              1) Change value of video Slider
                videoControlsAnchorPane.getVideoSlider().setValue(newValue.toMillis());
//                System.out.println("newValue.toMillis(): " + ((int) newValue.toMillis() / 1000)
//                        + "   getTotalStartMillis(): " + ((int) subtitlesArrayList.get(currentSubtitleIndex).getTotalStartMillis() / 1000));
                // 2) search for the subtitle at that time and highlight that subtitle in listView
                Subtitle subtitle = Subtitle.searchSubtitleByTime((int) newValue.toMillis(), project.getSubtitleArrayList());
                if (!videoSliderClicked) { // if video Slider is not clicked
                    if (subtitle != null) {
                        // if subtitle found at that time
                        textEditorAnchorPane.getListView().getSelectionModel().select(subtitle.getId() - 1);
                        textEditorAnchorPane.getListView().scrollTo(subtitle.getId() - 1);
                    } else {
                        textEditorAnchorPane.getListView().getSelectionModel().select(project.getSubtitleArrayList().size() - 1);
                        textEditorAnchorPane.getListView().scrollTo(project.getSubtitleArrayList().size() - 1);
                    }

                    // 3) change the current subtitle to subtitle
                    currentSubtitle = subtitle;
                }
                // 4) change the time at time Labels on video controls anchorPane
                double currentTime = videoAreaPane.getMediaPlayer().getCurrentTime().toMillis();
                double currentHours = currentTime / 3600000;
                int currentHoursInt = (int) currentHours;
                double currentMins = ((currentHours - currentHoursInt) * 60);
                int currentMinsInt = (int) currentMins;
                double currentSec = ((currentMins - currentMinsInt) * 60);
                int currentSecInt = (int) currentSec;
                double currentMilli = ((currentSec - currentSecInt) * 1000);
                int currentMilliInt = (int) currentMilli;
                String timeText = String.format("%01d:%02d:%02d:%03d", (int) currentHoursInt, (int) currentMins, currentSecInt, currentMilliInt);
                videoControlsAnchorPane.getCurrentTimeLabel().setText(timeText);
            }
        });
        videoAreaPane.getMediaPlayer().setOnReady(() -> {
            double totalDurationMillis = videoAreaPane.getMediaPlayer().getTotalDuration().toMillis();
            videoControlsAnchorPane.getVideoSlider().setMax(totalDurationMillis);
            mainSplitPane.setDividerPosition(0, 0.15);

            double totalTime = totalDurationMillis;
            double totalHours = totalTime / 3600000;
            int totalHoursInt = (int) totalHours;
            double totalMins = ((totalHours - totalHoursInt) * 60);
            int totalMinsInt = (int) totalMins;
            double totalSec = ((totalMins - totalMinsInt) * 60);
            int totalSecInt = (int) totalSec;
            double totalMilli = ((totalSec - totalSecInt) * 1000);
            int totalMilliInt = (int) totalMilli;
            String timeText = String.format("%01d:%02d:%02d:%03d", (int) totalHoursInt, (int) totalMins, totalSecInt, totalMilliInt);
            videoControlsAnchorPane.getTotalTimeLabel().setText("   / " + timeText);
        });

        ObservableList<TextNode> items = textEditorAnchorPane.getListView().getItems();

        for (int i = 0; i < items.size(); i++) {
            TextNode textNode = items.get(i);
            Button playPauseSubtitleButton = textNode.getPlaySubtitleButton();
            playPauseSubtitleButton.setOnMousePressed(new PlayPauseSubtitleEventHandler(textNode.getSubtitle(), playPauseSubtitleButton));

            textNode.getDeleteSubtitleButton().setOnMousePressed(new EventHandler() {
                @Override
                public void handle(Event event) {
                    items.remove(textNode);
                    project.getSubtitleArrayList().remove(textNode.getSubtitle());
                    for (int i = textNode.getSubtitle().getId() - 1; i < items.size(); i++) {
                        // can be done in parallel to improve performance
                        project.getSubtitleArrayList().get(i).setId(i + 1);
                        items.get(i).getLabel_SubtitleID().setText((i + 1) + "");

                    }
                }
            });

        }

        video_text_SplitPane.getItems().add(videoAreaPane);
        video_text_SplitPane.getItems().add(textEditorAnchorPane);

        videoTextBorderPane.setTop(rangeSliderAnchorPane);
        videoTextBorderPane.setBottom(videoControlsAnchorPane);
        videoTextBorderPane.setCenter(video_text_SplitPane);

//        mainSplitPane.setDividerPosition(0, 0.15);
        mainSplitPane.getItems().add(projectsTreeView);
        mainSplitPane.getItems().add(videoTextBorderPane);

        setTop(menuBar);
        setCenter(mainSplitPane);
        setBottom(statusBar);
        currentSubtitle = this.project.getSubtitleArrayList().get(0); // this line must be after initialization of subtitleArrayList 
    }

    private void prepareMenuBar() {
        Menu menu_File = new Menu("File");
        MenuItem menuItem_NewProject = new MenuItem("New Project...");
        MenuItem menuItem_OpenProject = new MenuItem("Open Project...");
        Menu menuItem_OpenRecentProject = new Menu("Open Recent Project"); // a sub menu will open with names all recent projects
        menuItem_OpenRecentProject.getItems().addAll(new MenuItem("dummy"), new MenuItem("dummy"), new MenuItem("dummy"), new MenuItem("dummy"), new MenuItem("dummy"));
        MenuItem menuItem_OpenTextFile = new MenuItem("Open txt or srt file...");
        MenuItem menuItem_Save = new MenuItem("Save");
        MenuItem menuItem_SaveAs = new MenuItem("Save As...");
        MenuItem menuItem_ExportSrtFile = new MenuItem("Export srt file...");
        MenuItem menuItem_ExportTextFile = new MenuItem("Export transcript file...");
        MenuItem menuItem_GenerateTranscript = new MenuItem("Generate Transcript...");
        MenuItem menuItem_Exit = new MenuItem("Exit");

        menu_File.getItems().addAll(menuItem_NewProject, menuItem_OpenProject, menuItem_OpenRecentProject, menuItem_OpenTextFile, menuItem_Save, menuItem_SaveAs, menuItem_ExportSrtFile, menuItem_ExportTextFile, menuItem_GenerateTranscript, menuItem_Exit);

        Menu menu_Edit = new Menu("Edit");
        MenuItem menuItem_Cut = new MenuItem("Cut");
        MenuItem menuItem_Copy = new MenuItem("Copy");
        MenuItem menuItem_Paste = new MenuItem("Paste");
        MenuItem menuItem_Find = new MenuItem("Find...");
        MenuItem menuItem_Replace = new MenuItem("Replace...");
        menu_Edit.getItems().addAll(menuItem_Cut, menuItem_Copy, menuItem_Paste, menuItem_Find, menuItem_Replace);

        Menu menu_View = new Menu("View");
        MenuItem menuItem_DarkMode = new MenuItem("Dark Mode");
        menu_View.getItems().addAll(menuItem_DarkMode);

        menuItem_NewProject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NewProjectStage newProjectScreen = new NewProjectStage();
                newProjectScreen.show();
            }
        });// New Project
        menuItem_OpenProject.setOnAction(new OpenProjectEventHandler()); // Open Project
        menuItem_OpenTextFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are about to change text file or srt file.\n"
                        + "Are you sure you want to continue ?");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.CANCEL) {
                    event.consume();
                } else if (alert.getResult() == ButtonType.OK) {
                    textEditorAnchorPane.getListView().getItems().clear();
                    project.getSubtitleArrayList().clear();

                    FileChooser fileChooser = new FileChooser();
                    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(".txt/.srt", "*.txt", "*.srt");

                    fileChooser.getExtensionFilters().add(filter);

                    File file = fileChooser.showOpenDialog(Runner.primaryStage);

                    if (file != null) {
                        String filePath = file.getPath();
                        String fileName = file.getName();
                        fileName = fileName.replace(".", "#");
                        String fileExtension = fileName.split("#")[1];
                        if (fileExtension.equalsIgnoreCase("srt")) {
                            ArrayList<Subtitle> readSrtFile = AllStaticMethods.readSrtFile(filePath);
                            project.setSubtitleArrayList(readSrtFile);

                            for (int i = 0; i < readSrtFile.size(); i++) {
                                textEditorAnchorPane.getListView().getItems().add(new TextNode(readSrtFile.get(i), project.getSpeakerNamesArrayList()));
                            }
                        }
                    }
                }

            }
        }); //Open txt or srt file...
        menuItem_Save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<TextNode> items = textEditorAnchorPane.getListView().getItems();
                ArrayList<Subtitle> subtitleArrayList = project.getSubtitleArrayList();
                for (int i = 0; i < items.size(); i++) {
                    Subtitle subtitle = subtitleArrayList.get(i);
                    TextNode item = items.get(i);
                    subtitle.setId(Integer.parseInt(item.getLabel_SubtitleID().getText()));
                    subtitle.setCompleted(item.getCompletedCheckBox().isSelected());
//                    subtitle.setNote(); // already set at the time when the note is written
                    Object value = item.getSpeakerNames_ComboBox().getValue();
                    if (value != null) {
                        subtitle.setSpeaker(value.toString());
                    }
                    subtitle.setStartHours(Integer.parseInt(item.getLabelStartHours().getText()));
                    subtitle.setStartMilliSec(Integer.parseInt(item.getTextfieldStartMilli().getText()));
                    subtitle.setStartMin(Integer.parseInt(item.getLabelStartMin().getText()));
                    subtitle.setStartSec(Integer.parseInt(item.getTextfieldStartSec().getText()));
                    subtitle.setStopHours(Integer.parseInt(item.getLabelStopHours().getText()));
                    subtitle.setStopMilliSec(Integer.parseInt(item.getLabelStopHours().getText()));
                    subtitle.setStopMin(Integer.parseInt(item.getLabelStopMin().getText()));
                    subtitle.setStopSec(Integer.parseInt(item.getTextfieldStopSec().getText()));
                    subtitle.setText(item.getTextArea().getText());
//                    subtitle.setTotalStartMillis(); // alreadty set at the time when add time stamp button was clicked
//                    subtitle.setTotalStopMillis(); // alreadty set at the time when add time stamp button was clicked

                }
                project.createProjectFile();

            }
        }); // Save
        menuItem_SaveAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String projectPath = project.getProjectPath();
                String projectName = projectPath.substring(projectPath.lastIndexOf("\\") + 1);

                DirectoryChooser dir_chooser = new DirectoryChooser();
                File file = dir_chooser.showDialog(Runner.primaryStage);

                if (file != null) {
                    String newDirPath = file.toPath().toString() + "\\" + projectName;
                    System.out.println("projectPath: " + projectPath + "   newDirPath: " + newDirPath);
                    if (!newDirPath.equalsIgnoreCase(projectPath)) {
                        // copy directory to new dir path

                        AllStaticMethods.copyFile(projectPath, newDirPath);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING,
                                "A directory named '" + projectName + "' already exists at the destination folder !\n"
                                + "Are sure to replace it with project directory?",
                                ButtonType.OK, ButtonType.CANCEL);
                        alert.showAndWait();
                        if (alert.getResult() == ButtonType.OK) {
                            // copy directory to new dir path
                            boolean copyFile = AllStaticMethods.copyFile(projectPath, newDirPath);

                        } else if (alert.getResult() == ButtonType.CANCEL) {
                            event.consume();
                        }
                    }
                }
            }
        }); // Save As
        menuItem_ExportSrtFile.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                FileChooser fileChooser = new FileChooser();
                ExtensionFilter filter = new ExtensionFilter("srt file", "*.srt");
                fileChooser.getExtensionFilters().add(filter);
                String projectPath = project.getProjectPath();
                String projectName = projectPath.substring(projectPath.lastIndexOf("\\") + 1);
                File projectDirectory = new File(projectPath);
                fileChooser.setInitialDirectory(projectDirectory);
                fileChooser.setInitialFileName(projectName);
                File file = fileChooser.showSaveDialog(Runner.primaryStage);
                if (file != null) {
                    boolean writeSrtFile = Subtitle.writeSrtFile(project.getSubtitleArrayList(), (file.toPath().toString()));
                    if (writeSrtFile) {
                        statusBar.setText("Subtitle file saved successfully !");
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        statusBar.setText("Ok !");
                                    }
                                });
                            }
                        }, 3000);
                    } else {
                        new Alert(Alert.AlertType.ERROR, "There was some error in saving the subtitle file !").show();
                    }
                }

            }
        }); //Export srt file...
        menuItem_ExportTextFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                FileChooser fileChooser = new FileChooser();
                ExtensionFilter filter = new ExtensionFilter("Text File", "*.txt");
                fileChooser.getExtensionFilters().add(filter);
                String projectPath = project.getProjectPath();
                String projectName = projectPath.substring(projectPath.lastIndexOf("\\") + 1);
                File projectDirectory = new File(projectPath);
                fileChooser.setInitialDirectory(projectDirectory);
                fileChooser.setInitialFileName(projectName);
                File file = fileChooser.showSaveDialog(Runner.primaryStage);
                if (file != null) {
                    boolean writeSrtFile = Subtitle.writeTxtFile(project.getSubtitleArrayList(), (file.toPath().toString()));
                    if (writeSrtFile) {
                        statusBar.setText("Transcript file saved successfully !");
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        statusBar.setText("Ok !");
                                    }
                                });
                            }
                        }, 3000);
                    } else {
                        new Alert(Alert.AlertType.ERROR, "There was some error in saving the subtitle file !").show();
                    }
                }
            }
        });
        menuItem_Exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    System.exit(0);
                } else if (alert.getResult() == ButtonType.CANCEL) {
                    event.consume();
                }
            }
        });

        menuItem_Cut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    Robot r = new Robot();
                    r.keyPress(KeyEvent.VK_CONTROL);
                    r.keyPress(KeyEvent.VK_X);
                    r.keyRelease(KeyEvent.VK_X);
                    r.keyRelease(KeyEvent.VK_CONTROL);
                } catch (AWTException ex) {
                    System.out.println("MainScreenBorderPane:menuItem_Cut.setOnAction: " + ex);
                }
            }
        });
        menuItem_Copy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    Robot r = new Robot();
                    r.keyPress(KeyEvent.VK_CONTROL);
                    r.keyPress(KeyEvent.VK_C);
                    r.keyRelease(KeyEvent.VK_C);
                    r.keyRelease(KeyEvent.VK_CONTROL);
                } catch (AWTException ex) {
                    System.out.println("MainScreenBorderPane:menuItem_Copy.setOnAction: " + ex);
                }
            }
        });
        menuItem_Paste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    Robot r = new Robot();
                    r.keyPress(KeyEvent.VK_CONTROL);
                    r.keyPress(KeyEvent.VK_V);
                    r.keyRelease(KeyEvent.VK_V);
                    r.keyRelease(KeyEvent.VK_CONTROL);
                } catch (AWTException ex) {
                    System.out.println("MainScreenBorderPane:menuItem_Paste.setOnAction: " + ex);
                }
            }
        });
        menuItem_Find.setOnAction(new EventHandler<ActionEvent>() { //NOT WORKING ;
            @Override
            public void handle(ActionEvent actionEvent) {
//                TextInputDialog inputDialog = new TextInputDialog();
//                inputDialog.setContentText("Find what :");
//                inputDialog.setHeaderText(null);
//                inputDialog.getDialogPane().getButtonTypes().removeAll(ButtonType.OK, ButtonType.CANCEL);
//                ButtonType bt = new ButtonType("Find Next");
//                inputDialog.getDialogPane().getButtonTypes().add(bt);
//                inputDialog.show();
//
//                final Button findNext = (Button) inputDialog.getDialogPane().lookupButton(bt);
//                inputDialog.setOnCloseRequest(e -> {
//                    e.consume();
//                });
//
//                findNext.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        String textTofind = inputDialog.getEditor().getText();
////                        int i = 0;
//                        while (indexTillScanned < project.getSubtitleArrayList().size()) {
//                            Subtitle subtitle = project.getSubtitleArrayList().get(indexTillScanned);
//                            String subtitleText = subtitle.getText();
//                            if (subtitleText.toLowerCase().contains(textTofind.toLowerCase())) {
//                                ListView listView = textEditorAnchorPane.getListView();
//                                listView.scrollTo(indexTillScanned);
//                                TextNode textNode = (TextNode) listView.getItems().get(indexTillScanned);
//                                int indexOf = subtitleText.indexOf(textTofind);
//                                textNode.getTextArea().selectRange(indexOf, (indexOf + textTofind.length()));
//                                break;
//                            }
//                            System.out.println("indexTillScanned: " + indexTillScanned);
//                            indexTillScanned += 1;
//
//                        }
//                    }
//                });

            }
        });

        menuBar.getMenus().addAll(menu_File, menu_Edit, menu_View);
    }

    private void showSpeedPopup(final String message, final Node node) {
//        final Popup popup = createPopup(message);
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        Label label = new Label(message);
        label.setMinSize(45, 25);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-background-color: white;-fx-padding: 0;"
                + "-fx-border-color: black;-fx-border-width: 1;-fx-font-size: 16;");
//        label.getStylesheets().add("/css/styles.css");
        label.getStyleClass().add("popup");
        popup.getContent().add(label);
        Bounds boundsInScreen = node.localToScreen(node.getBoundsInLocal());
        popup.show(node, boundsInScreen.getMinX(), (boundsInScreen.getMinY() - 30));

        PauseTransition wait = new PauseTransition(Duration.seconds(1));
        wait.setOnFinished((e) -> {
            popup.hide();
        });
        wait.play();
    }

    private String millisToHMSM_String(int milliSeconds__) {
        double currentTime = milliSeconds__;
        double currentHours = currentTime / 3600000;
        int currentHoursInt = (int) currentHours;
        double currentMins = ((currentHours - currentHoursInt) * 60);
        int currentMinsInt = (int) currentMins;
        double currentSec = ((currentMins - currentMinsInt) * 60);
        int currentSecInt = (int) currentSec;
        double currentMilli = ((currentSec - currentSecInt) * 1000);
        int currentMilliInt = (int) currentMilli;
        String timeText = String.format("%01d:%02d:%02d,%03d", (int) currentHoursInt, (int) currentMins, currentSecInt, currentMilliInt);
        return timeText;
    }

    private void prepareRangeSliderPane() {
        int currentTime = (int) videoControlsAnchorPane.getVideoSlider().getValue();
        int totalTime = (int) videoControlsAnchorPane.getVideoSlider().getMax();

        int lowerRange = currentTime - 10000;
        int upperRange = currentTime + 10000;
        if (lowerRange >= 0) {
            if (lowerRange < totalTime) {
                rangeSliderAnchorPane.getLowValueLabel().setText(millisToHMSM_String((int) lowerRange));
            } else {
                rangeSliderAnchorPane.getLowValueLabel().setText(millisToHMSM_String(totalTime));
            }
        } else {
            rangeSliderAnchorPane.getLowValueLabel().setText(millisToHMSM_String(0));
        }
        if (upperRange >= 0) {
            if (upperRange < totalTime) {
                rangeSliderAnchorPane.getHighValueLabel().setText(millisToHMSM_String((int) upperRange));
            } else {
                rangeSliderAnchorPane.getHighValueLabel().setText(millisToHMSM_String(totalTime));
            }
        } else {
            rangeSliderAnchorPane.getHighValueLabel().setText(millisToHMSM_String(0));
        }

        Subtitle subtitle = Subtitle.searchSubtitleByTime(currentTime, project.getSubtitleArrayList());
        if (subtitle != null) {
            //  5) change time on range slider time label
            rangeSliderAnchorPane.getTimeRangeLabel().setText(
                    currentSubtitle.getStartHours() + ":"
                    + currentSubtitle.getStartMin() + ":"
                    + currentSubtitle.getStartSec() + ","
                    + currentSubtitle.getStartMilliSec() + " --> "
                    + currentSubtitle.getStopHours() + ":"
                    + currentSubtitle.getStopMin() + ":"
                    + currentSubtitle.getStopSec() + ","
                    + currentSubtitle.getStopMilliSec());

            // 6) change low and high values of range slider
            int lowValue = currentSubtitle.getTotalStartMillis() - currentTime; // will return a negative value
            int highValue = currentSubtitle.getTotalStopMillis() - currentTime; // will return a positive value
            rangeSliderAnchorPane.getRangeSlider().setLowValue(lowValue);
            rangeSliderAnchorPane.getRangeSlider().setHighValue(highValue);

        }
        // 7) change min and max values of range slider
//        double lowerRange = videoControlsAnchorPane.getVideoSlider().getValue() - 10000;
////                    System.out.println("Video Slider Value : " + (lowerRange + 10000));
////                    System.out.println("currentSubtitle.getTotalStartMillis(): " + currentSubtitle.getTotalStartMillis());
//        if (lowerRange >= 0) {
//            rangeSliderAnchorPane.getRangeSlider().setMin(lowerRange);
//        } else {
//            rangeSliderAnchorPane.getRangeSlider().setMin(lowerRange + 10000);
//        }
//        double totalDuration = videoAreaPane.getMediaPlayer().getTotalDuration().toMillis();
//        double upperRange = videoControlsAnchorPane.getVideoSlider().getValue() + 10000;
//        if (upperRange <= totalDuration) {
//            rangeSliderAnchorPane.getRangeSlider().setMax(upperRange);
//        } else {
//            rangeSliderAnchorPane.getRangeSlider().setMax(upperRange - 10000);
//        }
    }

    private class PlayPauseSubtitleEventHandler implements EventHandler<MouseEvent> {

        Subtitle subtitle;
        Button playPauseSubtitleToggleButton;

        public PlayPauseSubtitleEventHandler(Subtitle subtitle, Button playPauseSubtitleButton) {
            this.subtitle = subtitle;
            this.playPauseSubtitleToggleButton = playPauseSubtitleButton;
        }

        @Override
        public void handle(MouseEvent event) {
            MediaPlayer mediaPlayer = videoAreaPane.getMediaPlayer();
            videoControlsAnchorPane.getStopButton().setDisable(false);
            mediaPlayer.seek(Duration.millis(subtitle.getTotalStartMillis()));
            videoAreaPane.getMediaPlayer().play();
            ToggleButton playPauseButton = videoControlsAnchorPane.getPlayPauseButton();
            playPauseButton.setSelected(true);
            playPauseButton.setText("⏸");
            playPauseButton.setTooltip(new Tooltip("Pause"));

            timer = new Timer();
            timer.schedule(new TimerTask() { // tasks to do after subtitle is played

                @Override
                public void run() {
                    playPauseButton.setSelected(false);
                    Platform.runLater(new Runnable() { // runs code on javafx Application thread
                        @Override
                        public void run() {
                            playPauseButton.setText("▶");//▶
                            playPauseButton.setTooltip(new Tooltip("Play"));
                            playPauseSubtitleToggleButton.setText("▶");
                            playPauseSubtitleToggleButton.setTooltip(new Tooltip("Play this subtitle"));
                            mediaPlayer.pause();
//                            mediaPlayer.seek(Duration.millis(subtitle.getTotalStopMillis()));
                        }
                    });
                }
            }, (subtitle.getTotalStopMillis() - subtitle.getTotalStartMillis()));
        }
    }

}
