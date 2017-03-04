package ru.codewar.module.multiplexer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleOperator;

public class MultiplexerTests {

    private MultiplexerLogic multiplexerLogic;
    private ModuleOperator engineMock;
    private ModuleOperator rocketMock;
    private Channel channelMock;

    @Before
    public void SetUp() {
        /*
            +------------+     +------------+
            | engineMock |     | rocketMock |
            +------------+     +------------+
                  |                   |
                  +-------+   +-------+
                          |   |
                  +-------------------+
                  |  multiplexerLogic |
                  +-------------------+
                            |
                    +--------------+
                    |  channelMock |
                    +--------------+
         */

        engineMock = mock(ModuleOperator.class);
        when(engineMock.getAddress()).thenReturn("ship.engine");

        rocketMock = mock(ModuleOperator.class);
        when(rocketMock.getAddress()).thenReturn("ship.rocket");

        channelMock = mock(Channel.class);

        multiplexerLogic = new MultiplexerLogic();
        multiplexerLogic.addModule(engineMock);
        multiplexerLogic.addModule(rocketMock);
        multiplexerLogic.attachToChannel(channelMock);
    }

    @Test
    public void createVirtualChannel() {
        int engineChannelId = multiplexerLogic.openVirtualChannel("ship.engine");
        verify(engineMock).attachToChannel(any());

        int rocketChannelId = multiplexerLogic.openVirtualChannel("ship.rocket");
        verify(rocketMock).attachToChannel(any());

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
        verify(engineMock).attachToChannel(any());

        multiplexerLogic.closeVirtualChannel(engineChannelId);

        // Checking, that after the channel was closed, it is possible to
        // open new channel
        multiplexerLogic.openVirtualChannel("ship.engine");
        verify(engineMock, times(2)).attachToChannel(any());

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
        verify(engineMock).attachToChannel(engineChannelCaptured.capture());

        ArgumentCaptor<Channel> rocketChannelCaptured = ArgumentCaptor.forClass(Channel.class);
        verify(rocketMock).attachToChannel(rocketChannelCaptured.capture());

        // Checking forwarding messages from client to module
        multiplexerLogic.forwardingMessage(engineChannelId, new Message("Frame to engine module"));
        verify(engineMock).onMessageReceived(new Message("Frame to engine module"));

        multiplexerLogic.forwardingMessage(rocketChannelId, new Message("Frame to rocket module"));
        verify(rocketMock).onMessageReceived(new Message("Frame to rocket module"));

        // Checking forwarding messages from module to client
        engineChannelCaptured.getValue().sendMessage(new Message("Response from engine module"));
        verify(channelMock, times(1)).sendMessage(
                new Message("Response from engine module").addHeader("VC " + engineChannelId));

        rocketChannelCaptured.getValue().sendMessage(new Message("Response from rocket module"));
        verify(channelMock, times(1)).sendMessage(
                new Message("Response from rocket module").addHeader("VC " + rocketChannelId));
    }

}
