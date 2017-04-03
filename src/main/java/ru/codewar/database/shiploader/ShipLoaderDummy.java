package ru.codewar.database.shiploader;

import org.json.JSONObject;
import ru.codewar.module.ship.ShipFactory;
import ru.codewar.module.ship.BaseShip;
import ru.codewar.util.JsonStreamReader;
import ru.codewar.world.Player;

public class ShipLoaderDummy implements ShipLoader {
    private ShipFactory factory = new ShipFactory();

    public BaseShip loadShip(Player player, String shipName) {
        JSONObject description = JsonStreamReader.read(getClass().getResourceAsStream(shipName + ".json"));
        return (description != null) ? factory.make(description, player.getLogin()) : null;
    }
}