package ru.nsu.projects.malaeva;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    public Pane pane;
    public VBox vbox;

//    private VBox getTextFields() {
//
//    }

    public MainController() {

    }

    @FXML
    public void addNextFields() {
        vbox.getChildren().add(new TextField());
    }
}