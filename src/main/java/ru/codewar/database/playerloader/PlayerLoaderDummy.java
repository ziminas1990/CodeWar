package ru.codewar.database.playerloader;

import org.json.JSONObject;
import ru.codewar.module.IModulesFactory;
import ru.codewar.module.multiplexer.Multiplexer;
import ru.codewar.module.ship.ShipModule;
import ru.codewar.util.JsonStreamReader;
import ru.codewar.world.Player;

public class PlayerLoaderDummy implements PlayerLoader {

    private IModulesFactory modulesFactory;

    public PlayerLoaderDummy(IModulesFactory modulesFactory) {
        this.modulesFactory = modulesFactory;
    }

    public Player loadPlayer(String login) {
        Player player = new Player(login, new Multiplexer(modulesFactory));
        JSONObject shipData = JsonStreamReader.read(getClass().getResourceAsStream("shuttle.json"));

        ShipModule ship = (ShipModule)modulesFactory.make(shipData);
        player.setShip(ship);
        return player;
    }
}
