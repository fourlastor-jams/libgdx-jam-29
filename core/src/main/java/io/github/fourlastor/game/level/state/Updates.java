package io.github.fourlastor.game.level.state;

import io.github.fourlastor.game.level.state.update.ChargeBattery;
import io.github.fourlastor.game.level.state.update.IncreaseProgress;
import io.github.fourlastor.game.level.state.update.PetCat;
import io.github.fourlastor.game.level.state.update.ShineLight;
import javax.inject.Inject;

public class Updates {
    public final IncreaseProgress.Factory increaseProgress;
    public final PetCat.Factory petCat;
    public final ChargeBattery chargeBattery;
    public final ShineLight shineLight;

    @Inject
    public Updates(
            IncreaseProgress.Factory increaseProgress,
            PetCat.Factory petCat,
            ChargeBattery chargeBattery,
            ShineLight shineLight) {
        this.increaseProgress = increaseProgress;
        this.petCat = petCat;
        this.chargeBattery = chargeBattery;
        this.shineLight = shineLight;
    }
}
