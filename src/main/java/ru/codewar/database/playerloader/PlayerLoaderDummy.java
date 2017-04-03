package ru.codewar.database.playerloader;

import ru.codewar.database.shiploader.ShipLoader;
import ru.codewar.module.ship.ShipModule;
import ru.codewar.world.Player;
import ru.codewar.database.shiploader.ShipLoaderDummy;

public class PlayerLoaderDummy implements PlayerLoader {

    private ShipLoader shipLoader;

    public PlayerLoaderDummy(ShipLoader shipLoader) {
        this.shipLoader = shipLoader;
    }

    public Player loadPlayer(String login) {
        Player player = new Player(login);
        player.setShip(shipLoader.loadShip(player, "shuttle"));
        return player;
    }

}
