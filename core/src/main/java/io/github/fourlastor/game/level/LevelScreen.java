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
import io.github.fourlastor.game.level.state.Character;
import io.github.fourlastor.game.level.state.Progress;
import io.github.fourlastor.game.level.state.State;
import io.github.fourlastor.game.level.state.StateContainer;
import io.github.fourlastor.game.level.state.Updates;
import io.github.fourlastor.game.level.ui.ActionsContainer;
import io.github.fourlastor.game.level.ui.ProgressBar;
import javax.inject.Inject;
import javax.inject.Named;

public class LevelScreen extends ScreenAdapter {

    public static Color CLEAR_COLOR = Color.BLACK;

    private final InputMultiplexer inputMultiplexer;
    private final Viewport viewport;
    private final TextureRegion whitePixel;
    private final EnhancedRandom random;
    private final Updates updates;
    private final Stage stage;
    private final StateContainer container;
    private final Label.LabelStyle style;

    @Inject
    public LevelScreen(
            InputMultiplexer inputMultiplexer,
            Viewport viewport,
            @Named(WHITE_PIXEL) TextureRegion whitePixel,
            EnhancedRandom random,
            Updates updates) {
        this.inputMultiplexer = inputMultiplexer;
        this.viewport = viewport;
        this.whitePixel = whitePixel;
        this.random = random;
        this.updates = updates;
        container = new StateContainer(State.initial());
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/quan-pixel-16.fnt"));
        style = new Label.LabelStyle();
        style.font = font;
        stage = new Stage(viewport);

        ActionsContainer actions = new ActionsContainer(container, updates, style);
        Image bg = new Image(whitePixel);
        bg.setColor(new Color(0x333333ff));
        bg.setSize(480, 270);

        bg.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                actions.setVisible(false);
            }
        });
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

        stage.addActor(actions);
        Image raeleus = createCharacter(whitePixel, 0, 0, actions, Character.Type.RAELEUS);
        Image lyze = createCharacter(whitePixel, 40, 0, actions, Character.Type.LYZE);
        stage.addActor(raeleus);
        stage.addActor(lyze);

        VerticalGroup debugInfo = new VerticalGroup();
        debugInfo.align(Align.bottomRight);
        debugInfo.setPosition(stage.getWidth(), 0, Align.bottomRight);
        Label battery = new Label("Battery", style);
        Label day = new Label("", style);
        Label tod = new Label("", style);
        Label deathSafety = new Label("", style);
        Label deathAppeared = new Label("", style);
        Label raeleusInfo = new Label("", style);
        Label lyzeInfo = new Label("", style);
        debugInfo.addActor(battery);
        debugInfo.addActor(day);
        debugInfo.addActor(tod);
        debugInfo.addActor(deathSafety);
        debugInfo.addActor(deathAppeared);
        debugInfo.addActor(raeleusInfo);
        debugInfo.addActor(lyzeInfo);
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
        container.distinct(State::raeleus).listen(state -> {
            Character character = state.raeleus();
            raeleusInfo.setText("Raeleus: " + character.stress() + "% | " + character.kidnapped());
        });
        container.distinct(State::lyze).listen(state -> {
            Character character = state.lyze();
            lyzeInfo.setText("Lyze: " + character.stress() + "% | " + character.kidnapped());
        });
    }

    private Image createCharacter(
            TextureRegion whitePixel, float x, float y, ActionsContainer actions, Character.Type type) {
        Image character = new Image(whitePixel);
        character.setSize(30, 75);
        character.setPosition(x, y);
        character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float clickX, float clickY) {
                actions.reveal(x + 30, y + 75, type);
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
