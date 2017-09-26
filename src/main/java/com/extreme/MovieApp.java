package com.extreme;

import com.extreme.data.Database;
import com.extreme.feature.Feature;
import com.extreme.feature.FeaturesDialog;
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


        final FeaturesDialog featuresDialog2 = new FeaturesDialog(primaryStage);
        featuresDialog2.addFeature(new Feature("CSS", features.useCssProperty()));
        featuresDialog2.addFeature(new Feature("Image Background", features.movieBackgroundProperty()));
        featuresDialog2.addFeature(new Feature("List Animation", features.listAnimationProperty()));
        featuresDialog2.addFeature(new Feature("List Shadow", features.listShadowProperty()));
        featuresDialog2.addFeature(new Feature("List Cache", features.listCacheProperty()));
        featuresDialog2.addFeature(new Feature("Poster Transform", features.posterTransformProperty()));
        featuresDialog2.addFeature(new Feature("Custom Window UI", features.customWindowUIProperty()));
        featuresDialog2.addFeature(new Feature("Custom Window Clip", features.customWindowClipProperty()));
        featuresDialog2.show();



        final MovieView movieView = new MovieView();
        final FeaturesDialog featuresDialog = new FeaturesDialog(primaryStage);
        featuresDialog.addFeature(new Feature("CSS", movieView.useCssProperty()));
        featuresDialog.addFeature(new Feature("CSS - Custom Fonts", movieView.useCustomFontsProperty()));
        featuresDialog.addFeature(new Feature("Filtering", movieView.enableSortingAndFilteringProperty()));
        featuresDialog.addFeature(new Feature("Media View - Trailers", movieView.mediaViewTrailersProperty()));
        featuresDialog.addFeature(new Feature("Media View - Background", movieView.showMediaViewBackgroundProperty()));
        featuresDialog.addFeature(new Feature("Media View - Animations", movieView.animateMediaViewTrailersProperty()));
        featuresDialog.addFeature(new Feature("List View", movieView.useListViewProperty()));
        featuresDialog.addFeature(new Feature("List View - Cell Factory", movieView.useListViewCellFactoryProperty()));
        featuresDialog.addFeature(new Feature("List View - Clipping", movieView.useClippingProperty()));
        featuresDialog.addFeature(new Feature("List View - Scroll Bar", movieView.usePrettyListViewProperty()));
        featuresDialog.addFeature(new Feature("Poster - Effects", movieView.enableEffectsProperty()));
        featuresDialog.addFeature(new Feature("Poster - Parallax", movieView.enableParallaxProperty()));
        featuresDialog.addFeature(new Feature("ControlsFX", movieView.useControlsFXProperty()));
        featuresDialog.addFeature(new Feature("Drag & Drop", movieView.enableDragAndDropOfPosterProperty()));
        featuresDialog.addFeature(new Feature("Drag & Drop - Drag Image", movieView.enableDragAndDropOfPosterWithDragImageProperty()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
