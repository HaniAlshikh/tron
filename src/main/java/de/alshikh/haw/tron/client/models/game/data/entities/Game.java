package de.alshikh.haw.tron.client.models.game.data.entities;

import de.alshikh.haw.tron.client.models.game.helpers.CollisionDetector;

public class Game {
    private Player winner;
    private boolean ended;
    private boolean paused;

    // TODO: generalise to support more than two players
    private Player player;
    private Player opponent;

    public void applyOpponentUpdate(PlayerUpdate opponentUpdate) {
        opponent.applyUpdate(opponentUpdate);
    }

    public void movePlayers() {
        player.move();
        opponent.move();
    }

    public void checkForCollision() {
        CollisionDetector.check(this);
    }

    public void checkForBreak() {
        paused = player.pausedGame() || opponent.pausedGame();
    }

    public boolean ended() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public boolean paused() {
        return paused;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }
}
