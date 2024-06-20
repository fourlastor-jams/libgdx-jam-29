package io.github.fourlastor.game.level.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ProgressBar extends Image {

    public ProgressBar(TextureRegion whitePixel) {
        this(whitePixel, new Color(0xf77622ff), 0f);
    }

    public ProgressBar(TextureRegion whitePixel, Color fg, float initialProgress) {
        super(whitePixel);
        setColor(fg);
        setScaleX(initialProgress);
    }

    public void setProgress(float progress) {
        addAction(Actions.scaleTo(progress, 1, 0.3f));
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
