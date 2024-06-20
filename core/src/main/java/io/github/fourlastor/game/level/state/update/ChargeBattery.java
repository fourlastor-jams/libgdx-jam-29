package io.github.fourlastor.game.level.state.update;

import io.github.fourlastor.game.level.state.State;
import javax.inject.Inject;

public class ChargeBattery extends AdvancesTime {

    @Inject
    public ChargeBattery(Dependencies dependencies) {
        super(dependencies);
    }

    @Override
    public State apply(State state) {
        return super.apply(state).builder().updateBattery(100).build();
    }
}
