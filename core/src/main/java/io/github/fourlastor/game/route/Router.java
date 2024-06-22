package io.github.fourlastor.game.route;

import io.github.fourlastor.game.end.EndState;

public interface Router {

    void goToGameEnd(EndState endState);

    void goToLevel();
}
