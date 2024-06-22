package io.github.fourlastor.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.github.tommyettinger.ds.ObjectList;
import io.github.fourlastor.game.di.GameComponent;
import io.github.fourlastor.game.end.EndComponent;
import io.github.fourlastor.game.end.EndState;
import io.github.fourlastor.game.level.di.LevelComponent;
import io.github.fourlastor.game.route.Router;
import io.github.fourlastor.harlequin.Harlequin;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GdxGame extends Game implements Router {

    private final InputMultiplexer multiplexer;

    private final LevelComponent.Builder levelScreenFactory;
    private final EndComponent.Builder gameEndScreenFactory;
    private Screen pendingScreen = null;

    @Inject
    public GdxGame(
            InputMultiplexer multiplexer,
            LevelComponent.Builder levelScreenFactory,
            EndComponent.Builder gameEndScreenFactory) {
        this.multiplexer = multiplexer;
        this.levelScreenFactory = levelScreenFactory;
        this.gameEndScreenFactory = gameEndScreenFactory;
        Harlequin.LIST_CREATOR = new Harlequin.ListCreator() {
            @Override
            public <T> List<T> newList() {
                return new ObjectList<>();
            }

            @Override
            public <T> List<T> newList(int size) {
                return new ObjectList<>(size);
            }
        };
    }

    @Override
    public void create() {
        //        if (Gdx.app.getType() != Application.ApplicationType.Android) {
        //
        //            Cursor customCursor =
        //                    Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("images/included/whitePixel.png")),
        // 0, 0);
        //            Gdx.graphics.setCursor(customCursor);
        //        }
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.input.setInputProcessor(multiplexer);
        goToLevel();
    }

    @Override
    public void render() {
        if (pendingScreen != null) {
            Screen currentScreen = getScreen();
            setScreen(pendingScreen);
            if (currentScreen != null) {
                currentScreen.dispose();
            }
            pendingScreen = null;
        }
        super.render();
    }

    public static GdxGame createGame() {
        return GameComponent.component().game();
    }

    @Override
    public void goToGameEnd(EndState endState) {
        pendingScreen =
                gameEndScreenFactory.router(this).endState(endState).build().screen();
    }

    @Override
    public void goToLevel() {
        pendingScreen = levelScreenFactory.router(this).build().screen();
    }
}
