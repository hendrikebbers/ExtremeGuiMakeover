package com.extreme;

import com.extreme.data.Database;
import com.extreme.feature.Feature;
import com.extreme.feature.OptionsDialog;
import com.extreme.ui.MovieView;
import com.extreme.view.MasterDetailViewFeatures;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class MovieApp extends Application {

    private Stage currentStage;

    private void showMasterDetailInWindow(final Stage stage, final Database database, final MasterDetailViewFeatures features) throws JAXBException, IOException {
        final Parent viewRoot = ViewFactory.createMasterDetailView(database, features);

        final Rectangle clip = new Rectangle();
        clip.setArcHeight(18);
        clip.setArcWidth(18);
        clip.widthProperty().bind(stage.widthProperty());
        clip.heightProperty().bind(stage.heightProperty());
        //TODO: Only clipping or PerspectiveCamera is working... :(
        features.customWindowClipProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                viewRoot.setClip(clip);
            } else {
                viewRoot.setClip(null);
            }
        });

        final Scene scene = new Scene(viewRoot);

        features.useCssProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal) {
                scene.getStylesheets().add(MovieApp.class.getResource("/styles.css").toExternalForm());
                scene.getStylesheets().add(MovieApp.class.getResource("/listview.css").toExternalForm());
            } else {
                scene.getStylesheets().clear();
            }
        });

        scene.setFill(Color.TRANSPARENT);
        scene.setCamera(new PerspectiveCamera());

        if (features.isCustomWindowUI()) {
            stage.initStyle(StageStyle.TRANSPARENT);
        }

        stage.setTitle("Movie Database");
        stage.setScene(scene);
        stage.setWidth(1100);
        stage.setHeight(720);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);

        final MasterDetailViewFeatures features = new MasterDetailViewFeatures();
        final Database database = Database.loadDefaultDatabase();

        currentStage = primaryStage;
        showMasterDetailInWindow(currentStage, database, features);
        features.customWindowUIProperty().addListener((obs, oldVal, newVal) -> {
            final Stage newWindow = new Stage();
            try {
                showMasterDetailInWindow(newWindow, database, features);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(currentStage != null) {
                final Stage toHide = currentStage;
                //Platform.runLater(() -> toHide.close());
            }
            currentStage = newWindow;

        });


        final OptionsDialog optionsDialog = new OptionsDialog(primaryStage);
        optionsDialog.addFeature(new Feature("CSS", features.useCssProperty()));
        optionsDialog.addFeature(new Feature("Image Background", features.movieBackgroundProperty()));
        optionsDialog.addFeature(new Feature("List Animation", features.listAnimationProperty()));
        optionsDialog.addFeature(new Feature("List Shadow", features.listShadowProperty()));
        optionsDialog.addFeature(new Feature("List Cache", features.listCacheProperty()));
        optionsDialog.addFeature(new Feature("Poster Transform", features.posterTransformProperty()));
        optionsDialog.addFeature(new Feature("Custom Window UI", features.customWindowUIProperty()));
        optionsDialog.addFeature(new Feature("Custom Window Clip", features.customWindowClipProperty()));



        final MovieView movieView = new MovieView();
        //Stage stage2 = new Stage();
        //stage2.setScene(new Scene(movieView));
        //stage2.show();


        optionsDialog.addFeature(new Feature("CSS", movieView.useCssProperty()));
        optionsDialog.addFeature(new Feature("CSS - Custom Fonts", movieView.useCustomFontsProperty()));
        optionsDialog.addFeature(new Feature("Filtering", movieView.enableSortingAndFilteringProperty()));
        optionsDialog.addFeature(new Feature("Media View - Trailers", movieView.mediaViewTrailersProperty()));
        optionsDialog.addFeature(new Feature("Media View - Background", movieView.showMediaViewBackgroundProperty()));
        optionsDialog.addFeature(new Feature("Media View - Animations", movieView.animateMediaViewTrailersProperty()));
        optionsDialog.addFeature(new Feature("List View", movieView.useListViewProperty()));
        optionsDialog.addFeature(new Feature("List View - Cell Factory", movieView.useListViewCellFactoryProperty()));
        optionsDialog.addFeature(new Feature("List View - Clipping", movieView.useClippingProperty()));
        optionsDialog.addFeature(new Feature("List View - Scroll Bar", movieView.usePrettyListViewProperty()));
        optionsDialog.addFeature(new Feature("Poster - Effects", movieView.enableEffectsProperty()));
        optionsDialog.addFeature(new Feature("Poster - Parallax", movieView.enableParallaxProperty()));
        optionsDialog.addFeature(new Feature("ControlsFX", movieView.useControlsFXProperty()));
        optionsDialog.addFeature(new Feature("Drag & Drop", movieView.enableDragAndDropOfPosterProperty()));
        optionsDialog.addFeature(new Feature("Drag & Drop - Drag Image", movieView.enableDragAndDropOfPosterWithDragImageProperty()));

        optionsDialog.setOnHidden(e -> System.exit(0));
        optionsDialog.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
