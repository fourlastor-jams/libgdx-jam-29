package io.github.fourlastor.game.level.state.update;

import com.badlogic.gdx.math.MathUtils;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.state.State;

public class IncreaseProgress extends AdvancesTime {

    private final float progress;

    @AssistedInject
    public IncreaseProgress(@Assisted float progress) {
        this.progress = progress;
    }

    @Override
    public State.Builder update(State state) {
        return super.update(state)
                .updateBattery(state.battery() - 30)
                .progress(MathUtils.clamp(state.progress() + progress, 0, 1));
    }

    @AssistedFactory
    public interface Factory {
        IncreaseProgress create(float progress);
    }
}
