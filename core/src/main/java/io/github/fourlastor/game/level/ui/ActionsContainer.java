package io.github.fourlastor.game.level.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import io.github.fourlastor.game.level.state.Character;
import io.github.fourlastor.game.level.state.Progress;

public class ActionsContainer extends VerticalGroup {

    private final Label.LabelStyle style;
    private final Label.LabelStyle hoverStyle;
    private final Listener listener;
    private Character.Name character;

    public ActionsContainer(Label.LabelStyle style, Label.LabelStyle hoverStyle, Listener listener) {
        super();
        this.style = style;
        this.hoverStyle = hoverStyle;
        this.listener = listener;

        setVisible(false);
        align(Align.bottomLeft);
        columnAlign(Align.left);

        addActor(createProgress(Progress.Type.ART));
        addActor(createProgress(Progress.Type.TECH));
        addActor(createProgress(Progress.Type.STORY));
        addActor(createProgress(Progress.Type.MECH));
        addActor(createAction("Charge", () -> listener.charge(character)));
        addActor(createAction("Use light", listener::light));
        addActor(createAction("Pet cat", () -> {
            if (character == null) {
                return;
            }
            listener.cat(character);
        }));
    }

    private Label createProgress(Progress.Type type) {
        return createAction(type.displayName, () -> {
            if (character == null) {
                return;
            }
            listener.progress(character, type);
        });
    }

    public interface Listener {
        void progress(Character.Name name, Progress.Type type);

        void charge(Character.Name name);

        void light();

        void cat(Character.Name name);
    }

    private Label createAction(String displayName, Runnable onClick) {
        Label action = new Label(" " + displayName + " ", style);
        action.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                action.setStyle(hoverStyle);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                action.setStyle(style);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick.run();
                setVisible(false);
            }
        });
        return action;
    }

    public void reveal(float x, float y, Character.Name name) {
        this.character = name;
        setPosition(x, y);
        setVisible(true);
    }
}
