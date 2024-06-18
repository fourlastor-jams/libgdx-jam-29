package io.github.fourlastor.game.level.state.update;

import com.badlogic.gdx.math.MathUtils;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.state.Character;
import io.github.fourlastor.game.level.state.State;

public class PetCat extends AdvancesTime {

    private final Character.Name character;

    @AssistedInject
    public PetCat(Dependencies dependencies, @Assisted Character.Name character) {
        super(dependencies);
        this.character = character;
    }

    @Override
    public State apply(State state) {
        State.Builder builder = super.apply(state).builder();

        switch (character) {
            case RAELEUS:
                Character raeleus = state.raeleus();
                builder = builder.raeleus(raeleus.builder()
                        .stress(MathUtils.clamp(raeleus.stress() - 20, 0, 100))
                        .build());
                break;
            case LYZE:
                Character lyze = state.lyze();
                builder = builder.lyze(lyze.builder()
                        .stress(MathUtils.clamp(lyze.stress() - 20, 0, 100))
                        .build());
                break;
        }
        return builder.build();
    }

    @AssistedFactory
    public interface Factory {
        PetCat create(Character.Name character);
    }
}
