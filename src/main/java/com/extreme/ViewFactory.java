package com.extreme;

import com.extreme.data.Database;
import com.extreme.view.MasterDetailViewController;
import com.extreme.view.MasterDetailViewFeatures;
import com.extreme.view.MasterDetailViewModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ViewFactory {

    private ViewFactory() {
    }

    public static Parent createMasterDetailView(final Database database, final MasterDetailViewFeatures features) throws IOException {
        Objects.requireNonNull(database);
        final MasterDetailViewModel model = new MasterDetailViewModel();
        model.getMovies().addAll(database.getMovies());
        if(!model.getMovies().isEmpty()) {
            model.setSelectedMovie(model.getMovies().get(0));
        }
        final MasterDetailViewController controller = new MasterDetailViewController(model, features);
        final URL fxmlUrl = MasterDetailViewController.class.getResource("masterDetailView.fxml");
        final FXMLLoader loader = new FXMLLoader(fxmlUrl);
        loader.setController(controller);
        return loader.load();
    }

}
