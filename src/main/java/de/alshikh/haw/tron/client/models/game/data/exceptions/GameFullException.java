package de.alshikh.haw.tron.client.models.game.data.exceptions;

public class GameFullException extends RuntimeException {
    public GameFullException(String message) {
        super(message);
    }

    public GameFullException() {
        this("Game is already full");
    }
}
