package ru.codewar.world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.logicconveyor.physicallogic.PhysicalLogic;

import java.util.HashMap;
import java.util.Map;

public class World implements IWorld {

    Logger logger = LoggerFactory.getLogger(World.class);

    private Map<String, Player> players = new HashMap<>();
    private PhysicalLogic physicalEngine;
    private SolarSystem solarSystem;

    public World(PhysicalLogic physicalEngine)
    {
        this.physicalEngine = physicalEngine;
        solarSystem = new SolarSystem(physicalEngine);
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

    public ISolarSystem getSolarSystem() { return solarSystem; }
}
