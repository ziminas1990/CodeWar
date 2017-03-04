package ru.codewar.world;

import ru.codewar.module.ship.ShipModule;

public class PlayerLoader {

    private ShipLoader shipLoader = new ShipLoader();

    public Player loadPlayer(String login) {
        Player player = new Player(login);
        ShipModule ship = shipLoader.loadShip(player, "luna-1");
        player.setShip(ship);
        return player;
    }

}
