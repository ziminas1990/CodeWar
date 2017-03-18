package ru.codewar.world;

import ru.codewar.logicconveyor.physicallogic.PhysicalLogic;

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
