package com.extreme.view;

import com.extreme.data.Movie;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MasterDetailViewModel {

    private final ObservableList<Movie> movies = FXCollections.observableArrayList();

    private final ObjectProperty<Movie> selectedMovie = new SimpleObjectProperty<>();

    public ObservableList<Movie> getMovies() {
        return movies;
    }

    public Movie getSelectedMovie() {
        return selectedMovie.get();
    }

    public ObjectProperty<Movie> selectedMovieProperty() {
        return selectedMovie;
    }

    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie.set(selectedMovie);
    }
}
