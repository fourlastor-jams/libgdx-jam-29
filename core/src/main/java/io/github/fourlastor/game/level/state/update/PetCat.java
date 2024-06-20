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
        return super.apply(state)
                .builder()
                .character(character, state, it -> it.builder()
                        .stress(MathUtils.clamp(it.stress() - 20, 0, 100))
                        .build())
                .build();
    }

    @AssistedFactory
    public interface Factory {
        PetCat create(Character.Name character);
    }
}
