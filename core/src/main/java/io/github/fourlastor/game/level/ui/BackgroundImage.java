package io.github.fourlastor.game.level.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class BackgroundImage extends Image {

    private final Drawable bg0;
    private final Drawable bg25;
    private final Drawable bg50;
    private final Drawable bg100;

    public BackgroundImage(TextureRegion bg0, TextureRegion bg25, TextureRegion bg50, TextureRegion bg100) {
        super(bg0);
        this.bg0 = new TextureRegionDrawable(bg0);
        this.bg25 = new TextureRegionDrawable(bg25);
        this.bg50 = new TextureRegionDrawable(bg50);
        this.bg100 = new TextureRegionDrawable(bg100);
    }

    public void updateDamage(int damageLevel) {
        if (damageLevel < 25) {
            setDrawable(bg0);
        } else if (damageLevel < 50) {
            setDrawable(bg25);
        } else if (damageLevel < 100) {
            setDrawable(bg50);
        } else {
            setDrawable(bg100);
        }
    }
}
