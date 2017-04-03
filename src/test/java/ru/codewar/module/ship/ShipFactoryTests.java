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
        BaseShip ship = factory.make(jsonDescription, "zimin");
        assertTrue(ship != null);

        assertEquals(ship.getModuleAddress(), "zimin.ship.1");
        assertEquals(ship.getModuleModel(), "base ship");
    }
}