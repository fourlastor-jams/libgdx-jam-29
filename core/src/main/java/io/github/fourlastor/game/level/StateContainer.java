package io.github.fourlastor.game.level;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class StateContainer {
    private State state;
    private final List<Consumer<State>> observers = new LinkedList<>();

    public StateContainer(State state) {
        this.state = state;
    }

    public State current() {
        return state;
    }

    void update(Function<State, State> update) {
        this.state = update.apply(state);
        for (Consumer<State> observer : observers) {
            observer.accept(state);
        }
    }

    void listen(Consumer<State> consumer) {
        observers.add(consumer);
        consumer.accept(state);
    }
}
