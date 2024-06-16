package io.github.fourlastor.game.level.state.update;

import com.github.tommyettinger.random.EnhancedRandom;
import io.github.fourlastor.game.level.state.State;
import io.github.fourlastor.game.level.state.Update;
import javax.inject.Inject;

public abstract class AdvancesTime extends Update {

    private final EnhancedRandom random;

    protected AdvancesTime(Dependencies dependencies) {
        random = dependencies.random;
    }

    @Override
    public State.Builder update(State state) {
        int tod = (state.tod() + 1) % 7;
        int day = tod == 0 ? state.day() + 1 : state.day();
        boolean deathAppeared = random.nextBoolean(1 - state.deathSafety());
        float deathSafety = deathAppeared ? 1f : state.deathSafety() * 0.9f;
        return state.builder()
                .updateBattery(state.battery() - 10)
                .day(day)
                .tod(tod)
                .deathSafety(deathSafety)
                .deathAppeared(deathAppeared);
    }

    public static class Dependencies {
        private final EnhancedRandom random;

        @Inject
        public Dependencies(EnhancedRandom random) {
            this.random = random;
        }
    }
}
