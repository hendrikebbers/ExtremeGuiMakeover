package com.extreme;

import com.extreme.data.Database;
import com.extreme.data.SlidesDatabase;
import com.extreme.feature.Feature;
import com.extreme.feature.FeaturesDialog;
import com.extreme.ui.MovieView;
import com.extreme.view.MasterDetailViewFeatures;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class MovieApp extends Application {

    private Stage currentStage;

    private static final Database database = Database.loadDatabase();
    public static final SlidesDatabase slidesDatabase = SlidesDatabase.loadDatabase();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button demo1Button = new Button("Dirk");
        Button demo2Button = new Button("Hendrik");

        VBox.setVgrow(demo1Button, Priority.ALWAYS);
        VBox.setVgrow(demo2Button, Priority.ALWAYS);

        demo1Button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        demo2Button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        demo1Button.getStyleClass().add("demo1");
        demo2Button.getStyleClass().add("demo2");

        demo1Button.setOnAction(evt -> showDemo1());
        demo2Button.setOnAction(evt -> showDemo2());

        VBox vBox = new VBox(demo1Button, demo2Button);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add(MovieApp.class.getResource("/chooser.css").toExternalForm());
        scene.getStylesheets().add(MovieApp.class.getResource("/fonts.css").toExternalForm());

        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setTitle("Launcher");
        primaryStage.setScene(scene);
        primaryStage.setHeight(300);
        primaryStage.setWidth(150);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void showDemo1() {
        MovieView movieView = new MovieView();
        Scene scene = new Scene(movieView);
        scene.getStylesheets().add(MovieApp.class.getResource("/styles1.css").toExternalForm());

        Stage demo1Stage = new Stage();
        demo1Stage.setTitle("Movie Database");
        demo1Stage.setScene(scene);
        demo1Stage.sizeToScene();
        demo1Stage.show();

        FeaturesDialog featuresDialog1 = new FeaturesDialog(demo1Stage);
        featuresDialog1.addFeature(new Feature("CSS - Base", "demo1-css-base", movieView.useCssBaseProperty()));
        featuresDialog1.addFeature(new Feature("CSS - Details", "demo1-css-details", movieView.useCssAllProperty()));
        featuresDialog1.addFeature(new Feature("CSS - Custom Fonts", "demo1-css-fonts", movieView.useCustomFontsProperty()));
        featuresDialog1.addFeature(new Feature("Filtering", "demo1-filtering", movieView.enableSortingAndFilteringProperty()));
        featuresDialog1.addFeature(new Feature("Media View - Trailers", "demo1-media-view-trailers", movieView.mediaViewTrailersProperty()));
        featuresDialog1.addFeature(new Feature("Media View - Animations", "demo1-media-view-animations", movieView.animateMediaViewTrailersProperty()));
        featuresDialog1.addFeature(new Feature("Media View - Background", "demo1-media-view-background", movieView.showMediaViewBackgroundProperty()));
        featuresDialog1.addFeature(new Feature("List View", "demo1-list-view", movieView.useListViewProperty()));
        featuresDialog1.addFeature(new Feature("List View - Cell Factory", "demo1-list-view-cell-factory", movieView.useListViewCellFactoryProperty()));
        featuresDialog1.addFeature(new Feature("List View - Clipping", "demo1-list-view-clipping", movieView.useClippingProperty()));
        featuresDialog1.addFeature(new Feature("List View - Scroll Bar", "demo1-list-view-scroll-bar", movieView.usePrettyListViewProperty()));
        featuresDialog1.addFeature(new Feature("Poster - Effects", "demo1-poster-effects", movieView.enableEffectsProperty()));
        featuresDialog1.addFeature(new Feature("ControlsFX", "demo1-controlsfx", movieView.useControlsFXProperty()));
        featuresDialog1.addFeature(new Feature("Drag & Drop", "demo1-dnd", movieView.enableDragAndDropOfPosterProperty()));
        featuresDialog1.addFeature(new Feature("Drag & Drop - Drag Image", "demo1-dnd-image", movieView.enableDragAndDropOfPosterWithDragImageProperty()));
        featuresDialog1.show();
    }

    private void showDemo2() {
        final MasterDetailViewFeatures features = new MasterDetailViewFeatures();

        currentStage = new Stage();

        try {
            showMasterDetailInWindow(currentStage, database, features);
        } catch (Exception e) {
            e.printStackTrace();
        }

        features.customWindowUIProperty().addListener((obs, oldVal, newVal) -> {
            if (currentStage != null) {
                currentStage.hide();
            }

            final Stage newWindow = new Stage();
            try {
                showMasterDetailInWindow(newWindow, database, features);
            } catch (Exception e) {
                e.printStackTrace();
            }

            currentStage = newWindow;
        });
    }

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
            updateStylesheets(scene, newVal);
        });

        updateStylesheets(scene, features.isUseCss());

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

        final FeaturesDialog featuresDialog = new FeaturesDialog(stage);
        featuresDialog.addFeature(new Feature("CSS", "demo2-css", features.useCssProperty()));
        featuresDialog.addFeature(new Feature("Image Background", "demo2-image-background",features.movieBackgroundProperty()));
        featuresDialog.addFeature(new Feature("List Animation", "demo2-list-animation",features.listAnimationProperty()));
        featuresDialog.addFeature(new Feature("List Shadow", "demo2-list-shadow",features.listShadowProperty()));
        featuresDialog.addFeature(new Feature("List Cache", "demo2-list-cache",features.listCacheProperty()));
        featuresDialog.addFeature(new Feature("Poster Transform", "demo2-poster-transform",features.posterTransformProperty()));
        featuresDialog.addFeature(new Feature("Custom Window UI", "demo2-custom-window-ui",features.customWindowUIProperty()));
        featuresDialog.addFeature(new Feature("Custom Window Clip", "demo2-custom-window-clip", features.customWindowClipProperty()));
        featuresDialog.show();
    }

    private void updateStylesheets(Scene scene, Boolean useCss) {
        if (useCss) {
            scene.getStylesheets().add(MovieApp.class.getResource("/styles2.css").toExternalForm());
            scene.getStylesheets().add(MovieApp.class.getResource("/listview.css").toExternalForm());
        } else {
            scene.getStylesheets().clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
