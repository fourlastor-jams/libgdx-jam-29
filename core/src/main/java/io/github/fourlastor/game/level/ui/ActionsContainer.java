package io.github.fourlastor.game.level.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import io.github.fourlastor.game.level.state.Character;
import io.github.fourlastor.game.level.state.Progress;
import io.github.fourlastor.game.level.state.StateContainer;
import io.github.fourlastor.game.level.state.Updates;

public class ActionsContainer extends VerticalGroup {

    private final StateContainer container;
    private final Updates updates;
    private final Label.LabelStyle style;
    private Character.Name character;

    public ActionsContainer(StateContainer container, Updates updates, Label.LabelStyle style) {
        super();
        this.container = container;
        this.updates = updates;
        this.style = style;

        setVisible(false);
        align(Align.bottomLeft);

        createProgress(Progress.Type.ART);
        createProgress(Progress.Type.TECH);
        createProgress(Progress.Type.STORY);
        createProgress(Progress.Type.MECH);
        Label charge = new Label("Charge", style);
        charge.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (container.current().battery() == 100) {
                    return;
                }
                container.update(updates.chargeBattery);
                setVisible(false);
            }
        });
        addActor(charge);
        Label shineLight = new Label("Use light", style);
        shineLight.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (container.current().battery() < 30) {
                    return;
                }
                container.update(updates.shineLight);
                setVisible(false);
            }
        });
        addActor(shineLight);
        Label petCat = new Label("Pet cat", style);
        petCat.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (character == null) {
                    return;
                }
                container.update(updates.petCat.create(character));
                setVisible(false);
            }
        });
        addActor(petCat);
    }

    private void createProgress(Progress.Type type) {
        Label progress = new Label(type.displayName, style);
        addActor(progress);
        progress.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (container.current().enoughBattery(30) || character == null) {
                    return;
                }

                container.update(updates.increaseProgress.create(0.05f, type, character));
                setVisible(false);
            }
        });
    }

    public void reveal(float x, float y, Character.Name name) {
        this.character = name;
        setPosition(x, y);
        setVisible(true);
    }
}
