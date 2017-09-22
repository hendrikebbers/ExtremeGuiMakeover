package com.extreme.view;

import com.extreme.data.Movie;
import com.extreme.ui.MovieListCell;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MasterDetailViewController implements Initializable {

    private final MasterDetailViewModel model;

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private ImageView posterImageView;

    @FXML
    private Text titleText;

    @FXML
    private Text descriptionText;

    @FXML
    private Button watchTrailerButton;

    @FXML
    private ListView<Movie> movieList;

    @FXML
    private StackPane detailView;

    @FXML
    private StackPane moviePosterWrapper;

    @FXML
    private StackPane rootPane;

    @FXML
    private StackPane movieListGlasspane;

    @FXML
    private FontAwesomeIconView maximizeAppIconView;

    @FXML
    private FontAwesomeIconView closeAppIconView;

    private RotateTransition posterRotationTransition;

    private TranslateTransition movieListTransition;

    private FadeTransition movieListGlaspaneTransition;

    public MasterDetailViewController(final MasterDetailViewModel model) {
        this.model = Objects.requireNonNull(model);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movieList.setCellFactory(c -> new MovieListCell());
        movieList.setItems(model.getMovies());
        model.selectedMovieProperty().bind(movieList.getSelectionModel().selectedItemProperty());
        model.selectedMovieProperty().addListener((obs, oldVal, newVal) -> updateDetailArea());
        detailView.visibleProperty().bind(model.selectedMovieProperty().isNotNull());
        moviePosterWrapper.setRotationAxis(new Point3D(0.0, 1.0, 0.0));
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> updateBackgroundImageBinding());
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> updateBackgroundImageBinding());

        movieList.setOnMouseEntered(e -> playMovieListTransition(true));
        movieList.setOnMouseExited(e -> playMovieListTransition(false));
        movieList.setTranslateY(100);

        movieListGlasspane.setMouseTransparent(true);

        maximizeAppIconView.setOnMouseClicked(e -> ((Stage)rootPane.getScene().getWindow()).setFullScreen(true));
        closeAppIconView.setOnMouseClicked(e -> rootPane.getScene().getWindow().hide());

        updateBackgroundImageBinding();
        updateDetailArea();
    }

    private void playMovieListTransition(boolean show) {
        if(movieListTransition != null) {
            movieListTransition.stop();
        }
        movieListTransition = new TranslateTransition(Duration.millis(360), movieList);
        movieListTransition.setFromY(movieList.getTranslateY());
        if(show) {
            movieListTransition.setToY(0.0);
        } else {
            movieListTransition.setToY(100);
        }
        movieListTransition.play();

        if(movieListGlaspaneTransition != null) {
            movieListGlaspaneTransition.stop();
        }
        movieListGlaspaneTransition = new FadeTransition(Duration.millis(320), movieListGlasspane);
        movieListGlaspaneTransition.setFromValue(movieListGlasspane.getOpacity());
        if(show) {
            movieListGlaspaneTransition.setToValue(0.0);
        } else {
            movieListGlaspaneTransition.setToValue(1.0);
        }
        movieListGlaspaneTransition.play();
    }

    private void updateBackgroundImageBinding() {
        //TODO: not perfect. Works just for our images...
        backgroundImageView.setFitHeight(-1);
        backgroundImageView.setFitWidth(-1);
        if(rootPane.getHeight() > rootPane.getWidth()) {
            backgroundImageView.setFitHeight(rootPane.getHeight());
        } else {
            backgroundImageView.setFitWidth(rootPane.getWidth());
        }
    }

    private void updateDetailArea() {
        backgroundImageView.imageProperty().unbind();
        posterImageView.imageProperty().unbind();
        titleText.textProperty().unbind();
        descriptionText.textProperty().unbind();

        Movie currentMovie = model.getSelectedMovie();
        if(currentMovie != null) {
            titleText.textProperty().bind(currentMovie.titleProperty());
            descriptionText.textProperty().bind(currentMovie.synopsisProperty());
            posterImageView.imageProperty().bind(currentMovie.createPosterImageBinding());

            if(posterRotationTransition != null) {
                posterRotationTransition.stop();
            }
            moviePosterWrapper.setRotate(-32.0);
            posterRotationTransition = new RotateTransition(Duration.seconds(1.4), moviePosterWrapper);
            posterRotationTransition.setDelay(Duration.seconds(1.0));
            posterRotationTransition.setFromAngle(-32.0);
            posterRotationTransition.setToAngle(0.0);
            posterRotationTransition.play();
        }
    }
}
