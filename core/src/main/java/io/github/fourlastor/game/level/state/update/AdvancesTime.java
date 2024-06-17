package io.github.fourlastor.game.level.state.update;

import com.badlogic.gdx.math.MathUtils;
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
        boolean deathAppeared = state.deathSafety() == 0;
        int deathSafety = deathAppeared ? 100 : MathUtils.clamp(state.deathSafety() - 20, 0, 100);
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
