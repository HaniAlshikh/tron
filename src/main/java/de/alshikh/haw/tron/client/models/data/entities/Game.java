package de.alshikh.haw.tron.client.models.data.entities;

import de.alshikh.haw.tron.client.common.data.entites.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Game {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Player winner;

    private Set<Player> currentPlayers;
    private Set<Player> players = new HashSet<>();

    public Game(Player... players) {
        this.players.addAll(Arrays.asList(players));
    }

    public void update() {
        currentPlayers = new HashSet<>(players);
        currentPlayers.forEach(p -> {
            if (p.isDead()) {
                players.remove(p);
                return;
            }

            p.getBike().move();
            checkForCollision(p);
            if (playerEliminated()) {
                if (players.size() == 1) {
                    winner = players.iterator().next();
                }
            }
        });
    }

    private void checkForCollision(Player player) {
        for (Player enemy : currentPlayers) {
            if(!player.equals(enemy)) {
                if (player.getBike().getPosition().equals(enemy.getBike().getPosition())) {
                    log.info(player + " collided with " + enemy);
                    players.remove(player);
                    players.remove(enemy);
                }
                else if (enemy.getBike().getTrail().contains(player.getBike().getPosition())) {
                    players.remove(player);
                }
            }
        }
    }
    
    private boolean playerEliminated() {
        return players.size() != currentPlayers.size();
    }

    public Player getWinner() {
        return winner;
    }

    public Set<Player> getPlayers() {
        return players;
    }
}
