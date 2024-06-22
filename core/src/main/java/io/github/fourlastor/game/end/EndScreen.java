package io.github.fourlastor.game.end;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.fourlastor.game.route.Router;
import javax.inject.Inject;

public class EndScreen extends ScreenAdapter {

    public static Color CLEAR_COLOR = new Color(0x333333ff);

    private final Router router;
    private final Stage stage;
    private final Viewport viewport;
    private final BitmapFont font;

    @Inject
    public EndScreen(EndState endState, Router router) {
        this.router = router;
        Gdx.app.log("EndState", endState.name());
        viewport = new FitViewport(256, 144);
        stage = new Stage(viewport);
        font = new BitmapFont(Gdx.files.internal("fonts/quan-pixel-16.fnt"));
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        String endMessage = endState == EndState.WON ? "You won" : "You lost";
        Label wonLost = new Label(endMessage + "\nPress R to restart", style);
        wonLost.setAlignment(Align.center);
        wonLost.setPosition(stage.getWidth() / 2f, stage.getHeight() / 2f, Align.center);
        stage.addActor(wonLost);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(CLEAR_COLOR, true);
        stage.act(delta);
        stage.draw();
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            router.goToLevel();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
        font.dispose();
    }
}
