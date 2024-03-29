package ru.codewar.module.multiplexer;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.ArgumentCaptor;
import ru.codewar.module.IBaseModule;
import ru.codewar.module.IModulesFactory;
import ru.codewar.module.ModuleTerminal;
import ru.codewar.module.ModuleTerminalFactory;
import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleOperator;
import ru.codewar.util.ArgumentsReader;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiplexerFullStackTests {


    private MultiplexerLogic multiplexerLogic;
    private MultiplexerController controller;
    private ModuleOperator multiplexerOperator;

    private IBaseModule engineMocked;
    private IBaseModule rocketMocked;

    private IModulesFactory moduleFactoryMock;
    private ModuleTerminal engineTerminalMock;
    private ModuleTerminal rocketTerminalMock;

    private Channel channelMocked;

    @Before
    public void SetUp() {
        /*
         +------------+             +------------+
         | engineMock |             | rocketMock |
         +------------+             +------------+
           | A     |                   |     A |
           | |     +-------+   +-------+     | |
           V |             |   |             | V
          +----+    +------------------+    +----+
          | VC |<---| multiplexerLogic |--->| VC |
          +----+    +------------------+    +----+
             |               A                 |
             |               |                 |
             |    +-----------------------+    |
             |    | multiplexerController |    |
             |    +-----------------------+    |
             |               A                 |
             |               |                 |
             |    +-----------------------+    |
             |    |  multiplexerOperator  |    |
             |    +-----------------------+    |
             V               |                 V
           +-------------------------------------+
           |            channelMocked            |
           +-------------------------------------+


          "VC" - virtual channels. They would be created by multiplexerLogic after
           openVirtualChannel() function be called
         */

        engineMocked = mock(IBaseModule.class);
        when(engineMocked.getModuleAddress()).thenReturn("ship.engine");
        when(engineMocked.getModuleType()).thenReturn("engine");

        rocketMocked = mock(IBaseModule.class);
        when(rocketMocked.getModuleAddress()).thenReturn("ship.rocket");
        when(rocketMocked.getModuleType()).thenReturn("rocket");

        engineTerminalMock = mock(ModuleTerminal.class);
        when(engineTerminalMock.getModule()).thenReturn(engineMocked);
        rocketTerminalMock = mock(ModuleTerminal.class);
        when(rocketTerminalMock.getModule()).thenReturn(rocketMocked);
        moduleFactoryMock = mock(IModulesFactory.class);
        when(moduleFactoryMock.makeTerminal(engineMocked)).thenReturn(engineTerminalMock);
        when(moduleFactoryMock.makeTerminal(rocketMocked)).thenReturn(rocketTerminalMock);

        channelMocked = mock(Channel.class);

        multiplexerLogic = new MultiplexerLogic(moduleFactoryMock);
        controller = new MultiplexerController();
        multiplexerOperator = new MultiplexerOperator();

        multiplexerLogic.addModule(engineMocked);
        multiplexerLogic.addModule(rocketMocked);
        controller.attachToMultiplexer(multiplexerLogic);
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
        Message answer = controller.onRequest(1, "getModulesList");

        ArgumentsReader answerParser = new ArgumentsReader(answer.data);
        ArrayList<String> modulesList = answerParser.readArray();

        assertEquals(2, modulesList.size());

        assertTrue(modulesList.get(0).equals("ship.engine engine") || modulesList.get(0).equals("ship.rocket rocket"));
        assertTrue(modulesList.get(1).equals("ship.engine engine") || modulesList.get(1).equals("ship.rocket rocket"));
        assertFalse(modulesList.get(0).equals(modulesList.get(1)));
    }

    @Test
    public void openVirtualChannel() {
        multiplexerOperator.onMessageReceived(new Message("openVirtualChannel ship.engine").addHeader("REQ 1"));
        verify(engineTerminalMock).attachToChannel(any());

        multiplexerOperator.onMessageReceived(new Message("openVirtualChannel ship.rocket").addHeader("REQ 2"));
        verify(rocketTerminalMock).attachToChannel(any());

        multiplexerOperator.onMessageReceived(new Message("openVirtualChannel ship.restroom").addHeader("REQ 3"));
        verify(channelMocked).sendMessage(new Message("REQ 3 FAILED: Element ship.restroom NOT found"));

        multiplexerOperator.onMessageReceived(new Message("openVirtualChannel ship.engine").addHeader("REQ 4"));
        verify(channelMocked).sendMessage(new Message("REQ 4 FAILED: Module ship.engine is already in use"));
    }

    @Test
    public void forwardingMessages() {
        int engineVC = openVirtualChannel("ship.engine");
        int rocketVC = openVirtualChannel("ship.rocket");

        multiplexerOperator.onMessageReceived(new Message("VC " + engineVC + " message to engine"));
        multiplexerOperator.onMessageReceived(new Message("VC " + rocketVC + " message to rocket"));

        verify(engineTerminalMock).onMessageReceived(new Message("message to engine"));
        verify(rocketTerminalMock).onMessageReceived(new Message("message to rocket"));
    }

    @Test
    public void closeVirtualChannel() {
        int engineVC = openVirtualChannel("ship.engine");
        int rocketVC = openVirtualChannel("ship.rocket");

        multiplexerOperator.onMessageReceived(new Message("CMD closeVirtualChannel " + engineVC));
        multiplexerOperator.onMessageReceived(new Message("CMD closeVirtualChannel " + rocketVC));
        // No any answers in both cases are expected

        multiplexerOperator.onMessageReceived(new Message("CMD closeVirtualChannel " + (engineVC + rocketVC)));
        verify(channelMocked).sendMessage(
                new Message("CMD FAILED: Virtual channel " + (engineVC + rocketVC) + " doesn't exist"));
    }

    private int openVirtualChannel(String address)
    {
        Pattern responseParser = Pattern.compile("RESP\\s+(?<TID>\\w+)\\s+(?<VC>\\w+)");

        multiplexerOperator.onMessageReceived(
                new Message("REQ 100500 openVirtualChannel " + address));
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(channelMocked, atLeastOnce()).sendMessage(messageCaptor.capture());
        Message response = messageCaptor.getValue();

        Matcher match = responseParser.matcher(response.data);
        assertTrue(match.matches());
        return Integer.parseInt(match.group("VC"));
    }
}
