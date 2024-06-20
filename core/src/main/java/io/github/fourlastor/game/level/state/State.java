package io.github.fourlastor.game.level.state;

import com.badlogic.gdx.math.MathUtils;
import com.google.auto.value.AutoValue;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AutoValue
public abstract class State {

    public abstract Builder builder();

    public abstract Progress progress();

    public abstract Character raeleus();

    public abstract Character lyze();

    public abstract Character dragonQueen();

    public abstract Character panda();

    public abstract int day();

    public abstract int tod();

    public abstract int battery();

    public abstract int deathSafety();

    public abstract boolean deathAppeared();

    public List<Character> availableCharacters() {
        return Stream.of(raeleus(), lyze(), dragonQueen(), panda())
                .filter(it -> !it.kidnapped())
                .collect(Collectors.toList());
    }

    public static State initial() {
        return new AutoValue_State.Builder()
                .tod(0)
                .day(0)
                .battery(100)
                .progress(Progress.initial())
                .deathSafety(100)
                .deathAppeared(false)
                .raeleus(Character.initial(Character.Name.RAELEUS))
                .lyze(Character.initial(Character.Name.LYZE))
                .dragonQueen(Character.initial(Character.Name.DRAGON_QUEEN))
                .panda(Character.initial(Character.Name.PANDA))
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

        public Builder character(Character.Name name, State state, Function<Character, Character> update) {
            switch (name) {
                case RAELEUS:
                    return raeleus(update.apply(state.raeleus()));
                case LYZE:
                    return lyze(update.apply(state.lyze()));
                case DRAGON_QUEEN:
                    return dragonQueen(update.apply(state.dragonQueen()));
                case PANDA:
                    return panda(update.apply(state.panda()));
            }
            throw new IllegalArgumentException("Character " + name + " is unmanaged");
        }

        public abstract Builder raeleus(Character value);

        public abstract Builder lyze(Character value);

        public abstract Builder dragonQueen(Character value);

        public abstract Builder panda(Character value);

        public abstract State build();
    }
}
