package com.extreme.ui;

import com.extreme.data.Movie;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.util.function.Predicate;

/**
 * Created by lemmi on 14.09.17.
 */
public class MovieView extends Control {

    private final String optionalStylesheet = MovieView.class.getResource("/optional.css").toExternalForm();

    private final String fontsStylesheet = MovieView.class.getResource("/fonts.css").toExternalForm();

    private final String listViewStylesheet = MovieView.class.getResource("/listview.css").toExternalForm();

    public MovieView() {
        getStyleClass().add("movie-view");

        useCss.addListener(it -> updateOptionalStylesheet());
        useCustomFonts.addListener(it -> updateFontStylesheet());
        usePrettyListView.addListener(it -> updateListViewStylesheet());

        filterText.addListener(it -> filterPredicate.set(movie -> movie.getTitle().toLowerCase().contains(getFilterText().toLowerCase())));
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MovieViewSkin(this);
    }

    private final StringProperty selectedTrailer = new SimpleStringProperty();

    public final StringProperty selectedTrailerProperty() {
        return selectedTrailer;
    }

    public final void setSelectedTrailer(String selectedTrailer) {
        this.selectedTrailer.set(selectedTrailer);
    }

    public final String getSelectedTrailer() {
        return selectedTrailer.get();
    }

    private final ObjectProperty<Movie> selectedMovie = new SimpleObjectProperty<>();

    public final ObjectProperty<Movie> selectedMovieProperty() {
        return selectedMovie;
    }

