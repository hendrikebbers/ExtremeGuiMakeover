package com.extreme.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType
public class SlidesEntry {

    private String title = "Untitled";
    private String nodeId = "#id";
    private String featureId = "css";

    @XmlAttribute
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlAttribute
    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @XmlAttribute
    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    @XmlElement(name = "slide")
    public final ObservableList<Slide> slides = FXCollections.observableArrayList();

    public final ObservableList<Slide> getSlides() {
        return slides;
    }
}
