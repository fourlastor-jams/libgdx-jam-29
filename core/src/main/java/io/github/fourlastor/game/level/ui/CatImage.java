package io.github.fourlastor.game.level.ui;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import io.github.fourlastor.game.level.state.CatStance;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.ui.AnimatedImage;

public class CatImage extends AnimatedImage {
    private final Animation<? extends Drawable> idleStanding;
    private final Animation<? extends Drawable> idleSitting;
    private final Animation<? extends Drawable> walking;
    private final Animation<? extends Drawable> hissing;

    private CatStance stance = CatStance.IDLE_STANDING;

    public CatImage(
            Animation<? extends Drawable> idleStanding,
            Animation<? extends Drawable> idleSitting,
            Animation<? extends Drawable> walking,
            Animation<? extends Drawable> hissing) {
        super(idleStanding);
        this.idleStanding = idleStanding;
        this.idleSitting = idleSitting;
        this.walking = walking;
        this.hissing = hissing;
    }

    public void updateStance(CatStance newStance) {
        if (newStance == stance) {
            return;
        }
        stance = newStance;

        switch (newStance) {
            case IDLE_STANDING:
                setAnimation(idleStanding);
                break;
            case IDLE_SITTING:
                setAnimation(idleSitting);
                break;
            case WALKING:
                setAnimation(walking);
                break;
            case HISSING:
                setAnimation(hissing);
                break;
        }
    }
}
