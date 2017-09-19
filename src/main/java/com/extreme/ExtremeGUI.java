package com.extreme;

import com.extreme.data.Database;
import com.extreme.data.Movie;
import com.extreme.ui.MovieTableView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ExtremeGUI extends Application {

    private static Database database;

    private Label synopsisLabel;
    private MovieTableView tableView;
    private MediaView mediaView;

    @Override
    public void start(Stage primaryStage) throws Exception {

//        database = new Database();
//
//        Movie movie = new Movie();
//        movie.setTitle("ExtremeGUI");
//        movie.setYear(1969);
//        movie.setDirector("Dirk");
//        movie.setGenre(Genre.ACTION);
//        movie.setPosterFileName("poster.png");
//        movie.setRating(10);
//        movie.setTrailer("http://www.dlsc.com/movie.html");
//        movie.setSynopsis("OnceUponATime");
//        database.getMovies().addAll(movie, movie);
//
//        saveDatabase();

        loadDatabase();

        mediaView = new MediaView();
        mediaView.setFitHeight(300);
        mediaView.setOnMouseClicked(evt -> {
            if (mediaView.getMediaPlayer().getStatus().equals(MediaPlayer.Status.PLAYING)) {
                mediaView.getMediaPlayer().pause();
            } else {
                mediaView.getMediaPlayer().play();
            }
        });

        synopsisLabel = new Label();
        synopsisLabel.setWrapText(true);
        synopsisLabel.getStyleClass().add("synopsis-label");

        HBox upperHBox = new HBox(20, synopsisLabel, mediaView);
        upperHBox.setStyle("-fx-padding: 20;");
        VBox.setVgrow(upperHBox, Priority.ALWAYS);

        tableView = new MovieTableView();
        tableView.setItems(database.getMovies());


        tableView.getSelectionModel().select(0);

        HBox lowerHBox = new HBox(20, tableView);
        lowerHBox.setStyle("-fx-padding: 20;");
        lowerHBox.setFillHeight(false);
        lowerHBox.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(tableView, Priority.ALWAYS);

        tableView.getSelectionModel().selectedItemProperty().addListener(it -> selectionChanged());
        selectionChanged();

        VBox vBox = new VBox(lowerHBox, upperHBox);
        vBox.setFillWidth(true);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add(ExtremeGUI.class.getResource("/styles.css").toExternalForm());

        primaryStage.setTitle(("Extreme GUI"));
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void selectionChanged() {
        Movie selectedItem = tableView.getSelectionModel().getSelectedItem();
        synopsisLabel.setText(selectedItem.getSynopsis());

        MediaPlayer currentPlayer = mediaView.getMediaPlayer();
        if (currentPlayer != null) {
            currentPlayer.stop();
        }

//        Media media = new Media(ExtremeGUI.class.getResource("/trailers/" + selectedItem.getTrailer()).toExternalForm());
//        MediaPlayer player = new MediaPlayer(media);
//        mediaView.setMediaPlayer(player);
//        player.play();
    }


    private static void loadDatabase() {
        try {
            JAXBContext ctx = JAXBContext.newInstance(Database.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            database = (Database) unmarshaller.unmarshal(ExtremeGUI.class.getResourceAsStream("/movies.xml"));
            for (Movie movie : database.getMovies()) {
                System.out.println("loaded movie: " + movie.getTitle());
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void saveDatabase() {
        try {
            JAXBContext ctx = JAXBContext.newInstance(Database.class);
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(database, new File("/Users/dirk/Desktop/movies.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
