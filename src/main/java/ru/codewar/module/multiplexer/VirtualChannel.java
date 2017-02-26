package ru.codewar.module.multiplexer;

import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;
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

    @Override // from NetworkTerminal
    public void attachToChannel(Channel channel) {
        this.downLevel = channel;
    }

    public String getEndPointAddress() { return endPointAddress; }

    @Override // from onMessageReceived
    public void onMessageReceived(Message message)
    {
        // Just passing message to upLevel logic
        upLevel.onMessageReceived(message);
    }

    @Override // from Channel
    public void sendMessage(Message message)
    {
        // Adding to message a header with virtual channel id
        downLevel.sendMessage(message.addHeader("VC " + virtualChannelId));
    }

}
