package Naqqaal;

import java.util.ArrayList;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 *
 * @author Lenovo 520
 */
public class TextNode extends AnchorPane {

    private Subtitle subtitle;
    private TextArea textArea;
    private Label label_SubtitleID, labelStartHours, labelStartMin, labelStopHours, labelStopMin;
    private TextField textfieldStartSec, textfieldStartMilli, textfieldStopSec, textfieldStopMilli;
    private Button buttonNotes, deleteSubtitleButton, previewButton, playSubtitleButton, notesButton;
    private CheckBox completedCheckBox;
    private ComboBox speakerNames_ComboBox;
    private FlowPane timeFlowPane;
    private Popup notesPopup = null;
    private ArrayList<String> speakerNamesArrayList;

    public TextNode(Subtitle subtitle1, ArrayList<String> speakerNamesArrayList) {
        this.subtitle = subtitle1;
        this.speakerNamesArrayList = speakerNamesArrayList;

        timeFlowPane = new FlowPane();
        label_SubtitleID = new Label(this.subtitle.getId() + "");
        labelStartHours = new Label(this.subtitle.getStartHours() + "");
        labelStartMin = new Label(this.subtitle.getStartMin() + "");
        textfieldStartSec = new TextField(this.subtitle.getStartSec() + "");
        textfieldStartMilli = new TextField(this.subtitle.getStartMilliSec() + "");
        labelStopHours = new Label(this.subtitle.getStopHours() + "");
        labelStopMin = new Label(this.subtitle.getStopMin() + "");
        textfieldStopSec = new TextField(this.subtitle.getStopSec() + "");
        textfieldStopMilli = new TextField(this.subtitle.getStopMilliSec() + "");
        textArea = new TextArea(this.subtitle.getText());
        buttonNotes = new Button();
        playSubtitleButton = new Button("▶");//▶
        deleteSubtitleButton = new Button();//⏸
        notesButton = new Button("📝");
        completedCheckBox = new CheckBox();
        previewButton = new Button("👁");
        speakerNames_ComboBox = new ComboBox();

        label_SubtitleID.setTooltip(new Tooltip("Subtitle ID"));
        label_SubtitleID.setStyle("-fx-alignment:center; -fx-border-color:grey; -fx-background-color:white;");
        label_SubtitleID.setTextAlignment(TextAlignment.CENTER);
        label_SubtitleID.setWrapText(true);
        label_SubtitleID.setPrefWidth(40);
        label_SubtitleID.setPrefHeight(30);
        FlowPane.setMargin(label_SubtitleID, new Insets(5, 5, 5, 0));

        labelStartHours.setTooltip(new Tooltip("Start Hours"));
        labelStartHours.setPrefWidth(30);
        labelStartHours.setPrefHeight(30);
        labelStartHours.setStyle("-fx-alignment:center; -fx-border-color:grey; -fx-background-color:white;");
        FlowPane.setMargin(labelStartHours, new Insets(5, 5, 5, 5));

        labelStartMin.setTooltip(new Tooltip("Start Minutes"));
        labelStartMin.setPrefWidth(30);
        labelStartMin.setPrefHeight(30);
        labelStartMin.setStyle("-fx-alignment:center; -fx-border-color:grey; -fx-background-color:white;");
        FlowPane.setMargin(labelStartMin, new Insets(5, 5, 5, 5));

        textfieldStartSec.setTooltip(new Tooltip("Start Seconds"));
        FlowPane.setMargin(textfieldStartSec, new Insets(5, 5, 5, 5));
        textfieldStartSec.setMaxWidth(35);
        textfieldStartSec.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    textfieldStartSec.setText(newValue.replaceAll("[^\\d]", ""));
                }
                try {
                    int newValInt = Integer.parseInt(newValue);
                    if (newValInt > 59) {
                        textfieldStartSec.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                }

            }
        });

        textfieldStartMilli.setTooltip(new Tooltip("Start Milliseconds"));
        FlowPane.setMargin(textfieldStartMilli, new Insets(5, 5, 5, 5));
        textfieldStartMilli.setMaxWidth(45);
        textfieldStartMilli.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    textfieldStartMilli.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        labelStopHours.setTooltip(new Tooltip("Stop hours"));
        labelStopHours.setPrefWidth(30);
        labelStopHours.setPrefHeight(30);
        labelStopHours.setStyle("-fx-alignment:center; -fx-border-color:grey; -fx-background-color:white;");
        FlowPane.setMargin(labelStopHours, new Insets(5, 5, 5, 5));

        labelStopMin.setTooltip(new Tooltip("Stop Minutes"));
        labelStopMin.setPrefWidth(30);
        labelStopMin.setPrefHeight(30);
        labelStopMin.setStyle("-fx-alignment:center; -fx-border-color:grey; -fx-background-color:white;");
        FlowPane.setMargin(labelStopMin, new Insets(5, 5, 5, 5));

        textfieldStopSec.setTooltip(new Tooltip("Stop Seconds"));
        FlowPane.setMargin(textfieldStopSec, new Insets(5, 5, 5, 5));
        textfieldStopSec.setMaxWidth(35);
        textfieldStopSec.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    textfieldStopSec.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        textfieldStopMilli.setTooltip(new Tooltip("Stop Milliseconds"));
        FlowPane.setMargin(textfieldStopMilli, new Insets(5, 5, 5, 5));
        textfieldStopMilli.setMaxWidth(45);
        textfieldStopMilli.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    textfieldStopMilli.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Label labelColon = new Label(" : ");
        labelColon.setStyle("-fx-font-weight: bold;");
        Label labelColon1 = new Label(" : ");
        labelColon1.setStyle("-fx-font-weight: bold;");
        Label labelColon2 = new Label(" : ");
        labelColon2.setStyle("-fx-font-weight: bold;");
        Label labelColon3 = new Label(" : ");
        labelColon3.setStyle("-fx-font-weight: bold;");
        Label labelComma = new Label(" , ");
        labelComma.setStyle("-fx-font-weight: bold;");
        Label labelComma1 = new Label(",");
        labelComma1.setStyle("-fx-font-weight: bold;");
        Label label_Arrow = new Label(" --> ");
        label_Arrow.setStyle("-fx-font-weight: bold;");

        textArea.setFont(new Font("consolas", 20));
        textArea.setMaxHeight(textArea.getFont().getSize() * 1.7);
        int nol = this.subtitle.getText().split("\n").length; // no of lines 
        textArea.setMaxHeight(textArea.getMaxHeight() + (textArea.getFont().getSize() * 1.4 * (nol - 1)));
        setMaxHeight(50 + textArea.getMaxHeight());
        textArea.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
