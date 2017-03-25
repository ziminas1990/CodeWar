package ru.codewar.protocol.module;

import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;
import ru.codewar.networking.NetworkTerminal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModuleOperator implements NetworkTerminal {

    private final Pattern requestPattern = Pattern.compile("REQ\\s+(\\w+)\\s+(.*)");
    private final Pattern commandPattern = Pattern.compile("CMD\\s(.*)");

    private ModuleController module;
    private Channel channel;

    public void attachToModuleController(ModuleController module) {
        this.module = module;
    }

    public void attachToChannel(Channel channel) {
        this.channel = channel;
    }

    @Override // from NetworkTerminal
    public void onMessageReceived(Message message) {
        if(module == null)
            return;
        // Received a message, we should read it header and call an
        // appropriate function of the module
        Matcher result = requestPattern.matcher(message.data);
        if (result.matches()) {
            Integer transactionId = Integer.valueOf(result.group(1));
            Message response = module.onRequest(transactionId, result.group(2));
            if (response != null) {
                // Got response immediately - sending it to client
                onResponse(transactionId, response);
            }
        } else {
            result = commandPattern.matcher(message.data);
            if (result.matches()) {
                module.onCommand(result.group(1));
            }
        }
    }

    public void onResponse(int transactionId, Message response) {
        // Adding header to response and send it via channel
        channel.sendMessage(response.addHeader("RESP " + transactionId));
    }

    public void onIndication(Message indication) {
        // Adding header to response and send it via channel
        channel.sendMessage(indication.addHeader("IND"));
    }

    public void onCommandFailed(String details) {
        channel.sendMessage(new Message("CMD FAILED: " + details));
    }

    public void onRequestFailed(int transactionId, String details) {
        channel.sendMessage(new Message("REQ " + transactionId + " FAILED: " + details));
    }

}
