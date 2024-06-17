package io.github.fourlastor.game.level.state.update;

import com.badlogic.gdx.math.MathUtils;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.state.Progress;
import io.github.fourlastor.game.level.state.State;

public class IncreaseProgress extends AdvancesTime {

    private final float progress;
    private final Progress.Type type;

    @AssistedInject
    public IncreaseProgress(Dependencies dependencies, @Assisted float progress, @Assisted Progress.Type type) {
        super(dependencies);
        this.progress = progress;
        this.type = type;
    }

    @Override
    public State.Builder update(State state) {
        Progress currentProgress = state.progress();
        float total = MathUtils.clamp(currentProgress.total() + progress, 0, 1);
        Progress.Builder builder = currentProgress.builder().total(total);
        switch (type) {
            case ART:
                builder = builder.art(currentProgress.art() + 30);
                break;
            case TECH:
                builder = builder.tech(currentProgress.tech() + 30);
                break;
            case STORY:
                builder = builder.story(currentProgress.story() + 30);
                break;
            case MECH:
                builder = builder.mech(currentProgress.mech() + 30);
                break;
        }
        Progress newProgress = builder.build();
        return super.update(state).updateBattery(state.battery() - 30).progress(newProgress);
    }

    @AssistedFactory
    public interface Factory {
        IncreaseProgress create(float progress, Progress.Type type);
    }
}
