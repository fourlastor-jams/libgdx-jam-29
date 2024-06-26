package io.github.fourlastor.game.level;

import static io.github.fourlastor.game.di.modules.AssetsModule.WHITE_PIXEL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.random.EnhancedRandom;
import io.github.fourlastor.game.di.modules.AssetsModule;
import io.github.fourlastor.game.end.EndState;
import io.github.fourlastor.game.level.state.Character;
import io.github.fourlastor.game.level.state.CharacterMessage;
import io.github.fourlastor.game.level.state.Progress;
import io.github.fourlastor.game.level.state.State;
import io.github.fourlastor.game.level.state.StateContainer;
import io.github.fourlastor.game.level.state.Updates;
import io.github.fourlastor.game.level.ui.ActionsContainer;
import io.github.fourlastor.game.level.ui.BackgroundImage;
import io.github.fourlastor.game.level.ui.CatImage;
import io.github.fourlastor.game.level.ui.CharacterImage;
import io.github.fourlastor.game.level.ui.CharacterSpeech;
import io.github.fourlastor.game.level.ui.ProgressBar;
import io.github.fourlastor.game.route.Router;
import io.github.fourlastor.harlequin.animation.Animation;
import io.github.fourlastor.harlequin.animation.FixedFrameAnimation;
import io.github.fourlastor.harlequin.ui.AnimatedImage;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Named;

public class LevelScreen extends ScreenAdapter {

    public static Color CLEAR_COLOR = Color.BLACK;

    private final InputMultiplexer inputMultiplexer;
    private final Updates updates;
    private final TextureAtlas atlas;
    private final EnhancedRandom random;
    private final Viewport viewport;
    private final Stage stage;
    private final StateContainer container;
    private final Music music;
    private final Image fadeInOut;
    private final BitmapFont font;

