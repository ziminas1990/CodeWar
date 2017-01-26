package ru.codewar.protocol.module;

import ru.codewar.networking.Channel;
import ru.codewar.networking.NetworkTerminal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModuleOperator implements NetworkTerminal {

    private final Pattern requestPattern = Pattern.compile("REQ\\s+(\\w+)\\s+(.*)");
    private final Pattern commandPattern = Pattern.compile("CMD\\s(.*)");

    private String address;
    private String type;
    private ModuleController module;
    private Channel channel;

    ModuleOperator() {}
    ModuleOperator(String address, String type) {
        this.address = address;
        this.type = type;
    }

    public String getAddress() { return address; }
    public String getType() { return type; }

    public void attachToModuleController(ModuleController module) {
        this.module = module;
    }

    public void attachToChannel(Channel channel) {
        this.channel = channel;
    }

    public void onMessageReceived(String message) {
        // Received a message, we should read it header and call an
        // appropriate function of the module
        Matcher result = requestPattern.matcher(message);
        if (result.matches()) {
            Integer transactionId = Integer.valueOf(result.group(1));
            String response = module.onRequest(transactionId, result.group(2));
            if (response != null) {
                // Got response immediately - sending it to client
                onResponse(transactionId, response);
            }
        } else {
            result = commandPattern.matcher(message);
            if (result.matches()) {
                module.onCommand(result.group(1));
            }
        }
    }

    public void onResponse(int transactionId, String response) {
        // Adding header to response and send it via channel
        channel.sendMessage("RESP " + transactionId + " " + response);
    }

    public void onIndication(String indication) {
        // Adding header to response and send it via channel
        channel.sendMessage("IND " + indication);
    }

}
