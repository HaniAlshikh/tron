package de.alshikh.haw.tron.app.models.game.data.exceptions;

public class GameFullException extends RuntimeException {
    public GameFullException(String message) {
        super(message);
    }

    public GameFullException() {
        this("Game is already full");
    }
}
