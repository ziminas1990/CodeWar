package ru.codewar.database.playerloader;

import ru.codewar.module.ship.ShipModule;
import ru.codewar.world.Player;
import ru.codewar.database.shiploader.ShipLoaderDummy;

public class PlayerLoaderDummy implements PlayerLoader {

    private ShipLoaderDummy shipLoader = new ShipLoaderDummy();

    public Player loadPlayer(String login) {
        Player player = new Player(login);
        ShipModule ship = shipLoader.loadShip(player, "luna-1");
        player.setShip(ship);
        return player;
    }

}
