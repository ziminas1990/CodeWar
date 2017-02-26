package ru.codewar.networking;

public interface NetworkTerminal {
    void onMessageReceived(Message message);

    void attachToChannel(Channel channel);
}
