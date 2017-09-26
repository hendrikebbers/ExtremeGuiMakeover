package com.extreme.feature;

import com.extreme.Util;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;

/**
 * Created by lemmi on 14.09.17.
 */
public class FeaturesPane extends VBox {

    private double mouseX;

    private double mouseY;

    private final ObservableList<Feature> features = FXCollections.observableArrayList();

    public FeaturesPane() {
        super();

        getStylesheets().add(FeaturesPane.class.getResource("/options-pane.css").toExternalForm());

        getStyleClass().add("options-pane");

        Button all = new Button("All");
        all.setOnAction(evt -> features.forEach(f -> f.setActive(true)));
        all.setMaxWidth(Double.MAX_VALUE);
        all.setId("all-button");
        Util.installWindowDragListener(all);

        Button reset = new Button("Reset");
        reset.setOnAction(evt -> features.forEach(f -> f.setActive(false)));
        reset.setMaxWidth(Double.MAX_VALUE);
        reset.setId("reset-button");
        Util.installWindowDragListener(reset);

        HBox top = new HBox(all, reset);
        top.getStyleClass().add("toolbar");
        top.setFillHeight(true);
        HBox.setHgrow(all, Priority.ALWAYS);
        HBox.setHgrow(reset, Priority.ALWAYS);

        getChildren().add(top);
    }

    public void addFeature(final Feature feature) {
        features.add(feature);

        ToggleSwitch toggleSwitch = new ToggleSwitch();
        toggleSwitch.textProperty().bindBidirectional(feature.nameProperty());
        toggleSwitch.selectedProperty().bindBidirectional(feature.activeProperty());
        Util.installWindowDragListener(toggleSwitch);
        toggleSwitch.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(toggleSwitch, Priority.ALWAYS);

        ToggleButton showSlidesButton = FontAwesomeIconFactory.get().createIconToggleButton(FontAwesomeIcon.DOWNLOAD);
        Util.installWindowDragListener(showSlidesButton);

        HBox featureBox = new HBox(10, toggleSwitch, showSlidesButton);

        getChildren().add(featureBox);
    }
}
