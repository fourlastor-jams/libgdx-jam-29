package io.github.fourlastor.game.level.state;

import java.util.function.Function;

public abstract class Update implements Function<State, State> {

    public abstract State.Builder update(State state);

    @Override
    public State apply(State state) {
        return update(state).build();
    }
}
