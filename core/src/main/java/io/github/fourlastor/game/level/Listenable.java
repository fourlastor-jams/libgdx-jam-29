package io.github.fourlastor.game.level;

import java.util.function.Consumer;

public interface Listenable {
    void listen(Consumer<State> consumer);
}
