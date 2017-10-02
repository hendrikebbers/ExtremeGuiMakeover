package com.extreme.view;

import com.extreme.Util;
import com.extreme.data.Movie;
import com.extreme.ui.AnimatedIcon;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class MasterDetailViewController implements Initializable {

    private AnimatedIcon mediaStateIcon;

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

    @FXML
    private StackPane mediaPane;

    @FXML
    private MediaView mediaView;

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


        watchTrailerButton.setOnAction(e -> playTrailer());
        mediaView.fitWidthProperty().bind(rootPane.widthProperty().subtract(64.0));

        mediaStateIcon = new AnimatedIcon();
        mediaStateIcon.setOpacity(0.6);
        mediaStateIcon.setScaleX(4.0);
        mediaStateIcon.setScaleY(4.0);
        mediaStateIcon.setMouseTransparent(true);
        mediaStateIcon.toPause();

        Circle circle = new Circle();
        circle.setMouseTransparent(true);
        circle.setRadius(76);
        circle.setStroke(Color.ORANGE);
        circle.setStrokeWidth(8);
        circle.setOpacity(0.6);

        StackPane mediaControl = new StackPane(circle, mediaStateIcon);
        mediaControl.setMouseTransparent(true);
        mediaControl.setMinWidth(USE_PREF_SIZE);
        mediaControl.setMinHeight(USE_PREF_SIZE);
        mediaControl.setMaxWidth(USE_PREF_SIZE);
        mediaControl.setMaxHeight(USE_PREF_SIZE);
        mediaControl.setVisible(false);

        mediaPane.getChildren().addAll(mediaControl);

        //TODO: Animation
        mediaView.setOnMouseEntered(e -> mediaControl.setVisible(true));
        mediaView.setOnMouseExited(e -> mediaControl.setVisible(false));
    }

    private void playTrailer() {
        final Media media = model.getSelectedMovie().loadTrailer();
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPane.setVisible(false);
            mediaPlayer.stop();
            mediaPlayer.dispose();
        });
        mediaView.setOnMouseClicked(e -> {
            if(MediaPlayer.Status.PLAYING.equals(mediaPlayer.getStatus())) {
                mediaPlayer.pause();
                mediaStateIcon.toPlay();
            } else {
                mediaPlayer.play();
                mediaStateIcon.toPause();
            }
        });
        mediaView.setMediaPlayer(mediaPlayer);
        mediaPane.setVisible(true);
        mediaPlayer.play();
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
            double widthFactor = toFitWidth / originalWidth;
            return new Dimension2D(toFitWidth, originalHeight * widthFactor);
        } else {

            double heightFactor = toFitHeight / originalHeight;
            return new Dimension2D(originalWidth * heightFactor, toFitHeight);
        }
    }

}
