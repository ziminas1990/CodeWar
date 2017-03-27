package ru.codewar.database.shiploader;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.ShipsLogicConveyor;
import ru.codewar.logicconveyor.physicallogic.PhysicalLogic;
import ru.codewar.module.ship.ShipModule;
import ru.codewar.module.ship.BaseShip;
import ru.codewar.world.Player;

public class ShipLoaderDummy implements ShipLoader {
    private PhysicalLogic physicalEngine;
    private ShipsLogicConveyor shipsLogicConveyor;

    public ShipLoaderDummy(PhysicalLogic physicalEngine, ShipsLogicConveyor shipsLogicConveyor) {
        this.physicalEngine = physicalEngine;
        this.shipsLogicConveyor = shipsLogicConveyor;
    }

    public ShipModule loadShip(Player player, String shipName) {
        BaseShip ship =
                new BaseShip(
                        "ship", 1000000, 100, new Point(100, 100),
                        new Vector(0.5, 0.5), new Vector(0.001, 0.001));
        physicalEngine.registerObject(ship);
        shipsLogicConveyor.addShip(ship);
        return ship;
    }
}