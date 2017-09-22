package com.extreme.ui;

import com.extreme.data.Movie;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class MovieListCell extends ListCell<Movie> {

    private final ImageView coverView = new ImageView();

    public MovieListCell() {
        getStyleClass().add("movie-list-cell");
        itemProperty().addListener((obs, oldVal, newVal) -> updateLayout());
        coverView.setFitHeight(132);
        coverView.setPreserveRatio(true);

        final StackPane wrapper = new StackPane(coverView);
        wrapper.getStyleClass().add("movie-poster-wrapper");

        setGraphic(wrapper);
    }

    private void updateLayout() {
        coverView.imageProperty().unbind();

        final Movie movie = getItem();
        if(movie == null) {
            coverView.setImage(null);
        } else {
            coverView.imageProperty().bind(movie.createPosterImageBinding());
        }
    }
}
