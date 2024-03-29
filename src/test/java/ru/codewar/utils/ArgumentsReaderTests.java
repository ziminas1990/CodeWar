package ru.codewar.utils;

import org.junit.Test;
import ru.codewar.util.ArgumentsReader;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Александр on 14.12.2016.
 */
public class ArgumentsReaderTests {

    @Test
    public void readIntegerValuesTest() {
        ArgumentsReader reader = new ArgumentsReader("123  -456  ");
        assertEquals(Integer.valueOf("123"), reader.readInteger());
        assertEquals(Integer.valueOf("-456"), reader.readInteger());
        assertTrue(reader.getTail().isEmpty());

        reader = new ArgumentsReader("d123");
        assertEquals(null, reader.readInteger());
        assertEquals("d123", reader.getTail());
    }

    @Test
    public void readDoubleValuesTest() {
        ArgumentsReader reader = new ArgumentsReader("123 333.333 -10 +11.1 4.1e2 -6.6e-3  ");
        assertEquals(Double.valueOf("123"), reader.readDouble());
        assertEquals(Double.valueOf("333.333"), reader.readDouble());
        assertEquals(Double.valueOf("-10"), reader.readDouble());
        assertEquals(Double.valueOf("+11.1"), reader.readDouble());
        assertEquals(Double.valueOf("4.1e2"), reader.readDouble());
        assertEquals(Double.valueOf("-6.6e-3"), reader.readDouble());
        assertTrue(reader.getTail().isEmpty());

        reader = new ArgumentsReader("d123");
        assertEquals(null, reader.readDouble());
        assertEquals("d123", reader.getTail());
    }

    @Test
    public void readEmptyArrayTest() {
        ArgumentsReader reader = new ArgumentsReader("{ }");
        ArrayList<String> result;

        result = reader.readArray();
        assertEquals(0, result.size());
    }

    @Test
    public void readArrayTest() {
        ArgumentsReader reader =
                new ArgumentsReader("{ [element_1] [ { [ element_2_1 ] [ element_2_2 ] } ] [ element_2 ] }");
        ArrayList<String> result;

        result = reader.readArray();
        assertEquals(3, result.size());
        assertEquals("element_1", result.get(0));
        assertEquals("{ [ element_2_1 ] [ element_2_2 ] }", result.get(1));
        assertEquals("element_2", result.get(2));
    }

}
