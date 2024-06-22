package io.github.fourlastor.game.di.modules;

import dagger.Module;
import io.github.fourlastor.game.end.EndComponent;
import io.github.fourlastor.game.level.di.LevelComponent;

@Module(
        subcomponents = {
            LevelComponent.class,
            EndComponent.class,
        })
public class ScreensModule {}
