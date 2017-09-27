package com.extreme;

import com.extreme.data.Slide;
import com.extreme.data.SlidesEntry;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SlidesViewer {

    private final static SlidesViewer viewer = new SlidesViewer();

    private Stage stage;
    private Pagination pagination;

    public SlidesViewer() {
        pagination = new Pagination();

        pagination.setOnKeyPressed(evt -> {
            switch (evt.getCode()) {
                case LEFT:
                    pagination.setCurrentPageIndex(Math.max(0, pagination.getCurrentPageIndex() - 1));
                    break;
                case RIGHT:
                case SPACE:
                    pagination.setCurrentPageIndex(Math.min(pagination.getPageCount() - 1, pagination.getCurrentPageIndex() + 1));
                    break;
            }
        });

        Scene scene = new Scene(pagination);
        scene.getStylesheets().add(SlidesViewer.class.getResource("/slides-viewer.css").toExternalForm());

        stage = new Stage();
        stage.setScene(scene);
        stage.setWidth(1000);
        stage.setHeight(600);

        slidesEntry.addListener(it -> updateViewer());
    }

    public void showSlides() {
        stage.show();
    }

    private void updateViewer() {
        SlidesEntry entry = getSlidesEntry();
        if (entry == null) {
            pagination.setVisible(false);
        } else {

            stage.setTitle(entry.getTitle());

            pagination.setPageFactory(page -> {
                Slide slide = entry.getSlides().get(page);
                Image image = new Image(SlidesViewer.class.getResource("/" + slide.getName()).toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.fitWidthProperty().bind(pagination.widthProperty());
                imageView.fitHeightProperty().bind(pagination.heightProperty());
                imageView.setPreserveRatio(true);
                return imageView;
            });

            pagination.setPageCount(entry.getSlides().size());
            pagination.setVisible(true);
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
