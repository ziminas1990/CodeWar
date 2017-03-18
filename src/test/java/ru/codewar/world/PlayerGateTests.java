package ru.codewar.world;


import org.junit.Test;
import ru.codewar.database.playerloader.PlayerLoader;
import ru.codewar.networking.Message;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class PlayerGateTests {

    @Test
    public void successfulLoginTest() {
        Player player = new Player("test");
        World worldMocked = mock(World.class);
        PlayerLoader playerLoaderMocked = mock(PlayerLoader.class);

        PlayerGate gate = new PlayerGate(worldMocked, playerLoaderMocked);
        gate.addPlayer("ZiminAS1990", "shafto");

        when(worldMocked.getPlayer("ZiminAS1990")).thenReturn(null);
        when(playerLoaderMocked.loadPlayer("ZiminAS1990")).thenReturn(player);

        assertEquals(player, gate.login(new Message(" ZiminAS1990    shafto  ")));
        verify(worldMocked).getPlayer("ZiminAS1990");
        verify(playerLoaderMocked).loadPlayer("ZiminAS1990");
    }

}