    public final void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie.set(selectedMovie);
    }

    public final Movie getSelectedMovie() {
        return selectedMovie.get();
    }

    private final ReadOnlyObjectWrapper<Predicate<Movie>> filterPredicate = new ReadOnlyObjectWrapper<>();

    public ReadOnlyObjectProperty<Predicate<Movie>> filterPredicateProperty() {
        return filterPredicate.getReadOnlyProperty();
    }

    private final StringProperty filterText = new SimpleStringProperty();

    private final BooleanProperty enableSortingAndFiltering = new SimpleBooleanProperty();

    private final BooleanProperty useCss = new SimpleBooleanProperty();

    private final BooleanProperty useCustomFonts = new SimpleBooleanProperty();

    private final BooleanProperty mediaViewTrailers = new SimpleBooleanProperty();

    private final BooleanProperty animateMediaViewTrailers = new SimpleBooleanProperty();

    private final BooleanProperty showMediaViewBackground = new SimpleBooleanProperty();

    private final BooleanProperty useListView = new SimpleBooleanProperty();

    private final BooleanProperty useListViewCellFactory = new SimpleBooleanProperty();

    private final BooleanProperty useControlsFX = new SimpleBooleanProperty();

    private final BooleanProperty enableDragAndDropOfPoster = new SimpleBooleanProperty();

    private final BooleanProperty enableDragAndDropOfPosterWithDragImage = new SimpleBooleanProperty();

    private final BooleanProperty enableEffects = new SimpleBooleanProperty();

    private final BooleanProperty useClipping = new SimpleBooleanProperty();

    public boolean isUseClipping() {
        return useClipping.get();
    }

    public BooleanProperty useClippingProperty() {
        return useClipping;
    }

    public void setUseClipping(boolean useClipping) {
        this.useClipping.set(useClipping);
    }

    public boolean isEnableSortingAndFiltering() {

        return enableSortingAndFiltering.get();
    }

    public BooleanProperty enableSortingAndFilteringProperty() {
        return enableSortingAndFiltering;
    }

    public void setEnableSortingAndFiltering(boolean enableSortingAndFiltering) {
        this.enableSortingAndFiltering.set(enableSortingAndFiltering);
    }

    public boolean isUseCss() {
        return useCss.get();
    }

    public BooleanProperty useCssProperty() {
        return useCss;
    }

    public void setUseCss(boolean useCss) {
        this.useCss.set(useCss);
    }

    public boolean isMediaViewTrailers() {
        return mediaViewTrailers.get();
    }

    public BooleanProperty mediaViewTrailersProperty() {
        return mediaViewTrailers;
    }

    public void setMediaViewTrailers(boolean mediaViewTrailers) {
        this.mediaViewTrailers.set(mediaViewTrailers);
    }

    public boolean isAnimateMediaViewTrailers() {
        return animateMediaViewTrailers.get();
    }

    public BooleanProperty animateMediaViewTrailersProperty() {
        return animateMediaViewTrailers;
    }

    public void setAnimateMediaViewTrailers(boolean animateMediaViewTrailers) {
        this.animateMediaViewTrailers.set(animateMediaViewTrailers);
    }

    public boolean isShowMediaViewBackground() {
        return showMediaViewBackground.get();
    }

    public BooleanProperty showMediaViewBackgroundProperty() {
        return showMediaViewBackground;
    }

    public void setShowMediaViewBackground(boolean showMediaViewBackground) {
        this.showMediaViewBackground.set(showMediaViewBackground);
    }

    public boolean isUseListView() {
        return useListView.get();
    }

    public BooleanProperty useListViewProperty() {
        return useListView;
    }

    public void setUseListView(boolean useListView) {
        this.useListView.set(useListView);
    }

    public boolean isUseListViewCellFactory() {
        return useListViewCellFactory.get();
    }

    public BooleanProperty useListViewCellFactoryProperty() {
        return useListViewCellFactory;
    }

    public void setUseListViewCellFactory(boolean useListViewCellFactory) {
        this.useListViewCellFactory.set(useListViewCellFactory);
    }

    public boolean isUseControlsFX() {
        return useControlsFX.get();
    }

    public BooleanProperty useControlsFXProperty() {
        return useControlsFX;
    }

    public void setUseControlsFX(boolean useControlsFX) {
        this.useControlsFX.set(useControlsFX);
    }

    public boolean isEnableDragAndDropOfPoster() {
        return enableDragAndDropOfPoster.get();
    }

    public BooleanProperty enableDragAndDropOfPosterProperty() {
        return enableDragAndDropOfPoster;
    }

    public void setEnableDragAndDropOfPoster(boolean enableDragAndDropOfPoster) {
        this.enableDragAndDropOfPoster.set(enableDragAndDropOfPoster);
    }

    public boolean isEnableDragAndDropOfPosterWithDragImage() {
        return enableDragAndDropOfPosterWithDragImage.get();
    }

    public BooleanProperty enableDragAndDropOfPosterWithDragImageProperty() {
        return enableDragAndDropOfPosterWithDragImage;
    }

    public void setEnableDragAndDropOfPosterWithDragImage(boolean enableDragAndDropOfPosterWithDragImage) {
        this.enableDragAndDropOfPosterWithDragImage.set(enableDragAndDropOfPosterWithDragImage);
    }

    public boolean isUseCustomFonts() {
        return useCustomFonts.get();
    }

    public BooleanProperty useCustomFontsProperty() {
        return useCustomFonts;
    }

    public void setUseCustomFonts(boolean useCustomFonts) {
        this.useCustomFonts.set(useCustomFonts);
    }

    public String getFilterText() {
        return filterText.get();
    }

    public StringProperty filterTextProperty() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText.set(filterText);
    }

    public boolean isEnableEffects() {
        return enableEffects.get();
    }

    public BooleanProperty enableEffectsProperty() {
        return enableEffects;
    }

    public void setEnableEffects(boolean enableEffects) {
        this.enableEffects.set(enableEffects);
    }

    private final BooleanProperty usePrettyListView = new SimpleBooleanProperty();

    public boolean isUsePrettyListView() {
        return usePrettyListView.get();
    }

    public BooleanProperty usePrettyListViewProperty() {
        return usePrettyListView;
    }

    public void setUsePrettyListView(boolean usePrettyListView) {
        this.usePrettyListView.set(usePrettyListView);
    }

    private void updateOptionalStylesheet() {
        if (isUseCss()) {
            if (!getStylesheets().contains(optionalStylesheet)) {
                getStylesheets().add(optionalStylesheet);
            }
        } else {
            getStylesheets().remove(optionalStylesheet);
        }

        applyCss();
    }

    private void updateFontStylesheet() {
        if (isUseCustomFonts()) {
            if (!getStylesheets().contains(fontsStylesheet)) {
                getStylesheets().add(fontsStylesheet);
            }
        } else {
            getStylesheets().remove(fontsStylesheet);
        }

        applyCss();
    }

    private void updateListViewStylesheet() {
        if (isUsePrettyListView()) {
            if (!getStylesheets().contains(listViewStylesheet)) {
                getStylesheets().add(listViewStylesheet);
            }
        } else {
            getStylesheets().remove(listViewStylesheet);
        }

        applyCss();
    }
}
