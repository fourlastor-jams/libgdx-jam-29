package io.github.fourlastor.game.end;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import javax.inject.Inject;

public class EndScreen extends ScreenAdapter {

    public static Color CLEAR_COLOR = new Color(0x333333ff);

    private final InputMultiplexer inputMultiplexer;
    private final Stage stage;
    private final Viewport viewport;

    @Inject
    public EndScreen(InputMultiplexer inputMultiplexer, AssetManager assetManager, EndState endState) {
        this.inputMultiplexer = inputMultiplexer;

        Gdx.app.log("EndState", endState.name());
        viewport = new FitViewport(256, 144);
        stage = new Stage(viewport);
    }

    @Override
    public void show() {
        inputMultiplexer.addProcessor(processor);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(processor);
    }

    private final InputProcessor processor = new InputAdapter() {
        @Override
        public boolean keyUp(int keycode) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return true;
        }
    };

    @Override
    public void render(float delta) {
        ScreenUtils.clear(CLEAR_COLOR, true);
        stage.act(delta);
        stage.draw();
    }
}
