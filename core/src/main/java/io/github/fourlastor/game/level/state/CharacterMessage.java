package io.github.fourlastor.game.level.state;

public class CharacterMessage {
    public final Character.Name name;
    public final String message;

    public CharacterMessage(Character.Name name, String message) {
        this.name = name;
        this.message = message;
    }
}
