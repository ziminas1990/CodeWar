package ru.codewar.utils;

import org.junit.Test;
import ru.codewar.util.ArgumentsReader;

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

}
