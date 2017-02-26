package ru.codewar.networking;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class StringDatagramSocket {

    private DatagramChannel channel = DatagramChannel.open();
    private ByteBuffer readBuffer = ByteBuffer.allocateDirect(65507);
    private CharsetDecoder decoder = Charset.forName("ascii").newDecoder();
    private CharsetEncoder encoder = Charset.forName("ascii").newEncoder();
    private SocketAddress client;
    private SocketAddress lastReceivedFrameSource;

    public StringDatagramSocket(SocketAddress local, SocketAddress client) throws IOException {
        channel.configureBlocking(false);
        channel.socket().bind(local);
        if(client != null) {
            channel.socket().connect(client);
        }
        this.client = client;
    }

    public Message tryToReceiveMessage() {
        try {
            readBuffer.clear();
            lastReceivedFrameSource = channel.receive(readBuffer);
            if (lastReceivedFrameSource != null) {
                readBuffer.flip();
                return new Message(decoder.decode(readBuffer).toString());
            } else {
                return null;
            }
        } catch (IOException exception) {
            return null;
        }
    }

    public SocketAddress lastReceivedFrameSource() {
        return lastReceivedFrameSource;
    }

    public void sendMessage(Message message) throws IOException {
        if(client == null)
            throw new IOException("Client address wasn't provided via StringDatagramSocket constructor!");
        sendMessage(message, client);
    }

    public void sendMessage(Message message, SocketAddress destination) throws IOException {
        try {
            ByteBuffer bytes = encoder.encode(CharBuffer.wrap(message.data.toCharArray()));
            //bytes.flip();
            channel.send(bytes, destination);
        } catch (IOException exception) {}
    }

}
