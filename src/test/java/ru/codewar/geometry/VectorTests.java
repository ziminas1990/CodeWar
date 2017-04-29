package ru.codewar.geometry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import ru.codewar.util.JsonStreamReader;

import static org.junit.Assert.*;

public class VectorTests {

    @Test
    public void fromAndToJsonTest() {
        JSONObject data = JsonStreamReader.read(getClass().getResourceAsStream("Vector.json"));
        assertTrue(data != null && data.has("vector"));
        Vector vector = new Vector(data.getJSONArray("vector"));
        assertEquals(100, vector.getX(), 10e-6);
        assertEquals(200, vector.getY(), 10e-6);

        checkJsonRepresentation(vector, vector.toJson());
    }

    public static void checkJsonRepresentation(Vector vector, JSONArray json)
    {
        assertEquals(vector.getX(), json.getDouble(0), vector.getX() * 10e-6);
        assertEquals(vector.getY(), json.getDouble(1), vector.getY() * 10e-6);
    }

    public static void checkJsonRepresentation(Point point, JSONArray json)
    {
        assertEquals(point.getX(), json.getDouble(0), point.getX() * 10e-6);
        assertEquals(point.getY(), json.getDouble(1), point.getY() * 10e-6);
    }

}
