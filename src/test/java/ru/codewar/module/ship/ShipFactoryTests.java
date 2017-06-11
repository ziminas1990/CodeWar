package ru.codewar.module.ship;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import ru.codewar.module.IBaseModule;
import ru.codewar.module.IModulesFactory;
import ru.codewar.util.JsonStreamReader;

public class ShipFactoryTests {

    IModulesFactory mockedFactory = mock(IModulesFactory.class);
    ShipLoader shipLoader = new ShipLoader(mockedFactory);

    @Test
    public void loadShipTest() {
        JSONObject jsonDescription = JsonStreamReader.read(getClass().getResourceAsStream("BaseShip.json"));
        assertTrue(jsonDescription != null);

        IBaseModule module = shipLoader.makeModule(
                jsonDescription.getString("type"),
                jsonDescription.getString("model"),
                "zimin.ship.1",
                jsonDescription.getJSONObject("parameters"));
        assertNotEquals(null, module);
        assertTrue(module instanceof BaseShip);
        BaseShip ship = (BaseShip)module;

        // test that ship loader tried to load ship modules
        JSONArray modules = jsonDescription.getJSONObject("parameters").getJSONArray("modules");
        for(int i = 0; i < 5; i++)
            verify(mockedFactory, times(1)).make(eq(modules.getJSONObject(i)), any(BaseShip.class));

        assertEquals(ship.getModuleAddress(), "zimin.ship.1");
        assertEquals(ship.getModuleModel(), "base ship");
    }
}