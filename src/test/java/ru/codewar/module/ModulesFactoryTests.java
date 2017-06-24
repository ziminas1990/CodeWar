package ru.codewar.module;

import org.json.JSONObject;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import ru.codewar.module.engine.BaseEngine;
import ru.codewar.module.engine.EngineController;
import ru.codewar.module.engine.EngineModule;
import ru.codewar.module.ship.BaseShip;
import ru.codewar.module.ship.ShipController;
import ru.codewar.module.ship.ShipModule;
import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;

import static org.junit.Assert.*;

public class ModulesFactoryTests {

    @Test
    public void makeEngineTerminalTest() {
        EngineModule module = mock(EngineModule.class);
        when(module.getModuleType()).thenReturn(BaseEngine.moduleType);
        when(module.getModuleModel()).thenReturn(BaseEngine.moduleModel);

        IModulesFactory factory = new ModulesFactory();
        ModuleTerminal terminal = factory.makeTerminal(module);

        assertNotEquals(null, terminal);
        assertTrue(terminal.getController() instanceof EngineController);
        checkTerminal(terminal, module);
    }

    @Test
    public void makeShipTerminalTest() {
        ShipModule module = mock(ShipModule.class);
        when(module.getModuleType()).thenReturn(BaseShip.moduleType);
        when(module.getModuleModel()).thenReturn(BaseShip.moduleModel);

        IModulesFactory factory = new ModulesFactory();
        ModuleTerminal terminal = factory.makeTerminal(module);

        assertNotEquals(null, terminal);
        assertTrue(terminal.getController() instanceof ShipController);
        checkTerminal(terminal, module);
    }

    // Checking that terminal works correctly using IBaseModule protocol
    private void checkTerminal(ModuleTerminal terminal, IBaseModule mockedModule) {

        Channel channelMocked = mock(Channel.class);
        terminal.attachToChannel(channelMocked);

        // Check how terminal transmit requests and response, via sending getModuleInfo request
        when(mockedModule.getModuleAddress()).thenReturn("Test address");
        when(mockedModule.getModuleType()).thenReturn("Test Type");
        when(mockedModule.getModuleModel()).thenReturn("Test Model");
        when(mockedModule.getModuleInfo()).thenReturn("{\"test\" : \"value\"}");

        terminal.onMessageReceived(new Message("REQ 1 getModuleInfo"));

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(channelMocked).sendMessage(messageCaptor.capture());

        String response = messageCaptor.getValue().data;
        response = response.replaceFirst("RESP\\s+\\d+\\s*", ""); // Removing response header

        JSONObject info = new JSONObject(response);
        assertEquals(info.getString("address"), "Test address");
        assertEquals(info.getString("type"), "Test Type");
        assertEquals(info.getString("model"), "Test Model");
        assertEquals(info.getJSONObject("parameters").get("test"), "value");

    }

}
