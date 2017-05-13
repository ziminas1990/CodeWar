package ru.codewar.module.multiplexer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import ru.codewar.module.IBaseModule;
import ru.codewar.module.ModuleTerminal;
import ru.codewar.module.ModuleTerminalFactory;
import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;

public class MultiplexerTests {

    private ModuleTerminalFactory terminalFactoryMock;
    private MultiplexerLogic multiplexerLogic;
    private IBaseModule engineMock;
    private ModuleTerminal engineTerminalMock;
    private IBaseModule rocketMock;
    private ModuleTerminal rocketTerminalMock;
    private Channel channelMock;

    @Before
    public void SetUp() {
        /*
     +--------------------+   +--------------------+
     |     engineMock     |   |     rocketMock     |
     +--------------------+   +--------------------+
               |                         |
     +--------------------+   +--------------------+
     | engineTerminalMock |   | rocketTerminalMock |
     +--------------------+   +--------------------+
               |                         |
               +----------+   +----------+
                          |   |
                  +-------------------+                 +---------------------+
                  |  multiplexerLogic | <-------------> | terminalFactoryMock |
                  +-------------------+                 +---------------------+
                            |
                    +--------------+
                    |  channelMock |
                    +--------------+
         */

        engineMock = mock(IBaseModule.class);
        when(engineMock.getModuleAddress()).thenReturn("ship.engine");
        rocketMock = mock(IBaseModule.class);
        when(rocketMock.getModuleAddress()).thenReturn("ship.rocket");

        engineTerminalMock = mock(ModuleTerminal.class);
        when(engineTerminalMock.getModule()).thenReturn(engineMock);
        rocketTerminalMock = mock(ModuleTerminal.class);
        when(rocketTerminalMock.getModule()).thenReturn(rocketMock);

        terminalFactoryMock = mock(ModuleTerminalFactory.class);
        when(terminalFactoryMock.make(engineMock)).thenReturn(engineTerminalMock);
        when(terminalFactoryMock.make(rocketMock)).thenReturn(rocketTerminalMock);

        channelMock = mock(Channel.class);

        multiplexerLogic = new MultiplexerLogic(terminalFactoryMock);
        multiplexerLogic.addModule(engineMock);
        multiplexerLogic.addModule(rocketMock);
        multiplexerLogic.attachToChannel(channelMock);
    }

    @Test
    public void createVirtualChannel() {
        int engineChannelId = multiplexerLogic.openVirtualChannel("ship.engine");
        verify(engineTerminalMock).attachToChannel(any());

        int rocketChannelId = multiplexerLogic.openVirtualChannel("ship.rocket");
        verify(rocketTerminalMock).attachToChannel(any());

        assertNotEquals(engineChannelId, rocketChannelId);

        try {
            multiplexerLogic.openVirtualChannel("ship.restroom");
        } catch (IllegalArgumentException e) {
            assertEquals("Element ship.restroom NOT found", e.getMessage());
        }

        try {
            multiplexerLogic.openVirtualChannel("ship.engine");
        } catch (IllegalArgumentException e) {
            assertEquals("Module ship.engine is already in use", e.getMessage());
        }
    }

    @Test
    public void closeVirtualChannel() {
        int engineChannelId = multiplexerLogic.openVirtualChannel("ship.engine");
        verify(engineTerminalMock).attachToChannel(any());

        multiplexerLogic.closeVirtualChannel(engineChannelId);

        // Checking, that after the channel was closed, it is possible to
        // open new channel
        multiplexerLogic.openVirtualChannel("ship.engine");
        verify(engineTerminalMock, times(2)).attachToChannel(any());

        // Trying to close a non-existent channel
        try {
            multiplexerLogic.closeVirtualChannel(engineChannelId + 1);
        } catch (IllegalArgumentException e) {
            assertEquals("Virtual channel " + (engineChannelId + 1) + " doesn't exist", e.getMessage());
        }
    }

    @Test
    public void forwardingMessages() {
        int engineChannelId = multiplexerLogic.openVirtualChannel("ship.engine");
        int rocketChannelId = multiplexerLogic.openVirtualChannel("ship.rocket");

        ArgumentCaptor<Channel> engineChannelCaptured = ArgumentCaptor.forClass(Channel.class);
        verify(engineTerminalMock).attachToChannel(engineChannelCaptured.capture());

        ArgumentCaptor<Channel> rocketChannelCaptured = ArgumentCaptor.forClass(Channel.class);
        verify(rocketTerminalMock).attachToChannel(rocketChannelCaptured.capture());

        // Checking forwarding messages from client to module
        multiplexerLogic.forwardingMessage(engineChannelId, new Message("Frame to engine module"));
        verify(engineTerminalMock).onMessageReceived(new Message("Frame to engine module"));

        multiplexerLogic.forwardingMessage(rocketChannelId, new Message("Frame to rocket module"));
        verify(rocketTerminalMock).onMessageReceived(new Message("Frame to rocket module"));

        // Checking forwarding messages from module to client
        engineChannelCaptured.getValue().sendMessage(new Message("Response from engine module"));
        verify(channelMock, times(1)).sendMessage(
                new Message("Response from engine module").addHeader("VC " + engineChannelId));

        rocketChannelCaptured.getValue().sendMessage(new Message("Response from rocket module"));
        verify(channelMock, times(1)).sendMessage(
                new Message("Response from rocket module").addHeader("VC " + rocketChannelId));
    }

}
