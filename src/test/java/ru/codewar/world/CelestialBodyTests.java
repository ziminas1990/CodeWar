package ru.codewar.world;

import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

import ru.codewar.logicconveyor.physicallogic.PhysicalObjectTests;
import ru.codewar.util.JsonStreamReader;

public class CelestialBodyTests {

    @Test
    public void loadStar() {
        JSONObject data = JsonStreamReader.read(getClass().getResourceAsStream("CelestialBodies.json"));
        assertTrue(data != null);

        CelestialBody body = new CelestialBody(data.getJSONObject("Sol"));
        assertEquals(CelestialBody.BodyType.STAR, body.getType());
        assertEquals("Sol", body.getName());
        assertEquals(1.989e30, body.getMass(), 0.01);
        assertEquals(695700000, body.getSignature(), 0.01);
    }

    @Test
    public void toJson() {
        JSONObject data = JsonStreamReader.read(getClass().getResourceAsStream("CelestialBodies.json"));
        assertTrue(data != null);

        CelestialBody body = new CelestialBody(data.getJSONObject("Earth"));
        data = body.toJson();
        assertEquals("Planet", data.getString("type"));
        assertEquals("Earth", data.getString("name"));
        PhysicalObjectTests.checkJsonRepresentation(body, data);
    }

}
