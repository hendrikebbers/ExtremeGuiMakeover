package com.extreme.data;

import com.extreme.MovieApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "database")
public class Database {

    @XmlElement(name = "movie")
    public final ObservableList<Movie> movies = FXCollections.observableArrayList();

    public final ObservableList<Movie> getMovies() {
        return movies;
    }


    public static Database loadDefaultDatabase() throws JAXBException {
        final JAXBContext ctx = JAXBContext.newInstance(Database.class);
        final Unmarshaller unmarshaller = ctx.createUnmarshaller();
        final Database database = (Database) unmarshaller.unmarshal(MovieApp.class.getResourceAsStream("/movies.xml"));
        for (Movie movie : database.getMovies()) {
            System.out.println("loaded movie: " + movie.getTitle());
        }
        return database;
    }
}
