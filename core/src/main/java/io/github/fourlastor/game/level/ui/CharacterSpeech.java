package io.github.fourlastor.game.level.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.fourlastor.game.level.state.Character;
import io.github.fourlastor.game.level.state.CharacterMessage;
import java.util.List;

public class CharacterSpeech extends WidgetGroup {

    private final Label bubble;
    private int messageIndex = -1;
    private List<CharacterMessage> messages;

    public CharacterSpeech(Label.LabelStyle bubbleStyle) {
        super();
        bubble = new Label("", bubbleStyle);
        bubble.setWrap(true);
        addActor(bubble);
        setFillParent(true);
        setTouchable(Touchable.enabled);
        setVisible(false);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (messageIndex == -1) {
                    return;
                }
                if (messageIndex == messages.size() - 1) {
                    messageIndex = -1;
                    messages = null;
                    setVisible(false);
                    return;
                }
                showMessage(messageIndex + 1);
            }
        });
    }

    public void display(List<CharacterMessage> messages) {
        setVisible(true);
        this.messages = messages;
        showMessage(0);
    }

    private void showMessage(int index) {
        messageIndex = index;
        CharacterMessage message = messages.get(index);
        bubble.setText(message.message);
        bubble.setWidth(75);
        bubble.pack();
        bubble.setWidth(75);
        bubble.setPosition(
                message.name.idlePos.x + Character.IDLE_SIZE.x, message.name.idlePos.y + Character.IDLE_SIZE.y - 25);
    }
}
