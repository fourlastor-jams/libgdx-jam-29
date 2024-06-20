package io.github.fourlastor.game.level.ui;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.ui.AnimatedImage;

public class CharacterImage extends AnimatedImage {
    private final Animation<? extends Drawable> idle0;
    private final Animation<? extends Drawable> idle25;
    private final Animation<? extends Drawable> idle50;
    private final Animation<? extends Drawable> idle100;

    private int tier = 0;

    public CharacterImage(
            Animation<? extends Drawable> idle0,
            Animation<? extends Drawable> idle25,
            Animation<? extends Drawable> idle50,
            Animation<? extends Drawable> idle100) {
        super(idle0);
        this.idle0 = idle0;
        this.idle25 = idle25;
        this.idle50 = idle50;
        this.idle100 = idle100;
    }

    public void updateStress(int stress) {
        int newTier;
        if (stress < 25) {
            newTier = 0;
        } else if (stress < 50) {
            newTier = 1;
        } else if (stress < 80) {
            newTier = 2;
        } else {
            newTier = 3;
        }
        if (tier == newTier) {
            return;
        }
        tier = newTier;
        switch (tier) {
            case 0:
                setAnimation(idle0);
                break;
            case 1:
                setAnimation(idle25);
                break;
            case 2:
                setAnimation(idle50);
                break;
            case 3:
                setAnimation(idle100);
                break;
        }
    }
}
