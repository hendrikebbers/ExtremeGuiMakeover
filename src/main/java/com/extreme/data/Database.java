package com.extreme.data;

import com.extreme.MovieApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.InputStream;

@XmlRootElement(name = "database")
public class Database {

    @XmlElement(name = "movie")
    public final ObservableList<Movie> movies = FXCollections.observableArrayList();

    public final ObservableList<Movie> getMovies() {
        return movies;
    }


    public static Database loadDatabase() {
        Database database = null;

        try (InputStream in = MovieApp.class.getResourceAsStream("/movies.xml")) {

            JAXBContext ctx = JAXBContext.newInstance(Database.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            database = (Database) unmarshaller.unmarshal(in);

        } catch (JAXBException | IOException ex) {
            ex.printStackTrace();
        }

        return database;
    }
}
