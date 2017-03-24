package ru.codewar.database.shiploader;

import ru.codewar.module.ship.ShipModule;
import ru.codewar.world.Player;

public interface ShipLoader {
    ShipModule loadShip(Player player, String shipName);
}