//                System.out.println(event.getCode().toString());
                if (event.getCode().toString().equals("ENTER")) {
                    textArea.setMaxHeight(textArea.getMaxHeight() + (textArea.getFont().getSize() * 1.4));
                    setMaxHeight(50 + textArea.getMaxHeight());

                } else if (event.getCode().toString().equals("BACK_SPACE")) {
                    String text = textArea.getText();
                    int textLength = text.length();
                    if (textLength != 0 && (int) text.charAt(textLength - 1) == 10) {
                        textArea.setMaxHeight(textArea.getMaxHeight() - (textArea.getFont().getSize() * 1.4));
                    }
                } else if (event.isControlDown()) { // new Subtitle
                    if (event.getCode().toString().equals("N")) {

                        System.out.println("CTRL+N clicked dd!");
                    }
                }
            }
        });
        AnchorPane.setLeftAnchor(textArea, 133.25);
        AnchorPane.setRightAnchor(textArea, 10.0);
        AnchorPane.setTopAnchor(textArea, 50.0);
        textArea.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MainScreenBorderPane.currentSubtitle = subtitle;
//                System.out.println("Current Subtitle: "+ MainScreenBorderPane.currentSubtitle.getId());
            }
        });

        buttonNotes.setTooltip(new Tooltip(this.subtitle.getNote()));
        buttonNotes.setPrefSize(37, 37);

        playSubtitleButton.setTooltip(new Tooltip("Play this subtitle."));
        playSubtitleButton.setMinSize(21.25, 21.25);
        playSubtitleButton.setMaxSize(21.25, 21.25);
        playSubtitleButton.setPadding(new Insets(0));
        AnchorPane.setLeftAnchor(playSubtitleButton, 5.0);
        AnchorPane.setTopAnchor(playSubtitleButton, 82.0);

        deleteSubtitleButton.setPadding(new Insets(0));
        deleteSubtitleButton.setText("🗑");
        deleteSubtitleButton.setTooltip(new Tooltip("Delete Subtitle!"));
        deleteSubtitleButton.setMinSize(21.25, 21.25);
        deleteSubtitleButton.setMaxSize(21.25, 21.25);
        AnchorPane.setLeftAnchor(deleteSubtitleButton, 31.25);
        AnchorPane.setTopAnchor(deleteSubtitleButton, 82.0);

        notesButton.setMinSize(21.25, 21.25);
        notesButton.setMaxSize(21.25, 21.25);
        notesButton.setPadding(Insets.EMPTY);
        AnchorPane.setLeftAnchor(notesButton, 57.5);
        AnchorPane.setTopAnchor(notesButton, 82.0);
        notesButton.setTooltip(new Tooltip("Notes"));

        notesButton.setOnMousePressed(new EventHandler() {
            @Override
            public void handle(Event event) {
                showNotesPopup(subtitle.getNote(), notesButton);
            }
        });

        completedCheckBox.setTooltip(new Tooltip("Completed: " + this.subtitle.isCompleted()));
        completedCheckBox.setMaxSize(21.25, 21.25);
        completedCheckBox.setMinSize(21.25, 21.25);
        AnchorPane.setLeftAnchor(completedCheckBox, 82.75);
        AnchorPane.setTopAnchor(completedCheckBox, 82.0);
        completedCheckBox.setSelected(subtitle.isCompleted());

        previewButton.setMaxSize(21.25, 21.25);
        previewButton.setMinSize(21.25, 21.25);
        previewButton.setPadding(new Insets(0));
        previewButton.setTooltip(new Tooltip("Preview Subtitle"));
        AnchorPane.setLeftAnchor(previewButton, 108.0);
        AnchorPane.setTopAnchor(previewButton, 82.0);
        previewButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Popup popup = showPreviewPopup(textArea.getText(), previewButton);
                previewButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        popup.hide();
                    }
                });
            }
        });

