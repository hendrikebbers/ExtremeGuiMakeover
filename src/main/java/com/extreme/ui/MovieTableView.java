package com.extreme.ui;

import com.extreme.data.Movie;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MovieTableView extends TableView<Movie> {

    public MovieTableView() {

        TableColumn<Movie, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setPrefWidth(300);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Movie, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setPrefWidth(80);
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Movie, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setPrefWidth(140);
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

        TableColumn<Movie, Integer> directorColumn = new TableColumn<>("Director");
        directorColumn.setPrefWidth(140);
        directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));

        TableColumn<Movie, Integer> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setPrefWidth(120);
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

        getColumns().setAll(titleColumn, yearColumn, genreColumn, directorColumn, ratingColumn);
    }
}
