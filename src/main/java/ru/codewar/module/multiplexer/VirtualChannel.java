package ru.codewar.module.multiplexer;

import ru.codewar.module.ModuleTerminal;
import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;
import ru.codewar.networking.NetworkTerminal;

public class VirtualChannel implements Channel, NetworkTerminal {

    private String virtualChannelId;
    private ModuleTerminal terminal;
    private Channel downLevel;

    public VirtualChannel(String virtualChannelId) {
        this.virtualChannelId = virtualChannelId;
    }

    public ModuleTerminal getTerminal() { return terminal; }

    public void attachToLogic(ModuleTerminal upLevel) {
        this.terminal = upLevel;
        this.terminal.attachToChannel(this);
    }

    @Override // from NetworkTerminal
    public void attachToChannel(Channel channel) {
        this.downLevel = channel;
    }

    @Override // from NetworkTerminal
    public void onMessageReceived(Message message)
    {
        // Just passing message to terminal logic
        terminal.onMessageReceived(message);
    }

    @Override // from Channel
    public void sendMessage(Message message)
    {
        // Adding to message a header with virtual channel id
        downLevel.sendMessage(message.addHeader("VC " + virtualChannelId));
    }

}
