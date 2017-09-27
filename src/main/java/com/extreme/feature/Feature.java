package com.extreme.feature;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.controlsfx.control.PopOver;

public class Feature {

    private final StringProperty name = new SimpleStringProperty();

    private final StringProperty id = new SimpleStringProperty();

    private final StringProperty description = new SimpleStringProperty();

    private final BooleanProperty active = new SimpleBooleanProperty();

    private final BooleanProperty showSlides = new SimpleBooleanProperty();

    private final ObjectProperty<Node> slideAncor = new SimpleObjectProperty<>();

    private final ObjectProperty<PopOver.ArrowLocation> arrowLocation = new SimpleObjectProperty();

    private final ObservableList<String> slideUrls = FXCollections.observableArrayList();

    public Feature(final String name, final String id, final Property<Boolean> featureState) {
        active.bindBidirectional(featureState);
        setName(name);
        setId(id);
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        idProperty().set(id);
    }

    public String getId() {
        return id.get();
    }

    public ObservableList<String> getSlideUrls() {
        return slideUrls;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public boolean isShowSlides() {
        return showSlides.get();
    }

    public BooleanProperty showSlidesProperty() {
        return showSlides;
    }

    public void setShowSlides(boolean showSlides) {
        this.showSlides.set(showSlides);
    }

    public Node getSlideAncor() {
        return slideAncor.get();
    }

    public ObjectProperty<Node> slideAncorProperty() {
        return slideAncor;
    }

    public void setSlideAncor(Node slideAncor) {
        this.slideAncor.set(slideAncor);
    }

    public PopOver.ArrowLocation getArrowLocation() {
        return arrowLocation.get();
    }

    public ObjectProperty<PopOver.ArrowLocation> arrowLocationProperty() {
        return arrowLocation;
    }

    public void setArrowLocation(PopOver.ArrowLocation arrowLocation) {
        this.arrowLocation.set(arrowLocation);
    }
}