    @Inject
    public LevelScreen(
            Router router,
            InputMultiplexer inputMultiplexer,
            Viewport viewport,
            @Named(WHITE_PIXEL) TextureRegion whitePixel,
            Updates updates,
            TextureAtlas atlas,
            EnhancedRandom random,
            AssetManager assetManager) {
        this.inputMultiplexer = inputMultiplexer;
        this.viewport = viewport;
        this.updates = updates;
        this.atlas = atlas;
        this.random = random;
        music = assetManager.get(AssetsModule.PATH_MUSIC);
        music.setVolume(0.1f);
        music.setLooping(true);
        container = new StateContainer(State.initial());
        font = new BitmapFont(Gdx.files.internal("fonts/quan-pixel-8.fnt"));
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        Label.LabelStyle hoverStyle = new Label.LabelStyle(style);
        hoverStyle.background = new TextureRegionDrawable(whitePixel).tint(new Color(0xff0044ff));
        stage = new Stage(viewport);
        stage.addActor(new Image(atlas.findRegion("environment/universe")));

        ActionsContainer actions = new ActionsContainer(style, hoverStyle, listener());
        BackgroundImage bg = new BackgroundImage(
                atlas.findRegion("environment/backgrounds/background-0"),
                atlas.findRegion("environment/backgrounds/background-25"),
                atlas.findRegion("environment/backgrounds/background-50"),
                atlas.findRegion("environment/backgrounds/background-100"));
        stage.addActor(bg);

        stage.addActor(new Image(atlas.findRegion("environment/wall-2")));
        AnimatedImage screenImage =
                new AnimatedImage(toAnimation(atlas.findRegions("environment/screen/screen"), 0.4f));
        screenImage.setPosition(134, 122);
        stage.addActor(screenImage);
        AnimatedImage todImage = new AnimatedImage(toAnimation(atlas.findRegions("environment/screen/tod"), 1f));
        todImage.setPosition(158, 167);
        todImage.setPlaying(false);
        stage.addActor(todImage);
        Label dayLabel = new Label("Day 1", style);
        dayLabel.setColor(new Color(0xf77622ff));
        dayLabel.setPosition(175, 169);
        stage.addActor(dayLabel);
        AnimatedImage paneImage = new AnimatedImage(toAnimation(atlas.findRegions("environment/panel/pane"), 0.2f));
        paneImage.setPosition(342, 41);
        stage.addActor(paneImage);
        AnimatedImage computerImage =
                new AnimatedImage(toAnimation(atlas.findRegions("environment/computer/computer"), 0.2f));
        computerImage.setPosition(160, 32);
        stage.addActor(computerImage);
        AnimatedImage serverImage =
                new AnimatedImage(toAnimation(atlas.findRegions("environment/server/server"), 0.2f));
        serverImage.setPosition(28, 0);
        stage.addActor(serverImage);
        Image scytheImage = new Image(atlas.findRegion("environment/scythe"));
        scytheImage.setPosition(384, 149);
        stage.addActor(scytheImage);
        Image bikeImage = new Image(atlas.findRegion("environment/bike"));
        bikeImage.setPosition(378, 20);
        stage.addActor(bikeImage);
        Image box1Image = new Image(atlas.findRegion("environment/boxes/box-1"));
        box1Image.setPosition(119, 30);
        stage.addActor(box1Image);
        Image box2Image = new Image(atlas.findRegion("environment/boxes/box-2"));
        box2Image.setPosition(288, 4);
        stage.addActor(box2Image);
        Image light1Image = new Image(atlas.findRegion("environment/lights/light-1"));
        light1Image.setPosition(84, 28);
        stage.addActor(light1Image);
        Image light2Image = new Image(atlas.findRegion("environment/lights/light-2"));
        light2Image.setPosition(417, 27);
        stage.addActor(light2Image);
        Image lightImage = new Image(atlas.findRegion("environment/light"));
        stage.addActor(lightImage);

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
        CharacterImage raeleus = createCharacter(actions, Character.Name.RAELEUS);
        CharacterImage lyze = createCharacter(actions, Character.Name.LYZE);
        CharacterImage dragonQueen = createCharacter(actions, Character.Name.DRAGON_QUEEN);
        CharacterImage panda = createCharacter(actions, Character.Name.PANDA);
        stage.addActor(raeleus);
        stage.addActor(lyze);
        stage.addActor(dragonQueen);
        stage.addActor(panda);

        Array<TextureAtlas.AtlasRegion> batteryRegions = atlas.findRegions("environment/battery/battery");
        Array<Drawable> drawables = new Array<>(batteryRegions.size);
        for (TextureRegion region : batteryRegions) {
            drawables.add(new TextureRegionDrawable(region));
        }
        AnimatedImage battery = new AnimatedImage(new FixedFrameAnimation<>(1, drawables));
        battery.setPosition(345, 23);
        battery.setPlaying(false);
        stage.addActor(battery);

        CatImage cat = cat();
        cat.setPosition(96, 10);
        stage.addActor(cat);

        container.distinct(State::progress).listen(state -> {
            Progress progress = state.progress();
            totalProgress.setProgress(-1 * (1 - progress.total()));
            artProgress.setProgress(progress.artProgress());
            mechProgress.setProgress(progress.mechProgress());
            storyProgress.setProgress(progress.storyProgress());
            techProgress.setProgress(progress.techProgress());
        });
        container.distinct(State::battery).listen(state -> battery.setProgress(state.battery() / 10));
        container.distinct(State::day).listen(state -> dayLabel.setText("Day " + (state.day() + 1) + " / 7"));
        container.distinct(State::tod).listen(state -> todImage.setProgress(state.tod()));
        container.distinct(State::deathSafety).listen(state -> bg.updateDamage(100 - state.deathSafety()));
        container.distinct(State::isGameWon).listen(state -> {
            if (!state.isGameWon()) {
                return;
            }
            router.goToGameEnd(EndState.WON);
        });
        container.distinct(State::isGameLost).listen(state -> {
            if (!state.isGameLost()) {
                return;
            }
            router.goToGameEnd(EndState.LOST);
        });
        container.distinct(State::raeleus).listen(onCharacterChange(raeleus, State::raeleus));
        container.distinct(State::lyze).listen(onCharacterChange(lyze, State::lyze));
        container.distinct(State::dragonQueen).listen(onCharacterChange(dragonQueen, State::dragonQueen));
        container.distinct(State::panda).listen(onCharacterChange(panda, State::panda));
        container.distinct(State::catStance).listen(state -> cat.updateStance(state.catStance()));
        fadeInOut = new Image(whitePixel);
        fadeInOut.setSize(stage.getWidth(), stage.getHeight());
        fadeInOut.setTouchable(Touchable.disabled);
        Color fadeColor = new Color(Color.BLACK);
        fadeColor.a = 0;
        fadeInOut.setColor(fadeColor);
        stage.addActor(fadeInOut);
        NinePatchDrawable bubble = new NinePatchDrawable(new NinePatch(atlas.findRegion("bubble"), 3, 3, 1, 1));
        Label.LabelStyle bubbleStyle = new Label.LabelStyle(style);
        bubbleStyle.background = bubble;
        CharacterSpeech characterSpeech = new CharacterSpeech(bubbleStyle);
        stage.addActor(characterSpeech);
        characterSpeech.display(introMessages());
    }

