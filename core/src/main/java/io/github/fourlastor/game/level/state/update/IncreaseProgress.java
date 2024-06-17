package io.github.fourlastor.game.level.state.update;

import com.badlogic.gdx.math.MathUtils;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.state.Character;
import io.github.fourlastor.game.level.state.Progress;
import io.github.fourlastor.game.level.state.State;

public class IncreaseProgress extends AdvancesTime {

    private final float progress;
    private final Progress.Type type;
    private final Character.Type character;

    @AssistedInject
    public IncreaseProgress(
            Dependencies dependencies,
            @Assisted float progress,
            @Assisted Progress.Type type,
            @Assisted Character.Type character) {
        super(dependencies);
        this.progress = progress;
        this.type = type;
        this.character = character;
    }

    @Override
    public State.Builder update(State state) {
        Progress currentProgress = state.progress();
        float total = MathUtils.clamp(currentProgress.total() + progress, 0, 1);
        Progress.Builder progressBuilder = currentProgress.builder().total(total);
        switch (type) {
            case ART:
                progressBuilder = progressBuilder.art(currentProgress.art() + 30);
                break;
            case TECH:
                progressBuilder = progressBuilder.tech(currentProgress.tech() + 30);
                break;
            case STORY:
                progressBuilder = progressBuilder.story(currentProgress.story() + 30);
                break;
            case MECH:
                progressBuilder = progressBuilder.mech(currentProgress.mech() + 30);
                break;
        }
        Progress newProgress = progressBuilder.build();
        State.Builder builder = super.update(state);

        switch (character) {
            case RAELEUS:
                Character raeleus = state.raeleus();
                builder = builder.raeleus(
                        raeleus.builder().stress(raeleus.stress() + 10).build());
                break;
            case LYZE:
                Character lyze = state.lyze();
                builder = builder.lyze(lyze.builder().stress(lyze.stress() + 10).build());
                break;
        }
        return builder.updateBattery(state.battery() - 30).progress(newProgress);
    }

    @AssistedFactory
    public interface Factory {
        IncreaseProgress create(float progress, Progress.Type type, Character.Type character);
    }
}
