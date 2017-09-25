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
import javafx.scene.CacheHint;
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

    private final MasterDetailViewFeatures features;

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

    private TranslateTransition movieListTransition;

    private FadeTransition movieListGlaspaneTransition;

    public MasterDetailViewController(final MasterDetailViewModel model, final MasterDetailViewFeatures features) {
        this.model = Objects.requireNonNull(model);
        this.features = Objects.requireNonNull(features);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movieList.setCellFactory(c -> new MovieListCell());
        movieList.setItems(model.getMovies());
        movieList.setCacheHint(CacheHint.SPEED);

        model.selectedMovieProperty().bind(movieList.getSelectionModel().selectedItemProperty());
        model.selectedMovieProperty().addListener((obs, oldVal, newVal) -> updateDetailArea());
        detailView.visibleProperty().bind(model.selectedMovieProperty().isNotNull());
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> updateBackgroundImageBinding());
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> updateBackgroundImageBinding());

        maximizeAppIconView.setOnMouseClicked(e -> ((Stage)rootPane.getScene().getWindow()).setFullScreen(!((Stage)rootPane.getScene().getWindow()).isFullScreen()));
        closeAppIconView.setOnMouseClicked(e -> rootPane.getScene().getWindow().hide());

        moviePosterWrapper.setRotationAxis(new Point3D(0, 1, 0));
        moviePosterWrapper.setCache(true);
        moviePosterWrapper.setCacheShape(true);
        moviePosterWrapper.setCacheHint(CacheHint.SPEED);
        posterImageView.setCache(true);
        posterImageView.setCacheHint(CacheHint.SPEED);

        addFeatureSupport();
        updateBackgroundImageBinding();
        updateDetailArea();
    }

    private void addFeatureSupport() {
        backgroundImageView.visibleProperty().bind(features.movieBackgroundProperty());

        movieListGlasspane.visibleProperty().bind(features.listShadowProperty());
        movieList.cacheProperty().bind(features.listCacheProperty());
        movieList.cacheShapeProperty().bind(features.listCacheProperty());

        features.listAnimationProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal) {
                movieList.setTranslateY(100);
                movieList.setOnMouseEntered(e -> playMovieListTransition(true));
                movieList.setOnMouseExited(e -> playMovieListTransition(false));
            } else {
                movieList.setTranslateY(0);
                movieList.setOnMouseEntered(null);
                movieList.setOnMouseExited(null);
            }
        });

        maximizeAppIconView.visibleProperty().bind(features.customWindowUIProperty());
        closeAppIconView.visibleProperty().bind(features.customWindowUIProperty());

        features.posterTransformProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal) {
                RotateTransition posterRotationTransition = new RotateTransition(Duration.seconds(1), moviePosterWrapper);
                posterRotationTransition.setFromAngle(0);
                posterRotationTransition.setToAngle(-36.0);
                posterRotationTransition.play();
            } else {
                RotateTransition posterRotationTransition = new RotateTransition(Duration.seconds(1), moviePosterWrapper);
                posterRotationTransition.setFromAngle(-36.0);
                posterRotationTransition.setToAngle(0.0);
                posterRotationTransition.play();
            }
        });

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
            backgroundImageView.imageProperty().bind(currentMovie.createBackgroundImageBinding());
        }
    }
}
