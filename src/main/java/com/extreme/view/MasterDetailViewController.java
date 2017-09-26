package com.extreme.view;

import com.extreme.Util;
import com.extreme.data.Movie;
import com.extreme.ui.MovieListCell;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point3D;
import javafx.scene.CacheHint;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
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
        backgroundImageView.setMouseTransparent(true);

        movieList.setCellFactory(c -> new MovieListCell());
        movieList.setItems(model.getMovies());
        movieList.setCacheHint(CacheHint.SPEED);

        model.selectedMovieProperty().bind(movieList.getSelectionModel().selectedItemProperty());
        model.selectedMovieProperty().addListener((obs, oldVal, newVal) -> updateDetailArea());
        detailView.visibleProperty().bind(model.selectedMovieProperty().isNotNull());

        maximizeAppIconView.setOnMouseClicked(e -> ((Stage) rootPane.getScene().getWindow()).setFullScreen(!((Stage) rootPane.getScene().getWindow()).isFullScreen()));
        closeAppIconView.setOnMouseClicked(e -> rootPane.getScene().getWindow().hide());

        moviePosterWrapper.setRotationAxis(new Point3D(0, 1, 0));
        moviePosterWrapper.setCache(true);
        moviePosterWrapper.setCacheShape(true);
        moviePosterWrapper.setCacheHint(CacheHint.SPEED);
        posterImageView.setCache(true);
        posterImageView.setCacheHint(CacheHint.SPEED);

        Util.installWindowDragListener(rootPane);

        final ChangeListener backgroundImageResizeListener = (obs, oldVal, newVal) -> {
            final Image image = backgroundImageView.getImage();
            if (backgroundImageView.getImage() != null) {
                Dimension2D backgroundDimension = shouldFitIn(image.getWidth(), image.getHeight(), rootPane.getWidth(), rootPane.getHeight());
                backgroundImageView.setFitWidth(backgroundDimension.getWidth());
                backgroundImageView.setFitHeight(backgroundDimension.getHeight());
            }
        };
        rootPane.widthProperty().addListener(backgroundImageResizeListener);
        rootPane.heightProperty().addListener(backgroundImageResizeListener);
        backgroundImageView.imageProperty().addListener(backgroundImageResizeListener);

        addFeatureSupport();

        updateDetailArea();

    }

    private void addFeatureSupport() {
        backgroundImageView.visibleProperty().bind(features.movieBackgroundProperty());

        movieListGlasspane.visibleProperty().bind(features.listShadowProperty());
        movieList.cacheProperty().bind(features.listCacheProperty());
        movieList.cacheShapeProperty().bind(features.listCacheProperty());

        features.listAnimationProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
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
            if (newVal) {
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
        if (movieListTransition != null) {
            movieListTransition.stop();
        }
        movieListTransition = new TranslateTransition(Duration.millis(360), movieList);
        movieListTransition.setFromY(movieList.getTranslateY());
        if (show) {
            movieListTransition.setToY(0.0);
        } else {
            movieListTransition.setToY(100);
        }
        movieListTransition.play();

        if (movieListGlaspaneTransition != null) {
            movieListGlaspaneTransition.stop();
        }
        movieListGlaspaneTransition = new FadeTransition(Duration.millis(320), movieListGlasspane);
        movieListGlaspaneTransition.setFromValue(movieListGlasspane.getOpacity());
        if (show) {
            movieListGlaspaneTransition.setToValue(0.0);
        } else {
            movieListGlaspaneTransition.setToValue(1.0);
        }
        movieListGlaspaneTransition.play();
    }

    private void updateDetailArea() {
        backgroundImageView.imageProperty().unbind();
        posterImageView.imageProperty().unbind();
        titleText.textProperty().unbind();
        descriptionText.textProperty().unbind();

        Movie currentMovie = model.getSelectedMovie();
        if (currentMovie != null) {
            titleText.textProperty().bind(currentMovie.titleProperty());
            descriptionText.textProperty().bind(substring(currentMovie.synopsisProperty(), new SimpleIntegerProperty(500)));
            posterImageView.imageProperty().bind(currentMovie.createPosterImageBinding());
            backgroundImageView.imageProperty().bind(currentMovie.createBackgroundImageBinding());
        }
    }

    private StringBinding substring(final StringProperty property, final IntegerProperty lenght) {
        return Bindings.createStringBinding(() -> {
            if (property.get() != null && property.get().length() > lenght.get()) {
                return property.getValue().substring(0, lenght.get()) + "...";
            }
            return property.get();
        }, property);
    }

    public static Dimension2D shouldFitIn(double originalWidth, double originalHeight, double toFitWidth, double toFitHeight) {
        double fitRatio = toFitWidth / toFitHeight;
        double originalRatio = originalWidth / originalHeight;

        if (fitRatio > originalRatio) {
            //Die Weite muss zur Weite der Komponente passen. Oben & Unten abschneiden
            double widthFactor = toFitWidth / originalWidth;
            return new Dimension2D(toFitWidth, originalHeight * widthFactor);
        } else {
            //Die Höhe muss zur Höhe der Komponente passen. Links & Rechts abschneiden
            double heightFactor = toFitHeight / originalHeight;
            return new Dimension2D(originalWidth * heightFactor, toFitHeight);
        }
    }

}
