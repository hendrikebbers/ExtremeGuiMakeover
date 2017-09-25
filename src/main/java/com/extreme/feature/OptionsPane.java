package com.extreme.feature;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import org.controlsfx.control.ToggleSwitch;

/**
 * Created by lemmi on 14.09.17.
 */
public class OptionsPane extends VBox {

    private double mouseX;

    private double mouseY;

    private final ObservableList<Feature> features = FXCollections.observableArrayList();

    public OptionsPane() {
        super();

        getStylesheets().add(OptionsPane.class.getResource("/options-pane.css").toExternalForm());

        getStyleClass().add("options-pane");

        Button all = new Button("All");
        all.setOnAction(evt -> features.forEach(f -> f.setActive(true)));
        all.setMaxWidth(Double.MAX_VALUE);
        all.setId("all-button");
        installDragListener(all);

        Button reset = new Button("Reset");
        reset.setOnAction(evt -> features.forEach(f -> f.setActive(false)));
        reset.setMaxWidth(Double.MAX_VALUE);
        reset.setId("reset-button");
        installDragListener(reset);

        HBox top = new HBox(all, reset);
        top.getStyleClass().add("toolbar");
        top.setFillHeight(true);
        HBox.setHgrow(all, Priority.ALWAYS);
        HBox.setHgrow(reset, Priority.ALWAYS);

        getChildren().add(top);
    }

    private void installDragListener(Node node) {
        node.setOnMousePressed(evt -> {
            mouseX = evt.getScreenX();
            mouseY = evt.getScreenY();
        });

        node.setOnMouseDragged(evt -> {
            double deltaX = evt.getScreenX() - mouseX;
            double deltaY = evt.getScreenY() - mouseY;

            Window window = getScene().getWindow();

            window.setX(getScene().getWindow().getX() + deltaX);
            window.setY(getScene().getWindow().getY() + deltaY);

            mouseX = evt.getScreenX();
            mouseY = evt.getScreenY();
        });
    }

    public void addFeature(final Feature feature) {
        features.add(feature);

        HBox featureBox = new HBox();

        ToggleSwitch toggleButton = new ToggleSwitch();
        toggleButton.textProperty().bindBidirectional(feature.nameProperty());
        toggleButton.selectedProperty().bindBidirectional(feature.activeProperty());
        installDragListener(toggleButton);
        toggleButton.setMaxWidth(Double.MAX_VALUE);

        ToggleButton slideButton = FontAwesomeIconFactory.get().createIconToggleButton(FontAwesomeIcon.DOWNLOAD);

        featureBox.getChildren().addAll(toggleButton, slideButton);



        getChildren().add(featureBox);
    }
}
