package ru.codewar.module.multiplexer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;
import ru.codewar.networking.Channel;
import ru.codewar.protocol.module.ModuleOperator;

public class MultiplexerTests {

    private Multiplexer multiplexer;
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
                    +--------------+
                    |  multiplexer |
                    +--------------+
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

        multiplexer = new Multiplexer();
        multiplexer.addModule(engineMock);
        multiplexer.addModule(rocketMock);
        multiplexer.attachToChannel(channelMock);
    }

    @Test
    public void createVirtualChannel() {
        int engineChannelId = multiplexer.openVirtualChannel("ship.engine");
        verify(engineMock).attachToChannel(any());

        int rocketChannelId = multiplexer.openVirtualChannel("ship.rocket");
        verify(rocketMock).attachToChannel(any());

        assertNotEquals(engineChannelId, rocketChannelId);

        try {
            multiplexer.openVirtualChannel("ship.restroom");
        } catch (IllegalArgumentException e) {
            assertEquals("Element ship.restroom NOT found!", e.getMessage());
        }

        try {
            multiplexer.openVirtualChannel("ship.engine");
        } catch (IllegalArgumentException e) {
            assertEquals("Module ship.engine is already in use", e.getMessage());
        }
    }

    @Test
    public void closeVirtualChannel() {
        int engineChannelId = multiplexer.openVirtualChannel("ship.engine");
        verify(engineMock).attachToChannel(any());

        multiplexer.closeVirtualChannel(engineChannelId);

        // Checking, that after the channel was closed, it is possible to
        // open new channel
        multiplexer.openVirtualChannel("ship.engine");
        verify(engineMock, times(2)).attachToChannel(any());

        // Trying to close a non-existent channel
        try {
            multiplexer.closeVirtualChannel(engineChannelId + 1);
        } catch (IllegalArgumentException e) {
            assertEquals("Virtual channel " + (engineChannelId + 1) + " doesn't exist!", e.getMessage());
        }
    }

    @Test
    public void forwardingMessages() {
        int engineChannelId = multiplexer.openVirtualChannel("ship.engine");
        int rocketChannelId = multiplexer.openVirtualChannel("ship.rocket");

        ArgumentCaptor<Channel> engineChannelCaptured = ArgumentCaptor.forClass(Channel.class);
        verify(engineMock).attachToChannel(engineChannelCaptured.capture());

        ArgumentCaptor<Channel> rocketChannelCaptured = ArgumentCaptor.forClass(Channel.class);
        verify(rocketMock).attachToChannel(rocketChannelCaptured.capture());

        // Checking forwarding messages from client to module
        multiplexer.onMessageReceived(engineChannelId, "Message to engine module");
        verify(engineMock).onMessageReceived("Message to engine module");

        multiplexer.onMessageReceived(rocketChannelId, "Message to rocket module");
        verify(rocketMock).onMessageReceived("Message to rocket module");

        // Checking forwarding messages from module to client
        engineChannelCaptured.getValue().sendMessage("Response from engine module");
        verify(channelMock, times(1)).sendMessage("vc " + engineChannelId + " [Response from engine module]");

        rocketChannelCaptured.getValue().sendMessage("Response from rocket module");
        verify(channelMock, times(1)).sendMessage("vc " + rocketChannelId + " [Response from rocket module]");
    }

}
