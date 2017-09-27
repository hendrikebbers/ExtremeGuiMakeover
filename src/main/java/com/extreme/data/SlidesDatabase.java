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
import java.util.Optional;

@XmlRootElement(name = "slides-database")
public class SlidesDatabase {

    @XmlElement(name = "slide-entry")
    public final ObservableList<SlidesEntry> slideEntries = FXCollections.observableArrayList();

    public final ObservableList<SlidesEntry> getSlideEntries() {
        return slideEntries;
    }

    public Optional<SlidesEntry> getSlideEntry(String featureId) {
        return slideEntries.stream().filter(entry -> entry.getFeatureId().equals(featureId)).findFirst();
    }

    public static SlidesDatabase loadDatabase() {
        SlidesDatabase database = null;

        try (InputStream in = MovieApp.class.getResourceAsStream("/slides.xml")) {

            JAXBContext ctx = JAXBContext.newInstance(SlidesDatabase.class);
            Unmarshaller unmarshaller = ctx.createUnmarshaller();
            database = (SlidesDatabase) unmarshaller.unmarshal(in);

        } catch (JAXBException | IOException ex) {
            ex.printStackTrace();
        }

        return database;
    }

//    private static void saveDatabase() {
//        SlidesDatabase database = new SlidesDatabase();
//
//        SlidesEntry entry = new SlidesEntry();
//        database.getSlideEntries().add(entry);
//        Slide slide = new Slide();
//        entry.getSlides().add(slide);
//
//        SlidesEntry entry2 = new SlidesEntry();
//        database.getSlideEntries().add(entry2);
//        Slide slide2 = new Slide();
//        entry2.getSlides().add(slide2);
//
//        try {
//            JAXBContext ctx = JAXBContext.newInstance(SlidesDatabase.class);
//            Marshaller marshaller = ctx.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//            marshaller.marshal(database, new File("/Users/lemmi/Desktop/slides.xml"));
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        saveDatabase();
//    }
}
