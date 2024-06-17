package io.github.fourlastor.game.level.state.update;

import io.github.fourlastor.game.level.state.State;
import javax.inject.Inject;

public class ShineLight extends AdvancesTime {

    @Inject
    public ShineLight(Dependencies dependencies) {
        super(dependencies);
    }

    @Override
    public State apply(State state) {
        return super.apply(state)
                .builder()
                .deathSafety(100)
                .deathAppeared(false)
                .updateBattery(state.battery() - 30)
                .build();
    }
}
