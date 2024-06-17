package io.github.fourlastor.game.level.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public class ProgressBar extends WidgetGroup {

    private final Image fgProgress;

    public ProgressBar(TextureRegion whitePixel) {
        this(whitePixel, Color.CORAL);
    }

    public ProgressBar(TextureRegion whitePixel, Color fg) {
        super();
        Image bgProgress = new Image(whitePixel);
        bgProgress.setColor(Color.LIGHT_GRAY);
        bgProgress.setFillParent(true);
        fgProgress = new Image(whitePixel);
        fgProgress.setColor(fg);
        fgProgress.setFillParent(true);
        fgProgress.setScaleX(0f);
        addActor(bgProgress);
        addActor(fgProgress);
    }

    public void setProgress(float progress) {
        fgProgress.addAction(Actions.scaleTo(progress, 1, 0.3f));
    }

    @Override
    public float getPrefHeight() {
        return getHeight();
    }

    @Override
    public float getPrefWidth() {
        return getWidth();
    }
}
