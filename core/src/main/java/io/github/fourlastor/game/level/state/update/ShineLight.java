package io.github.fourlastor.game.level.state.update;

import io.github.fourlastor.game.level.state.State;
import javax.inject.Inject;

public class ShineLight extends AdvancesTime {

    @Inject
    public ShineLight(Dependencies dependencies) {
        super(dependencies);
    }

    @Override
    public State.Builder update(State state) {
        return super.update(state).deathSafety(1f).deathAppeared(false).updateBattery(state.battery() - 30);
    }
}