//        String speakerNames[] = {"Abdullah", "Bassam", "Moiz", "Bilal", "Ali"};
        speakerNames_ComboBox.setItems(FXCollections.observableArrayList(speakerNamesArrayList));
        speakerNames_ComboBox.setTooltip(new Tooltip("Speaker Name"));
        speakerNames_ComboBox.setMaxSize(124, 20);
        speakerNames_ComboBox.setMinSize(124, 20);
        AnchorPane.setLeftAnchor(speakerNames_ComboBox, 5.0);
        AnchorPane.setTopAnchor(speakerNames_ComboBox, 52.0);
        speakerNames_ComboBox.getEditor().setPadding(new Insets(3));
        speakerNames_ComboBox.setStyle("-fx-font-size: 12px;");
        speakerNames_ComboBox.setEditable(true);
        speakerNames_ComboBox.getSelectionModel().select(subtitle.getSpeaker());

        AnchorPane.setTopAnchor(timeFlowPane, 0.0);
        AnchorPane.setLeftAnchor(timeFlowPane, 5.0);
        timeFlowPane.setMinWidth(520.0);

        timeFlowPane.getChildren().addAll(label_SubtitleID, labelStartHours, labelColon,
                labelStartMin, labelColon1, textfieldStartSec, labelComma, textfieldStartMilli,
                label_Arrow, labelStopHours, labelColon2,
                labelStopMin, labelColon3, textfieldStopSec, labelComma1, textfieldStopMilli);

        getChildren().addAll(timeFlowPane, speakerNames_ComboBox, playSubtitleButton,
                deleteSubtitleButton, completedCheckBox, notesButton, previewButton, textArea);
    }

    private void showNotesPopup(final String message, final Node node) {
//        final Popup popup = createPopup(message);
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
//        popup.setHideOnEscape(true);
        TextArea notesTextArea = new TextArea(message);
        notesTextArea.setWrapText(true);
        notesTextArea.setMaxSize(200, 100);
        notesTextArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ESCAPE) {
                    popup.hide();
                }
            }
        });
        notesTextArea.getStyleClass().add("popup");
        popup.getContent().add(notesTextArea);
        Bounds boundsInScreen = node.localToScreen(node.getBoundsInLocal());
        popup.show(node, boundsInScreen.getMinX(), boundsInScreen.getMaxY());
        popup.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                subtitle.setNote(notesTextArea.getText());
                label_SubtitleID.requestFocus();
            }
        });
    }

    private Popup showPreviewPopup(final String message, final Node node) {
//        final Popup popup = createPopup(message);
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        HTMLEditor htmlEditor = new HTMLEditor();

        htmlEditor.setHtmlText(message.replaceAll("\n", "<br>"));
        // hiding the toolbars
        Node[] nodes = htmlEditor.lookupAll(".tool-bar").toArray(new Node[0]);
//        System.out.println("nodes.length: " + nodes.length);
        nodes[0].setVisible(false);
        nodes[0].setManaged(false);
        nodes[1].setVisible(false);
        nodes[1].setManaged(false);

        htmlEditor.setMaxSize(500, 100);

        htmlEditor.getStyleClass().add("popup");
        popup.getContent().add(htmlEditor);
        Bounds boundsInScreen = node.localToScreen(node.getBoundsInLocal());
        popup.show(node, boundsInScreen.getMinX(), boundsInScreen.getMaxY());
        return popup;
    }

    public Subtitle getSubtitle() {
        return subtitle;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public Label getLabel_SubtitleID() {
        return label_SubtitleID;
    }

    public Label getLabelStartHours() {
        return labelStartHours;
    }

    public Label getLabelStartMin() {
        return labelStartMin;
    }

    public Label getLabelStopHours() {
        return labelStopHours;
    }

    public Label getLabelStopMin() {
        return labelStopMin;
    }

    public TextField getTextfieldStartSec() {
        return textfieldStartSec;
    }

    public TextField getTextfieldStartMilli() {
        return textfieldStartMilli;
    }

    public TextField getTextfieldStopSec() {
        return textfieldStopSec;
    }

    public TextField getTextfieldStopMilli() {
        return textfieldStopMilli;
    }

//    public Button getButtonNotes() {
//        return buttonNotes;
//    }
    public Button getPlaySubtitleButton() {
        return playSubtitleButton;
    }

    public Button getDeleteSubtitleButton() {
        return deleteSubtitleButton;
    }

    public Button getNotesButton() {
        return notesButton;
    }

    public CheckBox getCompletedCheckBox() {
        return completedCheckBox;
    }

    public ComboBox getSpeakerNames_ComboBox() {
        return speakerNames_ComboBox;
    }

    public FlowPane getTimeFlowPane() {
        return timeFlowPane;
    }

}
