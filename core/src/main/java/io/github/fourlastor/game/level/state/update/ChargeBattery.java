package io.github.fourlastor.game.level.state.update;

import io.github.fourlastor.game.level.state.State;
import javax.inject.Inject;

public class ChargeBattery extends AdvancesTime {

    @Inject
    public ChargeBattery() {}

    @Override
    public State.Builder update(State state) {
        return super.update(state).updateBattery(100);
    }
}
