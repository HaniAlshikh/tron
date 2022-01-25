package de.alshikh.haw.tron.app.models.game.data.entities;

public interface IGame {
    void applyOpponentUpdate(IPlayerUpdate opponentUpdate);

    void movePlayers();

    void checkForCollision();

    IPlayer getWinner();

    void setWinner(IPlayer winner);

    boolean hasEnded();

    void setEnded(boolean ended);

    IPlayer getPlayer();

    void setPlayer(IPlayer player);

    IPlayer getOpponent();

    void setOpponent(IPlayer opponent);
}
