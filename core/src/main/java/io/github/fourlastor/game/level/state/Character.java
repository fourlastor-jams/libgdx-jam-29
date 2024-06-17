package io.github.fourlastor.game.level.state;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Character {
    public abstract Builder builder();

    public abstract int stress();

    public abstract boolean kidnapped();

    public static Character initial() {
        return new AutoValue_Character.Builder().stress(0).kidnapped(false).build();
    }

    public enum Type {
        RAELEUS,
        LYZE,
    }

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Character.Builder stress(int value);

        public abstract Character.Builder kidnapped(boolean value);

        public abstract Character build();
    }
}
