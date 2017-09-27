package com.extreme.ui;

import com.extreme.MovieApp;
import com.extreme.data.Database;
import com.extreme.data.Movie;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import eu.hansolo.enzo.flippanel.FlipPanel;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Rating;
import org.controlsfx.control.textfield.CustomTextField;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lemmi on 14.09.17.
 */
public class MovieViewSkin extends SkinBase<MovieView> {

    private GridPane container = new GridPane();

    private Button showTrailerButton = new Button();

    private Database database;
    private MediaView mediaView;
    private GlassPane glassPane;

    public MovieViewSkin(MovieView view) {
        super(view);

        loadDatabase();

        container.getStyleClass().add("container");

        addBackground();
        addBackgroundMediaView();
        addToolBar();
        addPosterAndSynopsis();
        addTableAndListView();

        getChildren().add(container);

        glassPane = new GlassPane();
        glassPane.hideProperty().bind(view.selectedTrailerProperty().isNull().or(view.mediaViewTrailersProperty().not()));

        getChildren().add(glassPane);

        addTrailerView();

        view.setSelectedMovie(database.getMovies().get(0));
    }

    private void addBackground() {
        StackPane background = new StackPane();
        background.getStyleClass().add("background");
        getChildren().add(background);
    }

    private void addBackgroundMediaView() {
        Media media = new Media(MovieView.class.getResource("/backdrop-video.mp4").toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        player.setMute(true);
        MediaView mediaView = new MediaView(player);
        mediaView.setOpacity(.3);
        getChildren().add(mediaView);
        mediaView.visibleProperty().bind(getSkinnable().showMediaViewBackgroundProperty());
        getSkinnable().showMediaViewBackgroundProperty().addListener(it -> {
            if (getSkinnable().isShowMediaViewBackground()) {

                /*
                 * Implementing our own infinite loop support.
                 * The built-in cycle count support seems to be buggy.
                 */
                player.setOnEndOfMedia(() -> {
                    player.seek(Duration.ZERO);
                    player.play();
                });
                player.play();
            } else {
                player.stop();
            }
        });
    }

    private void addToolBar() {
        TextField textField = new TextField();
        textField.getStyleClass().add("search-field");
        textField.textProperty().bindBidirectional(getSkinnable().filterTextProperty());

        Text clearIcon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.TIMES_CIRCLE, "18");
        CustomTextField customTextField = new CustomTextField();
        customTextField.getStyleClass().add("search-field");
        customTextField.setLeft(FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.SEARCH, "18px"));
        customTextField.setRight(clearIcon);
        customTextField.textProperty().bindBidirectional(getSkinnable().filterTextProperty());
        clearIcon.setOnMouseClicked(evt -> customTextField.setText(""));

        FlipPanel searchFlipPanel = new FlipPanel();
        searchFlipPanel.setFlipDirection(Orientation.HORIZONTAL);
        searchFlipPanel.getFront().getChildren().add(textField);
        searchFlipPanel.getBack().getChildren().add(customTextField);
        searchFlipPanel.visibleProperty().bind(getSkinnable().enableSortingAndFilteringProperty());

        getSkinnable().useControlsFXProperty().addListener(it -> {
            if (getSkinnable().isUseControlsFX()) {
                searchFlipPanel.flipToBack();
            } else {
                searchFlipPanel.flipToFront();
            }
        });

        showTrailerButton = new Button("Show Trailer");
        showTrailerButton.getStyleClass().add("trailer-button");
        showTrailerButton.setMaxHeight(Double.MAX_VALUE);
        showTrailerButton.setOnAction(evt -> showTrailer());

        BorderPane toolBar = new BorderPane();
        toolBar.setLeft(showTrailerButton);
        toolBar.setRight(searchFlipPanel);
        toolBar.getStyleClass().add("movie-toolbar");

