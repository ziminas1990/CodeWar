package ru.codewar.world;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.module.ship.ShipModule;
import ru.codewar.module.ship.ShipLogic;

public class ShipLoader {
    public ShipLoader() {}

    public ShipModule loadShip(Player player, String shipName) {
        ShipLogic ship =
                new ShipLogic(
                        1, 1000000, 100, new Point(100, 100),
                        new Vector(0.5, 0.5), new Vector(0.001, 0.001));
        return ship;
    }
}
