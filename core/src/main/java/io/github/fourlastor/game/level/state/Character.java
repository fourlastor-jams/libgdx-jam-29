package io.github.fourlastor.game.level.state;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Character {
    public abstract Builder builder();

    public abstract int stress();

    public abstract boolean kidnapped();

    public abstract Name name();

    public static Character initial(Name name) {
        return new AutoValue_Character.Builder()
                .name(name)
                .stress(0)
                .kidnapped(false)
                .build();
    }

    public enum Name {
        RAELEUS("raeleus"),
        LYZE("lyze"),
        DRAGON_QUEEN("dragon_queen"),
        PANDA("peanut_panda");

        public final String folder;

        Name(String folder) {
            this.folder = folder;
        }
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Character.Builder stress(int value);

        public abstract Character.Builder kidnapped(boolean value);

        public abstract Character.Builder name(Character.Name value);

        public abstract Character build();
    }
}
