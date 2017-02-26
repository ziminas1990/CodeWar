package ru.codewar.world;


import org.junit.Test;
import ru.codewar.networking.Message;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class PlayerGateTests {

    @Test
    public void successfulLoginTest() {
        Player player = new Player("test");
        World world = mock(World.class);
        PlayerGate gate = new PlayerGate(world);

        gate.addPlayer("ZiminAS1990", "shafto");
        when(world.getPlayer("ZiminAS1990")).thenReturn(player);

        assertEquals(player, gate.login(new Message(" ZiminAS1990    shafto  ")));
    }

}
