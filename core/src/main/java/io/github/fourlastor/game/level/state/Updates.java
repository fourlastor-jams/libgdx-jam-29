package io.github.fourlastor.game.level.state;

import io.github.fourlastor.game.level.state.update.ChargeBattery;
import io.github.fourlastor.game.level.state.update.IncreaseProgress;
import javax.inject.Inject;

public class Updates {
    public final IncreaseProgress.Factory increaseProgress;
    public final ChargeBattery chargeBattery;

    @Inject
    public Updates(IncreaseProgress.Factory increaseProgress, ChargeBattery chargeBattery) {
        this.increaseProgress = increaseProgress;
        this.chargeBattery = chargeBattery;
    }
}
