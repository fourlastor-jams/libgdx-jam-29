package io.github.fourlastor.game.level.state.update;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.state.Character;
import io.github.fourlastor.game.level.state.Progress;
import io.github.fourlastor.game.level.state.State;

public class IncreaseProgress extends AdvancesTime {

    private final float progress;
    private final Progress.Type type;
    private final Character.Name character;

    @AssistedInject
    public IncreaseProgress(
            Dependencies dependencies,
            @Assisted float progress,
            @Assisted Progress.Type type,
            @Assisted Character.Name character) {
        super(dependencies);
        this.progress = progress;
        this.type = type;
        this.character = character;
    }

    @Override
    public State apply(State state) {
        Progress currentProgress = state.progress();
        float total = currentProgress.total() + progress;
        Progress.Builder progressBuilder = currentProgress.builder().updateTotal(total);
        switch (type) {
            case ART:
                progressBuilder = progressBuilder.art(currentProgress.art() + 1);
                break;
            case TECH:
                progressBuilder = progressBuilder.tech(currentProgress.tech() + 1);
                break;
            case STORY:
                progressBuilder = progressBuilder.story(currentProgress.story() + 1);
                break;
            case MECH:
                progressBuilder = progressBuilder.mech(currentProgress.mech() + 1);
                break;
        }
        Progress newProgress = progressBuilder.build();
        return super.apply(state)
                .builder()
                .character(character, state, it -> it.builder()
                        .stress(it.stress() + 10)
                        .build())
                .updateBattery(state.battery() - 30)
                .progress(newProgress)
                .build();
    }

    @AssistedFactory
    public interface Factory {
        IncreaseProgress create(float progress, Progress.Type type, Character.Name character);
    }
}
