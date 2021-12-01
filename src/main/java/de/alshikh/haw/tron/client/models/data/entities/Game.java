package de.alshikh.haw.tron.client.models.data.entities;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Game {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Player player;
    private Player winner;
    private boolean gameEnded;

    public Game(Player player) {
        this.player = player;
    }

    public void update(Player opponent) {
        player.getBike().move();
        opponent.getBike().move();
        checkForCollision(opponent);
    }

    private void checkForCollision(Player opponent) {
        if (player.getBike().getPosition().equals(opponent.getBike().getPosition())) {
            log.info(player + " collided with " + opponent);
            player.die();
            opponent.die();
            gameEnded = true;
        }
        else if (opponent.getBike().getTrail().contains(player.getBike().getPosition())) {
            player.die();
            winner = opponent;
        }
        else if (player.getBike().getTrail().contains(opponent.getBike().getPosition())) {
            opponent.die();
            winner = player;
        }
    }

    public boolean gameEnded() {
        return gameEnded;
    }

    public Player getWinner() {
        return winner;
    }
}
