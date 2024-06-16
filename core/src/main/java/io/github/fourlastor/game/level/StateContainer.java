package io.github.fourlastor.game.level;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class StateContainer implements Listenable {
    private State state;
    private final List<Consumer<State>> observers = new LinkedList<>();

    public StateContainer(State state) {
        this.state = state;
    }

    public State current() {
        return state;
    }

    public void update(Function<State, State> update) {
        this.state = update.apply(state);
        for (Consumer<State> observer : observers) {
            observer.accept(state);
        }
    }

    public <T> Listenable distinct(Function<State, T> extractor) {
        return new DistinctConsumer<>(extractor, this);
    }

    @Override
    public void listen(Consumer<State> consumer) {
        observers.add(consumer);
        consumer.accept(state);
    }

    private static class DistinctConsumer<T> implements Listenable {

        private final Function<State, T> extractor;
        private final StateContainer container;
        private T current;

        public DistinctConsumer(Function<State, T> extractor, StateContainer container) {
            this.extractor = extractor;
            this.container = container;
        }

        @Override
        public void listen(Consumer<State> consumer) {
            container.listen(state -> {
                T newState = extractor.apply(state);
                if (current == null || !current.equals(newState)) {
                    current = newState;
                    consumer.accept(state);
                }
            });
        }
    }
}
