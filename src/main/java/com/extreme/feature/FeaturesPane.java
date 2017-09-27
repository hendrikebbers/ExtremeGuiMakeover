package com.extreme.feature;

import com.extreme.MovieApp;
import com.extreme.SlidesViewer;
import com.extreme.Util;
import com.extreme.data.SlidesEntry;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;

import java.util.Optional;

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
        toggleSwitch.setAlignment(Pos.CENTER_LEFT);
        toggleSwitch.textProperty().bindBidirectional(feature.nameProperty());
        toggleSwitch.selectedProperty().bindBidirectional(feature.activeProperty());
        Util.installWindowDragListener(toggleSwitch);
        toggleSwitch.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        HBox.setHgrow(toggleSwitch, Priority.ALWAYS);

        Button showSlidesButton = FontAwesomeIconFactory.get().createIconButton(FontAwesomeIcon.FILM);
        showSlidesButton.getStyleClass().add("show-slides-button");
        showSlidesButton.setVisible(false);
        HBox.setHgrow(showSlidesButton, Priority.NEVER);
        Util.installWindowDragListener(showSlidesButton);

        HBox featureBox = new HBox(10, toggleSwitch, showSlidesButton);
        featureBox.setAlignment(Pos.TOP_CENTER);

        getChildren().add(featureBox);

        Optional<SlidesEntry> slidesEntry = MovieApp.slidesDatabase.getSlideEntry(feature.getId());
        if (slidesEntry.isPresent()) {
            SlidesEntry entry = slidesEntry.get();
            if (!entry.getSlides().isEmpty()) {
                showSlidesButton.setVisible(true);
                showSlidesButton.setOnAction(evt -> SlidesViewer.showSlides(entry));

                toggleSwitch.selectedProperty().addListener(it -> {
                    if (toggleSwitch.isSelected()) {
                        SlidesViewer.showSlides(entry);
                    }
                });
            }
        }
    }
}
