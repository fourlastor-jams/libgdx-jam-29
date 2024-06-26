package io.github.fourlastor.game.level.state.update;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.github.tommyettinger.random.EnhancedRandom;
import io.github.fourlastor.game.level.state.CatStance;
import io.github.fourlastor.game.level.state.Character;
import io.github.fourlastor.game.level.state.Progress;
import io.github.fourlastor.game.level.state.State;
import io.github.fourlastor.game.level.state.Update;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;

public abstract class AdvancesTime extends Update {

    private static final String TAG = "AdvancesTime";
    private final EnhancedRandom random;

    protected AdvancesTime(Dependencies dependencies) {
        random = dependencies.random;
    }

    @Override
    public State apply(State state) {
        int tod = (state.tod() + 1) % 7;
        int day = tod == 0 ? state.day() + 1 : state.day();
        boolean deathAppeared = state.deathSafety() == 0;
        int deathSafety = deathAppeared ? 100 : MathUtils.clamp(state.deathSafety() - 20, 0, 100);
        State.Builder builder = state.builder()
                .updateBattery(state.battery() - 10)
                .day(day)
                .tod(tod)
                .deathSafety(deathSafety)
                .deathAppeared(deathAppeared);
        List<Character> availableCharacters = state.availableCharacters();
        for (Character character : availableCharacters) {
            builder = builder.character(
                    character.name(), state, it -> it.builder().cycling(false).build());
        }
        if (deathAppeared && !availableCharacters.isEmpty()) {
            Character character = random.randomElement(availableCharacters);
            builder = builder.character(
                    character.name(), state, it -> it.builder().kidnapped(true).build());
        }
        for (Character character : availableCharacters) {
            int stress = character.stress();
            Gdx.app.debug(TAG, character.name() + " is stressed: " + character.stress());
            float chance = stress / 100f;
            Gdx.app.debug(TAG, "Chance: " + chance);
            if (stress > 50 && random.nextBoolean(chance)) {
                List<Character> otherCharacters = availableCharacters.stream()
                        .filter(it -> it.name() != character.name())
                        .collect(Collectors.toList());
                if (!otherCharacters.isEmpty() && random.nextBoolean()) {
                    Character other = random.randomElement(otherCharacters);
                    Gdx.app.debug(TAG, "Stressing " + other.name());
                    builder = builder.character(other.name(), state, it -> it.builder()
                            .stress(it.stress() + 5)
                            .build());
                } else {
                    Gdx.app.debug(TAG, "Reducing progress..");
                    Progress progress = state.progress();
                    builder = builder.progress(progress.builder()
                            .updateTotal(progress.total() - 0.1f)
                            .build());
                }
            }
        }
        return builder.catStance(random.randomElement(CatStance.values())).build();
    }

    public static class Dependencies {
        private final EnhancedRandom random;

        @Inject
        public Dependencies(EnhancedRandom random) {
            this.random = random;
        }
    }
}
