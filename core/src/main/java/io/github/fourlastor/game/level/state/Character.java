package io.github.fourlastor.game.level.state;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Character {
    public abstract Builder builder();

    public abstract int stress();

    public abstract boolean kidnapped();

    public abstract boolean cycling();

    public abstract Name name();

    public static Character initial(Name name) {
        return new AutoValue_Character.Builder()
                .name(name)
                .stress(0)
                .kidnapped(false)
                .cycling(false)
                .build();
    }

    public static final Vector2 IDLE_SIZE = new Vector2(30, 75);

    public enum Name {
        RAELEUS("raeleus", new Vector2(228, 14), new Rectangle(372, 17, 56, 81)),
        LYZE("lyze", new Vector2(192, 14), new Rectangle(372, 18, 65, 80)),
        DRAGON_QUEEN("dragon_queen", new Vector2(152, 14), new Rectangle(372, 21, 45, 77)),
        PANDA("peanut_panda", new Vector2(121, 14), new Rectangle(372, 14, 55, 84));

        public final String folder;
        public final Vector2 idlePos;
        public final Rectangle cyclePos;

        Name(String folder, Vector2 idlePos, Rectangle cyclePos) {
            this.folder = folder;
            this.idlePos = idlePos;
            this.cyclePos = cyclePos;
        }
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Character.Builder stress(int value);

        public abstract Character.Builder kidnapped(boolean value);

        public abstract Character.Builder cycling(boolean value);

        public abstract Character.Builder name(Character.Name value);

        public abstract Character build();
    }
}
