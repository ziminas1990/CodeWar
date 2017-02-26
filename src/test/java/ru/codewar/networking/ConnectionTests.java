package ru.codewar.networking;

import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class ConnectionTests {

    @Test
    public void checkSendingMessages() throws IOException {
        Message[] messages = {
                new Message("Message 1"),
                new Message("Message 2"),
                new Message("Message 3")
        };

        StringDatagramSocket socketMocked = mock(StringDatagramSocket.class);

        Connection connection = new Connection(socketMocked, null);
        connection.sendMessage(messages[0]);
        connection.sendMessage(messages[1]);
        connection.sendMessage(messages[2]);
        connection.sendAllMessages();

        verify(socketMocked).sendMessage(messages[0]);
        verify(socketMocked).sendMessage(messages[1]);
        verify(socketMocked).sendMessage(messages[2]);
    }

    @Test
    public void receivingMessagesTests() throws IOException {
        Message[] messages = {
                new Message("Message 1"),
                new Message("Message 2"),
                new Message("Message 3")
        };

        StringDatagramSocket socketMocked = mock(StringDatagramSocket.class);
        NetworkTerminal terminalMocked = mock(NetworkTerminal.class);

        Connection connection = new Connection(socketMocked, terminalMocked);

        when(socketMocked.tryToReceiveMessage()).thenReturn(messages[0]).thenReturn(null);
        connection.receiveAllMessages();
        verify(terminalMocked).onMessageReceived(messages[0]);

        when(socketMocked.tryToReceiveMessage()).thenReturn(messages[1]).thenReturn(messages[2]).thenReturn(null);
        connection.receiveAllMessages();
        verify(terminalMocked).onMessageReceived(messages[1]);
        verify(terminalMocked).onMessageReceived(messages[2]);
    }

}
