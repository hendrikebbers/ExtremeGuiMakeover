package com.extreme;

import com.extreme.data.Slide;
import com.extreme.data.SlidesEntry;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SlidesViewer {

    private final static SlidesViewer viewer = new SlidesViewer();

    private Stage stage;
    private Pagination pagination;
    private ImageView titleImageView;
    private StackPane stackPane;

    public SlidesViewer() {
        pagination = new Pagination();

        pagination.setOnMouseClicked(evt -> nextSlide());
        pagination.setOnSwipeLeft(evt -> previousSlide());
        pagination.setOnSwipeRight(evt -> nextSlide());
        pagination.setOnScrollStarted(evt -> {

            if (evt.getTotalDeltaX() > 0) {
                nextSlide();
            } else {
                previousSlide();
            }

        });

        pagination.setOnKeyPressed(evt -> {
            switch (evt.getCode()) {
                case LEFT:
                    previousSlide();
                    break;
                case RIGHT:
                case SPACE:
                    nextSlide();
                    break;
            }
        });

        stackPane = new StackPane();

        titleImageView = new ImageView(SlidesViewer.class.getResource("/titleBW.png").toExternalForm());
        titleImageView.fitWidthProperty().bind(stackPane.widthProperty());
        titleImageView.fitHeightProperty().bind(stackPane.heightProperty());
        titleImageView.setPreserveRatio(true);

        stackPane.getChildren().addAll(pagination, titleImageView);
        stackPane.getStyleClass().add("container");
        stackPane.setPrefSize(0, 0);

        Scene scene = new Scene(stackPane);
        scene.getStylesheets().add(SlidesViewer.class.getResource("/slides-viewer.css").toExternalForm());

        stage = new Stage();
        stage.setScene(scene);
        stage.setWidth(960);
        stage.setHeight(540);

        slidesEntry.addListener(it -> updateViewer());
    }

    private void nextSlide() {
        pagination.setCurrentPageIndex(Math.min(pagination.getPageCount() - 1, pagination.getCurrentPageIndex() + 1));
    }

    private void previousSlide() {
        pagination.setCurrentPageIndex(Math.max(0, pagination.getCurrentPageIndex() - 1));
    }

    public void showSlides() {
        stage.show();
        stage.toFront();
    }

    private void updateViewer() {
        SlidesEntry entry = getSlidesEntry();
        if (entry == null) {
            pagination.toBack();
            pagination.setVisible(false);
        } else {

            stage.setTitle(entry.getTitle());

            pagination.setPageFactory(pageNumber -> {
                Slide slide = entry.getSlides().get(pageNumber);

                Image image = new Image(SlidesViewer.class.getResource("/" + slide.getName()).toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.fitWidthProperty().bind(stackPane.widthProperty().multiply(.9));
                imageView.fitHeightProperty().bind(stackPane.heightProperty().multiply(.9));
                imageView.setPreserveRatio(true);

                return imageView;
            });

            pagination.setPageCount(entry.getSlides().size());
            pagination.setVisible(true);
            pagination.toFront();
            pagination.requestFocus();
        }
    }

    private ObjectProperty<SlidesEntry> slidesEntry = new SimpleObjectProperty<>();

    public SlidesEntry getSlidesEntry() {
        return slidesEntry.get();
    }

    public void setSlidesEntry(SlidesEntry slidesEntry) {
        this.slidesEntry.set(slidesEntry);
    }

    public static void showSlides(SlidesEntry entry, boolean openWindow) {
        viewer.setSlidesEntry(entry);
        if (openWindow) {
            viewer.showSlides();
        }
    }
}
