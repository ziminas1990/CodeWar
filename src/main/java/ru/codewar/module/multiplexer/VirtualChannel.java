package ru.codewar.module.multiplexer;

import ru.codewar.networking.Channel;
import ru.codewar.networking.NetworkTerminal;

public class VirtualChannel implements Channel, NetworkTerminal {

    private String virtualChannelId;
    private String endPointAddress;
    private NetworkTerminal upLevel;
    private Channel downLevel;

    public VirtualChannel(String virtualChannelId, String endPointAddress) {
        this.virtualChannelId = virtualChannelId;
        this.endPointAddress = endPointAddress;
    }

    public void attachToLogic(NetworkTerminal upLevel) {
        this.upLevel = upLevel;
    }

    public void attachToChannel(Channel channel) {
        this.downLevel = channel;
    }

    public String getEndPointAddress() { return endPointAddress; }

    public void onMessageReceived(String message)
    {
        // Just passing message to upLevel logic
        upLevel.onMessageReceived(message);
    }

    public void sendMessage(String message)
    {
        // Adding to message a header with virtual channel id
        downLevel.sendMessage("vc " + virtualChannelId + " [" + message + "]");
    }

}
