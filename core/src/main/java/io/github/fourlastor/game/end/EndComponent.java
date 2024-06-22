package io.github.fourlastor.game.end;

import dagger.BindsInstance;
import dagger.Subcomponent;
import io.github.fourlastor.game.di.ScreenScoped;
import io.github.fourlastor.game.route.Router;

@ScreenScoped
@Subcomponent
public interface EndComponent {

    @ScreenScoped
    EndScreen screen();

    @Subcomponent.Builder
    interface Builder {

        Builder router(@BindsInstance Router router);

        Builder endState(@BindsInstance EndState state);

        EndComponent build();
    }
}