        container.add(toolBar, 1, 0);
    }

    private void addPosterAndSynopsis() {
        ImageView posterView = new ImageView();
        posterView.setFitWidth(180);
        posterView.setPreserveRatio(true);
        GridPane.setRowSpan(posterView, 2);
        GridPane.setValignment(posterView, VPos.TOP);
        container.add(posterView, 0, 1);

        getSkinnable().enableEffectsProperty().addListener(it -> {
            if (getSkinnable().isEnableEffects()) {
                Reflection reflection = new Reflection();
                reflection.setFraction(.3);
                reflection.setBottomOpacity(0);
                posterView.setEffect(reflection);
            } else {
                posterView.setEffect(null);
            }
        });

        Label synopsisLabel = new Label();
        synopsisLabel.setMinHeight(150);
        synopsisLabel.setMaxHeight(150);
        synopsisLabel.setWrapText(true);
        synopsisLabel.setAlignment(Pos.TOP_LEFT);
        synopsisLabel.getStyleClass().add("synopsis");
        GridPane.setValignment(synopsisLabel, VPos.TOP);

        Rating rating = new Rating();
        rating.visibleProperty().bind(getSkinnable().useControlsFXProperty());
        rating.setMax(5);
        rating.setPartialRating(true);

        getSkinnable().selectedMovieProperty().addListener(it -> {
            Movie selectedMovie = getSkinnable().getSelectedMovie();
            if (selectedMovie != null) {
                rating.setRating(selectedMovie.getRating() / 20); // to map 100% to 5 stars
            } else {
                rating.setRating(0);
            }
        });

        VBox vbox = new VBox(synopsisLabel, rating);
        vbox.getStyleClass().add("synopsis-container");

        container.add(vbox, 1, 1);

        getSkinnable().selectedMovieProperty().addListener(it -> {
            final Movie selectedMovie = getSkinnable().getSelectedMovie();
            if (selectedMovie != null) {
                final String posterFileName = selectedMovie.getPosterFileName();
                synopsisLabel.setText(getSkinnable().getSelectedMovie().getSynopsis());
                posterView.setImage(selectedMovie.createPosterImage());
            } else {
                synopsisLabel.setText("");
                posterView.setImage(null);
            }
        });

        getSkinnable().enableDragAndDropOfPosterProperty().addListener(it -> {
            if (getSkinnable().isEnableDragAndDropOfPoster()) {
                posterView.setOnDragDetected(evt -> {
                    Dragboard db = posterView.startDragAndDrop(TransferMode.COPY);

                    List<File> files = new ArrayList<>();

                    try {
                        final String posterFileName = getSkinnable().getSelectedMovie().getPosterFileName();

                        File file = new File(MovieView.class.getResource("/" + posterFileName).toURI());
                        files.add(file);

                        ClipboardContent content = new ClipboardContent();
                        content.putFiles(files);
                        db.setContent(content);
                    } catch (URISyntaxException ex) {
                        ex.printStackTrace();
                    }

                    evt.consume();
                });
            } else {
                posterView.setOnDragDetected(null);
            }
        });

        getSkinnable().enableDragAndDropOfPosterProperty().addListener(it -> {
            if (getSkinnable().isEnableDragAndDropOfPoster()) {
                vbox.setOnDragDetected(evt -> {

                    Dragboard db = vbox.startDragAndDrop(TransferMode.COPY);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(getSkinnable().getSelectedMovie().getSynopsis());
                    db.setContent(content);
                    if (getSkinnable().isEnableDragAndDropOfPosterWithDragImage()) {
                        SnapshotParameters param = new SnapshotParameters();
                        param.setFill(Color.BLACK);
                        WritableImage snapshot = vbox.snapshot(param, null);
                        db.setDragView(snapshot);
                    }
                    evt.consume();
                });
            } else {
                vbox.setOnDragDetected(null);
            }
        });

    }

    private void addTableAndListView() {
        FilteredList<Movie> filteredList = new FilteredList<>(database.getMovies());
        filteredList.predicateProperty().bind(getSkinnable().filterPredicateProperty());

        SortedList<Movie> tableViewSortedList = new SortedList<>(filteredList);
        TableView<Movie> tableView = new MovieTableView();
        tableViewSortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(tableViewSortedList);
        TableViewSelectionModel<Movie> tableViewSelectionModel = tableView.getSelectionModel();
        tableViewSelectionModel.selectedItemProperty().addListener(it -> getSkinnable().setSelectedMovie(tableViewSelectionModel.getSelectedItem()));

        PrettyListView<Movie> listView = new PrettyListView<>();
        listView.setItems(filteredList);
        MultipleSelectionModel<Movie> listViewSelectionModel = listView.getSelectionModel();
        listViewSelectionModel.selectedItemProperty().addListener(it -> getSkinnable().setSelectedMovie(listViewSelectionModel.getSelectedItem()));

        listView.setOnMouseClicked(evt -> {
            if (evt.getButton() == MouseButton.PRIMARY && evt.getClickCount() == 2) {
                showTrailer();
            }
        });

        Callback<ListView<Movie>, ListCell<Movie>> defaultCellFactory = listView.getCellFactory();

        getSkinnable().useListViewCellFactoryProperty().addListener(it -> {
            if (getSkinnable().isUseListView()) {
                listView.setCellFactory(view -> new MovieCell(getSkinnable()));
            } else {
                listView.setCellFactory(defaultCellFactory);
            }
        });

        getSkinnable().selectedMovieProperty().addListener(it -> {
            tableViewSelectionModel.select(getSkinnable().getSelectedMovie());
            listViewSelectionModel.select(getSkinnable().getSelectedMovie());
        });

        FlipPanel flipPanel = new FlipPanel();

        flipPanel.setFlipDirection(Orientation.HORIZONTAL);
        flipPanel.getFront().getChildren().add(tableView);
        flipPanel.getBack().getChildren().add(listView);

        getSkinnable().useListViewProperty().addListener(it -> {
            if (getSkinnable().isUseListView()) {
                flipPanel.flipToBack();
                if (getSkinnable().isUseListViewCellFactory()) {
                    listView.setCellFactory(view -> new MovieCell(getSkinnable()));
                }
            } else {
                flipPanel.flipToFront();
            }
        });

        GridPane.setVgrow(flipPanel, Priority.ALWAYS);
        container.add(flipPanel, 1, 2);
    }

    private void addTrailerView() {
        mediaView = new MediaView();
        mediaView.setEffect(new DropShadow());
        mediaView.setFitWidth(800);
        mediaView.setFitHeight(450);
        mediaView.setOnMouseClicked(evt -> {
            MediaPlayer player = mediaView.getMediaPlayer();
            if (player != null) {
                if (player.getStatus().equals(Status.PLAYING)) {
                    player.pause();
                } else {
                    player.play();
                }
            }
        });

        mediaView.setManaged(false);
        mediaView.setLayoutY(-mediaView.getFitHeight());

        getSkinnable().selectedTrailerProperty().addListener(it -> {
            final String selectedTrailer = getSkinnable().getSelectedTrailer();

            if (selectedTrailer != null) {

                if (getSkinnable().isMediaViewTrailers()) {
                    Media media = new Media(selectedTrailer);
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setAutoPlay(true);
                    mediaPlayer.setOnStopped(() -> mediaPlayer.dispose());

                    mediaView.setMediaPlayer(mediaPlayer);
                    mediaView.setVisible(true);

                    if (getSkinnable().isAnimateMediaViewTrailers()) {

                        mediaView.setManaged(false);
                        mediaView.setLayoutY(-mediaView.getFitHeight());
                        mediaView.setLayoutX((getSkinnable().getWidth() - mediaView.getFitWidth()) / 2);
                        mediaView.setOpacity(0);

                        KeyValue layoutYValue = new KeyValue(mediaView.layoutYProperty(), (getSkinnable().getHeight() - mediaView.getFitHeight()) / 2, Interpolator.EASE_OUT);
                        KeyValue opacityValue = new KeyValue(mediaView.opacityProperty(), 1, Interpolator.EASE_OUT);

                        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), layoutYValue, opacityValue);
                        Timeline timeline = new Timeline(keyFrame);
                        timeline.setOnFinished(evt -> mediaView.setManaged(true));
                        timeline.play();

                    } else {
                        mediaView.setOpacity(1);
                        mediaView.setManaged(true);
                    }
                } else {

                    try {
                        Desktop.getDesktop().browse(new URI(getSkinnable().getSelectedMovie().getYouTube()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }

            } else {

                if (getSkinnable().isAnimateMediaViewTrailers()) {

                    mediaView.setManaged(false);

                    KeyValue layoutYValue = new KeyValue(mediaView.layoutYProperty(), -mediaView.getFitHeight(), Interpolator.EASE_IN);
                    KeyValue opacityValue = new KeyValue(mediaView.opacityProperty(), 0, Interpolator.EASE_IN);
                    KeyValue volumeValue = new KeyValue(mediaView.getMediaPlayer().volumeProperty(), 0, Interpolator.LINEAR);

                    KeyFrame keyFrame = new KeyFrame(Duration.millis(500), layoutYValue, opacityValue, volumeValue);
                    Timeline timeline = new Timeline(keyFrame);
                    timeline.setOnFinished(evt -> stopMediaPlayer());
                    timeline.play();

                } else {
                    stopMediaPlayer();
                    mediaView.setVisible(false);
                    mediaView.setManaged(false);
                    mediaView.setLayoutY(-mediaView.getFitHeight());
                }
            }
        });

        getChildren().add(mediaView);
    }

    private void stopMediaPlayer() {
        MediaPlayer mediaPlayer = mediaView.getMediaPlayer();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    private void showTrailer() {
        Movie movie = getSkinnable().getSelectedMovie();
        final URL resource = MovieApp.class.getResource("/trailers/" + movie.getTrailer());
        if (resource != null) {
            String trailer = resource.toExternalForm();
            getSkinnable().setSelectedTrailer(trailer);
        } else {
            getSkinnable().setSelectedTrailer("");
        }
    }

    class GlassPane extends StackPane {

        public GlassPane() {

            getStyleClass().add("glasspane");

            setMouseTransparent(false);
            setOnMouseClicked(evt -> getSkinnable().setSelectedTrailer(null));
            setVisible(false);

            hideProperty().addListener(it -> {

                setVisible(true);

                if (getSkinnable().isAnimateMediaViewTrailers()) {
                    FadeTransition fadeTransition = new FadeTransition();
                    fadeTransition.setDuration(Duration.millis(500));
                    fadeTransition.setNode(GlassPane.this);
                    fadeTransition.setFromValue(GlassPane.this.isHide() ? .85 : 0);
                    fadeTransition.setToValue(GlassPane.this.isHide() ? 0 : .85);
                    fadeTransition.setOnFinished(evt -> {
                        if (isHide()) {
                            setVisible(false);
                        }
                    });
                    fadeTransition.play();
                } else {
                    if (isHide()) {
                        setVisible(false);
                    } else {
                        setVisible(true);
                    }
                }
            });
        }

        private final BooleanProperty hide = new SimpleBooleanProperty(this, "hide");

        public final BooleanProperty hideProperty() {
            return hide;
        }

        public final void setHide(boolean hide) {
            this.hide.set(hide);
        }

        public final boolean isHide() {
            return hide.get();
        }
    }

    private void loadDatabase() {
        try {
            JAXBContext ctx = JAXBContext.newInstance(Database.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            database = (Database) unmarshaller.unmarshal(MovieApp.class.getResourceAsStream("/movies.xml"));
            for (Movie movie : database.getMovies()) {
                System.out.println("loaded movie: " + movie.getTitle());
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void saveDatabase() {
        try {
            JAXBContext ctx = JAXBContext.newInstance(Database.class);
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(database, new File("/Users/dirk/Desktop/movies.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
