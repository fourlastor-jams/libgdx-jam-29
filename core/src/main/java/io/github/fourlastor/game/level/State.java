package io.github.fourlastor.game.level;

import com.badlogic.gdx.math.MathUtils;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class State {

    protected abstract Builder builder();

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

    private Builder advanceTime() {
        int tod = (tod() + 1) % 7;
        int day = tod == 0 ? day() + 1 : day();
        return builder().updateBattery(battery() - 10).day(day).tod(tod);
    }

    public State increaseProgress(float progress) {
        return advanceTime()
                .updateBattery(battery() - 30)
                .progress(MathUtils.clamp(this.progress() + progress, 0, 1))
                .build();
    }

    public boolean isGameWon() {
        return progress() == 1;
    }

    public boolean enoughBattery(int required) {
        return battery() <= required;
    }

    public State chargeBattery() {
        return advanceTime().battery(100).build();
    }

    @AutoValue.Builder
    protected abstract static class Builder {
        abstract Builder day(int value);

        abstract Builder tod(int value);

        abstract Builder battery(int value);

        Builder updateBattery(int value) {
            return battery(MathUtils.clamp(value, 0, 100));
        }

        abstract Builder progress(float value);

        abstract State build();
    }
}
