package io.github.fourlastor.game.level.state.update;

import io.github.fourlastor.game.level.state.State;
import io.github.fourlastor.game.level.state.Update;

public abstract class AdvancesTime extends Update {

    @Override
    public State.Builder update(State state) {
        int tod = (state.tod() + 1) % 7;
        int day = tod == 0 ? state.day() + 1 : state.day();
        return state.builder().updateBattery(state.battery() - 10).day(day).tod(tod);
    }
}
