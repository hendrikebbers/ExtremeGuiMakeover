package com.extreme.ui;

import com.extreme.MovieApp;
import com.extreme.data.Movie;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;

/**
 * Created by lemmi on 18.09.17.
 */
public class MovieCell extends ListCell<Movie> {

    private MovieView movieView;

    private Label titleLabel;
    private Label directorLabel;
    private Label genreLabel;
    private Label trailerLabel;
    private ImageView directorImage;

    public MovieCell(MovieView movieView) {
        this.movieView = movieView;

        GridPane gridPane = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        ColumnConstraints col2 = new ColumnConstraints();
        ColumnConstraints col3 = new ColumnConstraints();
        ColumnConstraints col4 = new ColumnConstraints();
        ColumnConstraints col5 = new ColumnConstraints();

        col4.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().setAll(col1, col2, col3, col4, col5);
        gridPane.setHgap(10);

        // director image
        directorImage = new ImageView();
        directorImage.setFitWidth(60);
        directorImage.setFitHeight(60);
        directorImage.setEffect(new InnerShadow());
        gridPane.add(directorImage, 0, 0);
        GridPane.setRowSpan(directorImage, 2);
        GridPane.setValignment(directorImage, VPos.TOP);

        // title and year
        titleLabel = new Label();
        titleLabel.getStyleClass().add("title-label");
        gridPane.add(titleLabel, 1, 0);
        GridPane.setColumnSpan(titleLabel, 2);
        GridPane.setValignment(titleLabel, VPos.TOP);

        // director label
        directorLabel = new Label();
        directorLabel.getStyleClass().add("director-label");
        gridPane.add(directorLabel, 1, 1);
        GridPane.setValignment(directorLabel, VPos.TOP);

        // genre label
        genreLabel = new Label();
        genreLabel.getStyleClass().add("genre-label");
        gridPane.add(genreLabel, 4, 0);

        // trailer label
        trailerLabel = FontAwesomeIconFactory.get().createIconLabel(FontAwesomeIcon.FILM, "", "14px", "14px", ContentDisplay.GRAPHIC_ONLY);
        trailerLabel.getStyleClass().add("trailer-label");
        gridPane.add(trailerLabel, 3, 0);
        GridPane.setHalignment(trailerLabel, HPos.LEFT);
        GridPane.setValignment(trailerLabel, VPos.TOP);

        trailerLabel.setOnMouseClicked(evt -> {
            try {
                Movie movie = getItem();
                movieView.setSelectedTrailer(MovieApp.class.getResource("/trailers/" + movie.getTrailer()).toExternalForm());
            } catch (NullPointerException e) {
                movieView.setSelectedTrailer(MovieApp.class.getResource("/trailers/TrailerMissing.mp4").toExternalForm());
            }
        });

        setGraphic(gridPane);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        movieView.useClippingProperty().addListener(it -> updateClipping());
        updateClipping();
    }

    private void updateClipping() {
        if (movieView.isUseClipping()) {
            Circle circle = new Circle();
            double size = 30;
            circle.setRadius(size);
            circle.setCenterX(size);
            circle.setCenterY(size);
            directorImage.setClip(circle);
        } else {
            directorImage.setClip(null);
        }
    }

    @Override
    protected void updateItem(Movie movie, boolean empty) {
        super.updateItem(movie, empty);

        if (movie != null) {
            directorImage.setImage(new Image("/directors/" + movie.getDirectorImage()));
            titleLabel.setText(movie.getTitle() + " (" + movie.getYear() + ")");
            directorLabel.setText(movie.getDirector());
            genreLabel.setVisible(true);
            genreLabel.setText(movie.getGenre().toString());
            if (movie.getTrailer() != null) {
                trailerLabel.setVisible(true);
            } else {
                trailerLabel.setVisible(false);
            }
        } else {
            directorImage.setImage(null);
            titleLabel.setText("");
            directorLabel.setText("");
            genreLabel.setText("");
            genreLabel.setVisible(false);
            trailerLabel.setVisible(false);
        }
    }
}
