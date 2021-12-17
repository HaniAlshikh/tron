package de.alshikh.haw.tron.client.models.game.data.entities;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// game is observable and player is iobserver
public class Game {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Player winner;
    private boolean ended;
    private boolean paused;

    // TODO: maybe generalise to support more than two players
    private Player player;
    private Player opponent;

    public void update() {
        checkForCollision();
        checkForBreak();
    }

    private void checkForCollision() {
        ended = true;
        if (player.getBike().getPosition().equals(opponent.getBike().getPosition())) {
            log.info(player + " collided with " + opponent);
            player.die();
            opponent.die();
        }
        else if (opponent.getBike().getTrail().contains(player.getBike().getPosition())) {
            player.die();
            winner = opponent;
        }
        else if (player.getBike().getTrail().contains(opponent.getBike().getPosition())) {
            opponent.die();
            winner = player;
        } else {
            ended = false;
        }
    }

    private void checkForBreak() {
        paused = player.pausedGame() || opponent.pausedGame();
    }

    public boolean ended() {
        return ended;
    }

    public boolean paused() {
        return paused;
    }

    public Player getWinner() {
        return winner;
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
