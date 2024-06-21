package io.github.fourlastor.game.level.state.update;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.level.state.Character;
import io.github.fourlastor.game.level.state.State;

public class ChargeBattery extends AdvancesTime {

    private final Character.Name name;

    @AssistedInject
    public ChargeBattery(Dependencies dependencies, @Assisted Character.Name name) {
        super(dependencies);
        this.name = name;
    }

    @Override
    public State apply(State state) {
        return super.apply(state)
                .builder()
                .character(name, state, it -> it.builder().cycling(true).build())
                .updateBattery(100)
                .build();
    }

    @AssistedFactory
    public interface Factory {
        ChargeBattery create(Character.Name character);
    }
}
