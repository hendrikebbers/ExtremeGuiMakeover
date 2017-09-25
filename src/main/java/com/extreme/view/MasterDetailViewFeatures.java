package com.extreme.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class MasterDetailViewFeatures {

    private final BooleanProperty useCss = new SimpleBooleanProperty(false);

    private final BooleanProperty movieBackground = new SimpleBooleanProperty(false);

    private final BooleanProperty listAnimation = new SimpleBooleanProperty(false);

    private final BooleanProperty listShadow = new SimpleBooleanProperty(false);

    private final BooleanProperty listCache = new SimpleBooleanProperty(false);

    private final BooleanProperty customWindowUI = new SimpleBooleanProperty(false);

    private final BooleanProperty customWindowClip = new SimpleBooleanProperty(false);

    private final BooleanProperty posterTransform = new SimpleBooleanProperty(false);

    public boolean isUseCss() {
        return useCss.get();
    }

    public BooleanProperty useCssProperty() {
        return useCss;
    }

    public void setUseCss(boolean useCss) {
        this.useCss.set(useCss);
    }

    public boolean isMovieBackground() {
        return movieBackground.get();
    }

    public BooleanProperty movieBackgroundProperty() {
        return movieBackground;
    }

    public void setMovieBackground(boolean movieBackground) {
        this.movieBackground.set(movieBackground);
    }

    public boolean isListAnimation() {
        return listAnimation.get();
    }

    public BooleanProperty listAnimationProperty() {
        return listAnimation;
    }

    public void setListAnimation(boolean listAnimation) {
        this.listAnimation.set(listAnimation);
    }

    public boolean isListShadow() {
        return listShadow.get();
    }

    public BooleanProperty listShadowProperty() {
        return listShadow;
    }

    public void setListShadow(boolean listShadow) {
        this.listShadow.set(listShadow);
    }

    public boolean isListCache() {
        return listCache.get();
    }

    public BooleanProperty listCacheProperty() {
        return listCache;
    }

    public void setListCache(boolean listCache) {
        this.listCache.set(listCache);
    }

    public boolean isCustomWindowUI() {
        return customWindowUI.get();
    }

    public BooleanProperty customWindowUIProperty() {
        return customWindowUI;
    }

    public void setCustomWindowUI(boolean customWindowUI) {
        this.customWindowUI.set(customWindowUI);
    }

    public boolean isCustomWindowClip() {
        return customWindowClip.get();
    }

    public BooleanProperty customWindowClipProperty() {
        return customWindowClip;
    }

    public void setCustomWindowClip(boolean customWindowClip) {
        this.customWindowClip.set(customWindowClip);
    }

    public boolean isPosterTransform() {
        return posterTransform.get();
    }

    public BooleanProperty posterTransformProperty() {
        return posterTransform;
    }

    public void setPosterTransform(boolean posterTransform) {
        this.posterTransform.set(posterTransform);
    }
}
