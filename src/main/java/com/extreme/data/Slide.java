package com.extreme.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Slide {

    public Slide() {

    }

    // name
    private final StringProperty name = new SimpleStringProperty(this, "name", "hi");

    public final StringProperty nameProperty() {
        return name;
    }

    public final void setName(String name) {
        this.name.set(name);
    }

    public final String getName() {
        return name.get();
    }
}
