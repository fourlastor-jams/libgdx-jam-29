package io.github.fourlastor.game.level;

import static io.github.fourlastor.game.di.modules.AssetsModule.WHITE_PIXEL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.random.EnhancedRandom;
import io.github.fourlastor.game.di.modules.AssetsModule;
import io.github.fourlastor.game.level.state.Character;
import io.github.fourlastor.game.level.state.Progress;
import io.github.fourlastor.game.level.state.State;
import io.github.fourlastor.game.level.state.StateContainer;
import io.github.fourlastor.game.level.state.Updates;
import io.github.fourlastor.game.level.ui.ActionsContainer;
import io.github.fourlastor.game.level.ui.ProgressBar;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.animation.FixedFrameAnimation;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import javax.inject.Inject;
import javax.inject.Named;

public class LevelScreen extends ScreenAdapter {

    public static Color CLEAR_COLOR = Color.BLACK;

    private final InputMultiplexer inputMultiplexer;
    private final EnhancedRandom random;
    private final Viewport viewport;
    private final Stage stage;
    private final StateContainer container;
    private final Label.LabelStyle style;
    private final Music music;

    @Inject
    public LevelScreen(
            InputMultiplexer inputMultiplexer,
            Viewport viewport,
            @Named(WHITE_PIXEL) TextureRegion whitePixel,
            Updates updates,
            TextureAtlas atlas,
            EnhancedRandom random,
            AssetManager assetManager) {
        this.inputMultiplexer = inputMultiplexer;
        this.viewport = viewport;
        this.random = random;
        music = assetManager.get(AssetsModule.PATH_MUSIC);
        music.setVolume(0f);
        music.setLooping(true);
        container = new StateContainer(State.initial());
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/quan-pixel-8.fnt"));
        style = new Label.LabelStyle();
        style.font = font;
        Label.LabelStyle hoverStyle = new Label.LabelStyle(style);
        hoverStyle.background = new TextureRegionDrawable(whitePixel).tint(new Color(0xff0044ff));
        stage = new Stage(viewport);
        stage.addActor(new Image(atlas.findRegion("environment/universe")));

        ActionsContainer actions = new ActionsContainer(container, updates, style, hoverStyle);
        Image bg = new Image(atlas.findRegion("environment/background"));
        stage.addActor(bg);

        stage.addActor(new Image(atlas.findRegion("environment/wall-2")));
        stage.addActor(new Image(atlas.findRegion("environment/wall-1")));
        stage.addActor(new Image(atlas.findRegion("environment/floor")));

        Actor hideActionsClickTarget = new Actor();
        hideActionsClickTarget.setSize(480, 270);
        hideActionsClickTarget.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actions.setVisible(false);
            }
        });
        stage.addActor(hideActionsClickTarget);

        ProgressBar totalProgress = new ProgressBar(whitePixel, new Color(0x262b44ff), -1f);
        totalProgress.setSize(133, 8);
        totalProgress.setPosition(159 + totalProgress.getWidth(), 183);
        stage.addActor(totalProgress);

        ProgressBar artProgress = new ProgressBar(whitePixel);
        artProgress.setSize(44, 3);
        artProgress.setPosition(274, 168);
        stage.addActor(artProgress);
        ProgressBar techProgress = new ProgressBar(whitePixel);
        techProgress.setSize(44, 3);
        techProgress.setPosition(274, 159);
        stage.addActor(techProgress);
        ProgressBar storyProgress = new ProgressBar(whitePixel);
        storyProgress.setSize(44, 3);
        storyProgress.setPosition(274, 150);
        stage.addActor(storyProgress);
        ProgressBar mechProgress = new ProgressBar(whitePixel);
        mechProgress.setSize(44, 3);
        mechProgress.setPosition(274, 141);
        stage.addActor(mechProgress);

        stage.addActor(actions);
        AnimatedImage raeleus =
                createCharacter(atlas.findRegions("character/raeleus/idle"), 228, 14, actions, Character.Name.RAELEUS);
        Image lyze = createCharacter(atlas.findRegions("character/lyze/idle"), 192, 14, actions, Character.Name.LYZE);
        Image dragonQueen = createCharacter(
                atlas.findRegions("character/dragon_queen/idle"), 152, 16, actions, Character.Name.DRAGON_QUEEN);
        Image panda = createCharacter(
                atlas.findRegions("character/peanut_panda/idle"), 121, 10, actions, Character.Name.PANDA);
        stage.addActor(raeleus);
        stage.addActor(lyze);
        stage.addActor(dragonQueen);
        stage.addActor(panda);

        VerticalGroup debugInfo = new VerticalGroup();
        debugInfo.align(Align.bottomRight);
        debugInfo.columnAlign(Align.left);
        debugInfo.setPosition(stage.getWidth(), 0, Align.bottomRight);
        Label batteryInfo = new Label("Battery", style);
        Label day = new Label("", style);
        Label tod = new Label("", style);
        Label deathSafety = new Label("", style);
        Label deathAppeared = new Label("", style);
        Label raeleusInfo = new Label("", style);
        Label lyzeInfo = new Label("", style);
        debugInfo.addActor(batteryInfo);
        debugInfo.addActor(day);
        debugInfo.addActor(tod);
        debugInfo.addActor(deathSafety);
        debugInfo.addActor(deathAppeared);
        debugInfo.addActor(raeleusInfo);
        debugInfo.addActor(lyzeInfo);
        stage.addActor(debugInfo);
        Array<TextureAtlas.AtlasRegion> batteryRegions = atlas.findRegions("environment/battery/battery");
        Array<Drawable> drawables = new Array<>(batteryRegions.size);
        for (TextureRegion region : batteryRegions) {
            drawables.add(new TextureRegionDrawable(region));
        }
        AnimatedImage battery = new AnimatedImage(new FixedFrameAnimation<>(1, drawables));
        battery.setPosition(345, 23);
        battery.setPlaying(false);
        stage.addActor(battery);

        container.distinct(State::progress).listen(state -> {
            Progress progress = state.progress();
            totalProgress.setProgress(-1 * (1 - progress.total()));
            artProgress.setProgress(progress.artProgress());
            mechProgress.setProgress(progress.mechProgress());
            storyProgress.setProgress(progress.storyProgress());
            techProgress.setProgress(progress.techProgress());
        });
        container.distinct(State::battery).listen(state -> {
            battery.setProgress(state.battery() / 10);
            batteryInfo.setText("Battery " + state.battery() + "%");
        });
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
        container.distinct(State::raeleus).listen(state -> {
            Character character = state.raeleus();
            raeleusInfo.setText("Raeleus: " + character.stress() + "% | " + character.kidnapped());
        });
        container.distinct(State::lyze).listen(state -> {
            Character character = state.lyze();
            lyzeInfo.setText("Lyze: " + character.stress() + "% | " + character.kidnapped());
        });
    }

    private AnimatedImage createCharacter(
            Array<? extends TextureRegion> regions, float x, float y, ActionsContainer actions, Character.Name name) {
        Array<Drawable> frames = new Array<>(regions.size);
        for (TextureRegion region : regions) {
            frames.add(new TextureRegionDrawable(region));
        }
        FixedFrameAnimation<Drawable> animation = new FixedFrameAnimation<>(0.2f, frames, Animation.PlayMode.LOOP);
        AnimatedImage character = new AnimatedImage(animation);
        character.setSize(30, 75);
        character.setPosition(x, y);
        character.setProgress(random.nextFloat(1f));
        character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float clickX, float clickY) {
                actions.reveal(x + 30, y + 75, name);
            }
        });
        return character;
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
        music.play();
    }

    @Override
    public void hide() {
        music.stop();
        inputMultiplexer.removeProcessor(stage);
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }
}
