package ru.codewar.module.multiplexer;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.ArgumentCaptor;
import ru.codewar.networking.Channel;
import ru.codewar.protocol.module.ModuleOperator;
import ru.codewar.util.ArgumentsReader;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiplexerFullStackTests {

    private Multiplexer multiplexer;
    private MultiplexerController controller;
    private ModuleOperator engineMocked;
    private ModuleOperator rocketMocked;
    private ModuleOperator multiplexerOperator;
    private Channel channelMocked;

    @Before
    public void SetUp() {
        /*
         +------------+             +------------+
         | engineMock |             | rocketMock |
         +------------+             +------------+
           | ^    |                   |      ^ |
           | |    +-------+   +-------+      | |
           v |            |   |              | v
          +----+      +-------------+       +----+
          | VC |<-----| multiplexer |------>| VC |
          +----+      +-------------+       +----+
             |               ^                 |
             |               |                 |
             |    +-----------------------+    |
             |    | multiplexerController |    |
             |    +-----------------------+    |
             |               ^                 |
             |               |                 |
             |    +-----------------------+    |
             |    |  multiplexerOperator  |    |
             |    +-----------------------+    |
             v               |                 v
           +-------------------------------------+
           |            channelMocked            |
           +-------------------------------------+


          "VC" - virtual channels. They would be created by multiplexer after
           openVirtualChannel() function be called
         */

        multiplexer = new Multiplexer();
        controller = new MultiplexerController();
        multiplexerOperator = new MultiplexerOperator("ship.mux");

        engineMocked = mock(ModuleOperator.class);
        when(engineMocked.getAddress()).thenReturn("ship.engine");
        when(engineMocked.getType()).thenReturn("engine");

        rocketMocked = mock(ModuleOperator.class);
        when(rocketMocked.getAddress()).thenReturn("ship.rocket");
        when(rocketMocked.getType()).thenReturn("rocket");

        channelMocked = mock(Channel.class);

        multiplexer.addModule(engineMocked);
        multiplexer.addModule(rocketMocked);
        controller.attachToMultiplexer(multiplexer);
        controller.attachToOperator(multiplexerOperator);
        multiplexerOperator.attachToModuleController(controller);
        multiplexerOperator.attachToChannel(channelMocked);
    }

    @Test
    public void checkIfSupported() {
        assertTrue(MultiplexerController.checkIfSupported("getModulesList"));
        assertTrue(MultiplexerController.checkIfSupported("openVirtualChannel args"));
        assertTrue(MultiplexerController.checkIfSupported("closeVirtualChannel args"));
    }


    @Test
    public void getModulesList() {
        String answer = controller.onRequest(1, "getModulesList");

        ArgumentsReader answerParser = new ArgumentsReader(answer);
        ArrayList<String> modulesList = answerParser.readArray();

        assertEquals(2, modulesList.size());

        assertTrue(modulesList.get(0).equals("ship.engine engine") || modulesList.get(0).equals("ship.rocket rocket"));
        assertTrue(modulesList.get(1).equals("ship.engine engine") || modulesList.get(1).equals("ship.rocket rocket"));
        assertFalse(modulesList.get(0).equals(modulesList.get(1)));
    }

    @Test
    public void openVirtualChannel() {
        multiplexerOperator.onMessageReceived("REQ 1 openVirtualChannel ship.engine");
        verify(engineMocked).attachToChannel(any());

        multiplexerOperator.onMessageReceived("REQ 2 openVirtualChannel ship.rocket");
        verify(rocketMocked).attachToChannel(any());

        multiplexerOperator.onMessageReceived("REQ 3 openVirtualChannel ship.restroom");
        verify(channelMocked).sendMessage("REQ 3 FAILED: Element ship.restroom NOT found");

        multiplexerOperator.onMessageReceived("REQ 4 openVirtualChannel ship.engine");
        verify(channelMocked).sendMessage("REQ 4 FAILED: Module ship.engine is already in use");
    }

    @Test
    public void forwardingMessages() {
        int engineVC = openVirtualChannel("ship.engine");
        int rocketVC = openVirtualChannel("ship.rocket");

        multiplexerOperator.onMessageReceived("VC " + engineVC + ": message to engine");
        multiplexerOperator.onMessageReceived("VC " + rocketVC + ": message to rocket");

        verify(engineMocked).onMessageReceived("message to engine");
        verify(rocketMocked).onMessageReceived("message to rocket");
    }

    @Test
    public void closeVirtualChannel() {
        int engineVC = openVirtualChannel("ship.engine");
        int rocketVC = openVirtualChannel("ship.rocket");

        multiplexerOperator.onMessageReceived("CMD closeVirtualChannel " + engineVC);
        multiplexerOperator.onMessageReceived("CMD closeVirtualChannel " + rocketVC);
        // No any answers in both cases are expected

        multiplexerOperator.onMessageReceived("CMD closeVirtualChannel " + (engineVC + rocketVC));
        verify(channelMocked).sendMessage("CMD FAILED: Virtual channel " + (engineVC + rocketVC) + " doesn't exist");
    }

    private int openVirtualChannel(String address)
    {
        Pattern responseParser = Pattern.compile("RESP\\s+(?<TID>\\w+)\\s+(?<VC>\\w+)");

        multiplexerOperator.onMessageReceived("REQ 100500 openVirtualChannel " + address);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(channelMocked, atLeastOnce()).sendMessage(messageCaptor.capture());
        String response = messageCaptor.getValue();

        Matcher match = responseParser.matcher(response);
        assertTrue(match.matches());
        return Integer.parseInt(match.group("VC"));
    }
}
