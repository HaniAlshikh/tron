package de.alshikh.haw.tron.client.models.game.data.entities;

public class PlayerUpdate {

    private int x, y;
    private boolean pauseGame;
    private int version;

    public PlayerUpdate(int x, int y, boolean pauseGame, int version) {
        this.x = x;
        this.y = y;
        this.pauseGame = pauseGame;
        this.version = version;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean pauseGame() {
        return pauseGame;
    }

    public void setPauseGame(boolean pauseGame) {
        this.pauseGame = pauseGame;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