    private static List<CharacterMessage> introMessages() {
        return Arrays.asList(
                new CharacterMessage(Character.Name.RAELEUS, "Alright team, let's do this."),
                new CharacterMessage(
                        Character.Name.RAELEUS, "Work on something, ANYTHING, we need to finish the jam in 7 days."),
                new CharacterMessage(Character.Name.LYZE, "What happens if we don't finish it?"),
                new CharacterMessage(Character.Name.PANDA, "We lose."),
                new CharacterMessage(
                        Character.Name.DRAGON_QUEEN,
                        "We shouldn't overwork. It stresses us out and can cause setbacks."),
                new CharacterMessage(Character.Name.DRAGON_QUEEN, "You can play with the cat if you need to relax."),
                new CharacterMessage(Character.Name.LYZE, "What's the bike for?"),
                new CharacterMessage(Character.Name.RAELEUS, "See that battery on the right?"),
                new CharacterMessage(
                        Character.Name.RAELEUS,
                        "Working on something will consume power, someone will have to recharge it from time to time."),
                new CharacterMessage(Character.Name.PANDA, "The hull is slowly breaking down."),
                new CharacterMessage(
                        Character.Name.PANDA,
                        "We need to keep it in good shape, or someone will have to go out and fix it, and they won't be able to come back in."),
                new CharacterMessage(Character.Name.DRAGON_QUEEN, "Make sure the hull doesn't break. Got it."),
                new CharacterMessage(Character.Name.LYZE, "What happens if we finish before the deadline?"),
                new CharacterMessage(Character.Name.RAELEUS, "We need to hold on until the end of the jam."),
                new CharacterMessage(
                        Character.Name.RAELEUS,
                        "Let's recap:\n1 - Push the progress forward, but don't overwork too much."),
                new CharacterMessage(Character.Name.RAELEUS, "2 - If you're stressed, chill a bit with the cat."),
                new CharacterMessage(Character.Name.RAELEUS, "3 - Make sure the hull doesn't break."),
                new CharacterMessage(Character.Name.RAELEUS, "4 - Hold on until the end of the jam."),
                new CharacterMessage(Character.Name.RAELEUS, "That's all, let's get started!"));
    }

    private ActionsContainer.Listener listener() {
        return new ActionsContainer.Listener() {
            @Override
            public void progress(Character.Name name, Progress.Type type) {
                if (!container.current().enoughBattery(30)) {
                    return;
                }
                runUpdate(() -> container.update(updates.increaseProgress.create(0.05f, type, name)));
            }

            @Override
            public void charge(Character.Name name) {
                if (container.current().battery() == 100) {
                    return;
                }
                runUpdate(() -> container.update(updates.chargeBattery.create(name)));
            }

            @Override
            public void light() {
                if (container.current().battery() < 30) {
                    return;
                }
                runUpdate(() -> container.update(updates.shineLight));
            }

            @Override
            public void cat(Character.Name name) {
                runUpdate(() -> container.update(updates.petCat.create(name)));
            }
        };
    }

    private void runUpdate(Runnable update) {
        fadeInOut.addAction(Actions.sequence(
                Actions.fadeIn(0.4f), Actions.delay(0.3f), Actions.run(update), Actions.fadeOut(0.2f)));
    }

    private Consumer<State> onCharacterChange(CharacterImage characterImage, Function<State, Character> selector) {
        return state -> {
            Character character = selector.apply(state);
            if (character.kidnapped()) {
                characterImage.setVisible(false);
                return;
            }
            if (character.cycling()) {
                characterImage.setCycling();
            } else {
                characterImage.updateStress(character.stress());
            }
        };
    }

    private CatImage cat() {
        Animation<Drawable> idleStanding = catAnimation("idle-standing");
        Animation<Drawable> idleSitting = catAnimation("idle-sitting");
        Animation<Drawable> walking = catAnimation("walking");
        Animation<Drawable> hissing = catAnimation("hissing");
        return new CatImage(idleStanding, idleSitting, walking, hissing);
    }

    private Animation<Drawable> toAnimation(Array<? extends TextureRegion> regions, float frameDuration) {
        Array<Drawable> drawables = new Array<>(regions.size);
        for (TextureRegion region : regions) {
            drawables.add(new TextureRegionDrawable(region));
        }
        return new FixedFrameAnimation<>(frameDuration, drawables, Animation.PlayMode.LOOP);
    }

    private Animation<Drawable> catAnimation(String stance) {
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions("environment/cat/" + stance);
        return toAnimation(regions, 0.2f);
    }

    private CharacterImage createCharacter(ActionsContainer actions, Character.Name name) {
        Animation<Drawable> idle0 = idleAnimation(name, 0);
        Animation<Drawable> idle25 = idleAnimation(name, 25);
        Animation<Drawable> idle50 = idleAnimation(name, 50);
        Animation<Drawable> idle100 = idleAnimation(name, 100);
        Animation<Drawable> cycle = toAnimation(atlas.findRegions("character/" + name.folder + "/cycle"), 0.2f);
        CharacterImage character =
                new CharacterImage(idle0, idle25, idle50, idle100, cycle, name.idlePos, name.cyclePos);
        character.setPosition(name.idlePos.x, name.idlePos.y);
        character.setProgress(random.nextFloat(1f));
        character.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float clickX, float clickY) {
                actions.reveal(
                        character.getX() + Character.IDLE_SIZE.x, character.getY() + Character.IDLE_SIZE.y, name);
            }
        });
        return character;
    }

    private Animation<Drawable> idleAnimation(Character.Name name, int stress) {
        Array<? extends TextureAtlas.AtlasRegion> regions =
                atlas.findRegions("character/" + name.folder + "/idle-stress-" + stress);
        return toAnimation(regions, 0.2f);
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
