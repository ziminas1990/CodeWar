package ru.codewar.logicconveyor.physicallogic;

import org.json.JSONObject;
import org.junit.Test;
import ru.codewar.geometry.VectorTests;
import ru.codewar.util.JsonStreamReader;

import static org.junit.Assert.*;

public class PhysicalObjectTests {
    @Test
    public void toAndFromJsonTest() {
        JSONObject jsonDescription = JsonStreamReader.read(getClass().getResourceAsStream("PhysicalObject.json"));
        assertTrue(jsonDescription != null);
        PhysicalObject object = PhysicalObject.fromJson(jsonDescription);

        assertEquals(10000, object.getMass(), 10e-6);
        assertEquals(30, object.getSignature(), 10e-6);
        assertEquals(1000, object.getPosition().getX(), 10e-6);
        assertEquals(2000, object.getPosition().getY(), 10e-6);
        assertEquals(50, object.getVelocity().getX(), 10e-6);
        assertEquals(40, object.getVelocity().getY(), 10e-6);

        checkJsonRepresentation(object, object.toJson());
    }

    public static void checkJsonRepresentation(PhysicalObject object, JSONObject json)
    {
        assertEquals(object.getMass(), json.getDouble("mass"), object.getMass()*(10e-6));
        assertEquals(object.getSignature(), json.getDouble("signature"), object.getSignature()*(10e-6));
        VectorTests.checkJsonRepresentation(object.getPosition(), json.getJSONArray("position"));
        VectorTests.checkJsonRepresentation(object.getVelocity(), json.getJSONArray("velocity"));
    }

}
