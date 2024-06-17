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
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.random.EnhancedRandom;
import io.github.fourlastor.game.level.state.Progress;
import io.github.fourlastor.game.level.state.State;
import io.github.fourlastor.game.level.state.StateContainer;
import io.github.fourlastor.game.level.state.Updates;
import io.github.fourlastor.game.level.ui.ProgressBar;
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
        ProgressBar totalProgress = new ProgressBar(whitePixel);
        totalProgress.setSize(350, 40);
        totalProgress.setPosition(stage.getWidth() / 2, stage.getHeight() / 2, Align.center);
        stage.addActor(totalProgress);

        HorizontalGroup progressTypes = new HorizontalGroup();
        //        progressTypes.setSize(350, 20);
        ProgressBar artProgress = new ProgressBar(whitePixel, Color.GREEN);
        artProgress.setSize(87, 20);
        progressTypes.addActor(artProgress);
        ProgressBar techProgress = new ProgressBar(whitePixel, Color.RED);
        techProgress.setSize(87, 20);
        progressTypes.addActor(techProgress);
        ProgressBar storyProgress = new ProgressBar(whitePixel, Color.BLUE);
        storyProgress.setSize(87, 20);
        progressTypes.addActor(storyProgress);
        ProgressBar mechProgress = new ProgressBar(whitePixel, Color.BROWN);
        mechProgress.setSize(87, 20);
        progressTypes.addActor(mechProgress);
        progressTypes.space(10);
        progressTypes.setPosition(0, stage.getHeight() - 20);
        stage.addActor(progressTypes);

        createProgress(random, updates, style, actions, Progress.Type.ART);
        createProgress(random, updates, style, actions, Progress.Type.TECH);
        createProgress(random, updates, style, actions, Progress.Type.STORY);
        createProgress(random, updates, style, actions, Progress.Type.MECH);
        Label charge = new Label("Charge", style);
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
        Label shineLight = new Label("Use light", style);
        shineLight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (container.current().battery() == 100) {
                    return;
                }
                container.update(updates.shineLight);
                actions.setVisible(false);
            }
        });
        actions.addActor(shineLight);
        actions.setVisible(false);
        actions.setPosition(30, 75, Align.bottomLeft);
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
        Label deathSafety = new Label("", style);
        Label deathAppeared = new Label("", style);
        debugInfo.addActor(battery);
        debugInfo.addActor(day);
        debugInfo.addActor(tod);
        debugInfo.addActor(deathSafety);
        debugInfo.addActor(deathAppeared);
        stage.addActor(debugInfo);

        container.distinct(State::progress).listen(state -> {
            Progress progress = state.progress();
            totalProgress.setProgress(progress.total());
            artProgress.setProgress(progress.artProgress());
            mechProgress.setProgress(progress.mechProgress());
            storyProgress.setProgress(progress.storyProgress());
            techProgress.setProgress(progress.techProgress());
        });
        container.distinct(State::battery).listen(state -> battery.setText("Battery " + state.battery() + "%"));
        container.distinct(State::day).listen(state -> day.setText("Day " + (state.day() + 1) + " / 7"));
        container.distinct(State::tod).listen(state -> tod.setText("Time " + (state.tod() + 1) + " / 7"));
        container
                .distinct(State::deathSafety)
                .listen(state -> deathSafety.setText("Safety: " + state.deathSafety() + "%"));
        container
                .distinct(State::deathAppeared)
                .listen(state -> deathAppeared.setText("Death appeared: " + state.deathAppeared()));
        container.distinct(State::isGameWon).listen(state -> {
            if (!state.isGameWon()) {
                return;
            }
            System.out.println("You won!");
        });
    }

    private void createProgress(
            EnhancedRandom random, Updates updates, Label.LabelStyle style, VerticalGroup actions, Progress.Type type) {
        Label progress = new Label(type.displayName, style);
        actions.addActor(progress);
        progress.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (container.current().enoughBattery(30)) {
                    return;
                }
                if (random.nextFloat(1f) < 0.8) {
                    container.update(updates.increaseProgress.create(0.3f, type));
                } else {
                    container.update(updates.increaseProgress.create(-0.3f, type));
                }
                actions.setVisible(false);
            }
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
