package io.github.fourlastor.game.level.state;

import com.badlogic.gdx.math.MathUtils;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class State {

    public abstract Builder builder();

    public abstract Progress progress();

    public abstract Character raeleus();

    public abstract Character lyze();

    public abstract int day();

    public abstract int tod();

    public abstract int battery();

    public abstract int deathSafety();

    public abstract boolean deathAppeared();

    public static State initial() {
        return new AutoValue_State.Builder()
                .tod(0)
                .day(0)
                .battery(100)
                .progress(Progress.initial())
                .deathSafety(100)
                .deathAppeared(false)
                .raeleus(Character.initial())
                .lyze(Character.initial())
                .build();
    }

    public boolean isGameWon() {
        return progress().total() == 1;
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

        public abstract Builder progress(Progress value);

        public abstract Builder deathSafety(int value);

        public abstract Builder deathAppeared(boolean value);

        public abstract Builder raeleus(Character value);

        public abstract Builder lyze(Character value);

        public abstract State build();
    }
}
