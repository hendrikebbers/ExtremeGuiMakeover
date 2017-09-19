package com.extreme.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "database")
public class Database {

    @XmlElement(name = "movie")
    public final ObservableList<Movie> movies = FXCollections.observableArrayList();

    public final ObservableList<Movie> getMovies() {
        return movies;
    }
}
