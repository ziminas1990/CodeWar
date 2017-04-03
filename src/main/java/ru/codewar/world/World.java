package ru.codewar.world;

import java.util.HashMap;
import java.util.Map;

public class World {

    private Map<String, Player> players = new HashMap<>();

    public Player getPlayer(String playerName) {
        return players.get(playerName);
    }

    public void addPlayer(Player player)
    {
        players.put(player.getLogin(), player);
    }

}
