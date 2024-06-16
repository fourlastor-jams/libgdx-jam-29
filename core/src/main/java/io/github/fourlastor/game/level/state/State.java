package io.github.fourlastor.game.level.state;

import com.badlogic.gdx.math.MathUtils;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class State {

    public abstract Builder builder();

    public abstract int day();

    public abstract int tod();

    public abstract int battery();

    public abstract float progress();

    public static State initial() {
        return new AutoValue_State.Builder()
                .tod(0)
                .day(0)
                .battery(100)
                .progress(0)
                .build();
    }

    public boolean isGameWon() {
        return progress() == 1;
    }

    public boolean enoughBattery(int required) {
        return battery() <= required;
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder day(int value);

        public abstract Builder tod(int value);

        public abstract Builder battery(int value);

        public Builder updateBattery(int value) {
            return battery(MathUtils.clamp(value, 0, 100));
        }

        public abstract Builder progress(float value);

        public abstract State build();
    }
}
