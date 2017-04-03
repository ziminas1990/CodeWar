package ru.codewar.database.shiploader;

import ru.codewar.module.ship.BaseShip;
import ru.codewar.module.ship.ShipModule;
import ru.codewar.world.Player;

public interface ShipLoader {
    BaseShip loadShip(Player player, String shipName);
}
