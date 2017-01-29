package ru.codewar.networking;

import java.net.DatagramPacket;

public interface AbstractSocket {
    String receiveMessage();
    void sendMessage(String message);

}
