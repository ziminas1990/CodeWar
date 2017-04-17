package ru.codewar.world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.logicconveyor.physicallogic.PhysicalLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class World {

    Logger logger = LoggerFactory.getLogger(World.class);

    private Map<String, Player> players = new HashMap<>();
    private PhysicalLogic physicalEngine;
    private ArrayList<CelestialBody> bodies = new ArrayList<>();

    public World(PhysicalLogic physicalEngine)
    {
        this.physicalEngine = physicalEngine;
    }

    public void addCelestialBody(CelestialBody body) {
        bodies.add(body);
    }

    public Player getPlayer(String playerName) {
        return players.get(playerName);
    }

    public void addPlayer(Player player)
    {
        logger.info("Adding player \"{}\" to the world", player.getLogin());
        physicalEngine.registerObject(player.getActiveShip());
        players.put(player.getLogin(), player);
    }
}
