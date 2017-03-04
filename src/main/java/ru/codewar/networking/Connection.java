package ru.codewar.networking;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

class Connection implements Channel {

    // For receiving messages purpose
    private NetworkTerminal receiver;

    // For sending messages purpose
    private AtomicInteger responsesToSend = new AtomicInteger(0);
    private ArrayList<Message> responses = new ArrayList<>();
    private StringDatagramSocket channel;

    public Connection(StringDatagramSocket channel, NetworkTerminal receiver) throws IOException {
        this.receiver = receiver;
        this.channel = channel;
    }

    @Override // from Channel
    public void sendMessage(Message message) {
        responsesToSend.getAndIncrement();
        responses.add(message);
    }

    public void receiveAllMessages() throws IOException {
        Message message = channel.tryToReceiveMessage();
        while(message != null) {
            receiver.onMessageReceived(message);
            message = channel.tryToReceiveMessage();
        }
    }

    public void sendAllMessages() {
        int totalMessages = responsesToSend.getAndSet(0);
        for(int index = 0; index < totalMessages; index++) {
            try {
                channel.sendMessage(responses.get(index));
            } catch (IOException ioexception) {}
        }
        responses.clear();
    }
}
