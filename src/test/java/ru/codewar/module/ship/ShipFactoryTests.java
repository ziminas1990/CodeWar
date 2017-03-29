package ru.codewar.module.ship;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.codewar.util.JsonStreamReader;

public class ShipFactoryTests {

    @Test
    public void loadShipTest() {
        JSONObject jsonDescription = JsonStreamReader.read(getClass().getResourceAsStream("BaseShip.json"));
        assertTrue(jsonDescription != null);

        ShipFactory factory = new ShipFactory();
        BaseShip ship = factory.make(jsonDescription, jsonDescription.getString("address"));
        assertTrue(ship != null);

        assertEquals(ship.getModuleAddress(), "ship.1");
        assertEquals(ship.getModuleModel(), "base ship");
    }
}