package de.alshikh.haw.tron.app.models.game.data.entities;

import de.alshikh.haw.tron.app.models.game.helpers.CollisionDetector;

public class Game implements IGame {

    private IPlayer winner;
    private boolean ended;

    private IPlayer player;
    private IPlayer opponent;

    @Override
    public void applyOpponentUpdate(IPlayerUpdate opponentUpdate) {
        opponent.applyUpdate(opponentUpdate);
    }

    @Override
    public void movePlayers() {
        player.move();
        opponent.move();
    }

    @Override
    public void checkForCollision() {
        CollisionDetector.check(this);
    }

    @Override
    public IPlayer getWinner() {
        return winner;
    }

    @Override
    public void setWinner(IPlayer winner) {
        this.winner = winner;
    }

    @Override
    public boolean hasEnded() {
        return ended;
    }

    @Override
    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    @Override
    public IPlayer getPlayer() {
        return player;
    }

    @Override
    public void setPlayer(IPlayer player) {
        this.player = player;
    }

    @Override
    public IPlayer getOpponent() {
        return opponent;
    }

    @Override
    public void setOpponent(IPlayer opponent) {
        this.opponent = opponent;
    }
}
