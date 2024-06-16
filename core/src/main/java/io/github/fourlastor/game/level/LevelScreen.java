package io.github.fourlastor.game.level;

import static io.github.fourlastor.game.di.modules.AssetsModule.WHITE_PIXEL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.random.EnhancedRandom;
import io.github.fourlastor.game.level.state.State;
import io.github.fourlastor.game.level.state.StateContainer;
import io.github.fourlastor.game.level.state.Updates;
import javax.inject.Inject;
import javax.inject.Named;

public class LevelScreen extends ScreenAdapter {

    public static Color CLEAR_COLOR = Color.BLACK;

    private final InputMultiplexer inputMultiplexer;
    private final Viewport viewport;
    private final Stage stage;
    private final StateContainer container;

    @Inject
    public LevelScreen(
            InputMultiplexer inputMultiplexer,
            Viewport viewport,
            @Named(WHITE_PIXEL) TextureRegion whitePixel,
            EnhancedRandom random,
            Updates updates) {
        this.inputMultiplexer = inputMultiplexer;
        this.viewport = viewport;
        container = new StateContainer(State.initial());
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/quan-pixel-16.fnt"));
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        stage = new Stage(viewport);

        VerticalGroup actions = new VerticalGroup();
        Image bg = new Image(whitePixel);
        bg.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actions.setVisible(false);
            }
        });
        bg.setColor(new Color(0x333333ff));
        bg.setSize(480, 270);
        stage.addActor(bg);
        Image bgProgress = new Image(whitePixel);
        bgProgress.setSize(360, 50);
        bgProgress.setColor(Color.LIGHT_GRAY);
        bgProgress.setPosition(stage.getWidth() / 2, stage.getHeight() / 2, Align.center);
        stage.addActor(bgProgress);
        Image fgProgress = new Image(whitePixel);
        fgProgress.setSize(350, 40);
        fgProgress.setColor(Color.CORAL);
        fgProgress.setPosition(stage.getWidth() / 2, stage.getHeight() / 2, Align.center);
        fgProgress.setScaleX(0);
        stage.addActor(fgProgress);
        Label progress = new Label("Progress (50%)", style);
        Label charge = new Label("Charge", style);
        actions.addActor(progress);
        progress.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (container.current().enoughBattery(30)) {
                    return;
                }
                if (random.nextFloat(1f) < 0.8) {
                    container.update(updates.increaseProgress.create(0.3f));
                } else {
                    container.update(updates.increaseProgress.create(-0.3f));
                }
                actions.setVisible(false);
            }
        });
        charge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (container.current().battery() == 100) {
                    return;
                }
                container.update(updates.chargeBattery);
                actions.setVisible(false);
            }
        });
        actions.addActor(charge);
        actions.setVisible(false);
        actions.setPosition(30, 75, Align.bottomLeft);
        actions.setDebug(true);
        actions.align(Align.bottomLeft);
        stage.addActor(actions);
        Image character = new Image(whitePixel);
        character.setSize(30, 75);
        character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actions.setVisible(true);
            }
        });
        stage.addActor(character);

        VerticalGroup debugInfo = new VerticalGroup();
        debugInfo.align(Align.bottomRight);
        debugInfo.setPosition(stage.getWidth(), 0, Align.bottomRight);
        Label battery = new Label("Battery", style);
        Label day = new Label("", style);
        Label tod = new Label("", style);
        debugInfo.addActor(battery);
        debugInfo.addActor(day);
        debugInfo.addActor(tod);
        stage.addActor(debugInfo);

        container
                .distinct(State::progress)
                .listen(state -> fgProgress.addAction(Actions.scaleTo(state.progress(), 1, 0.3f)));
        container.distinct(State::battery).listen(state -> battery.setText("Battery " + state.battery() + "%"));
        container.distinct(State::day).listen(state -> day.setText("Day " + (state.day() + 1) + " / 7"));
        container.distinct(State::tod).listen(state -> tod.setText("Time " + (state.tod() + 1) + " / 7"));
        container.distinct(State::isGameWon).listen(state -> {
            if (!state.isGameWon()) {
                return;
            }
            System.out.println("You won!");
        });
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(CLEAR_COLOR, true);
        stage.act(delta);
        stage.getViewport().apply();
        stage.draw();
    }

    @Override
    public void show() {
        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void hide() {
        inputMultiplexer.removeProcessor(stage);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
