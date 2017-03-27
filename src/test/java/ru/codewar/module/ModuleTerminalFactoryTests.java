package ru.codewar.module;

import org.json.JSONObject;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import ru.codewar.module.engine.EngineController;
import ru.codewar.module.engine.EngineModule;
import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;

import static org.junit.Assert.*;

public class ModuleTerminalFactoryTests {

    @Test
    public void makeEngineTest() {
        EngineModule module = mock(EngineModule.class);
        ModuleTerminalFactory factory = new ModuleTerminalFactory();
        ModuleTerminal terminal = factory.make(module);

        assertTrue(terminal.getController() instanceof EngineController);
        checkTerminal(terminal, module);
    }

    private void checkTerminal(ModuleTerminal terminal, BaseModuleInterface mockedModule) {
        // Checking that terminal works correctly using BaseModuleInterface protocol

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