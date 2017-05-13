package ru.codewar.module.multiplexer;

import ru.codewar.module.IBaseModule;
import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleController;
import ru.codewar.protocol.module.ModuleOperator;
import ru.codewar.util.ArgumentsReader;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiplexerController implements ModuleController {

    private final static Pattern checkPattern =
            Pattern.compile("(openVirtualChannel\\s+.*|closeVirtualChannel\\s+.*|getModulesList)");
    private final static Pattern openPattern = Pattern.compile("openVirtualChannel\\s+([\\w.]+)");
    private final static Pattern closePattern = Pattern.compile("closeVirtualChannel\\s+(\\d+)");
    private final static Pattern getModulesListPattern = Pattern.compile("getModulesList");

    private MultiplexerLogic multiplexerLogic;
    private ModuleOperator operator;

    public static boolean checkIfSupported(String message) {
        return checkPattern.matcher(message).matches();
    }

    public void attachToMultiplexer(MultiplexerLogic multiplexerLogic) { this.multiplexerLogic = multiplexerLogic; }

    public void attachToOperator(ModuleOperator operator) { this.operator = operator; }

    public void onCommand(String command)
    {
        if(multiplexerLogic == null) {
            operator.onCommandFailed("controller NOT attached to module");
            return;
        }

        Matcher match;
        match = closePattern.matcher(command);
        if(match.matches()) {
            onCloseVirtualChannelCommand(new ArgumentsReader(match.group(1)));
            return;
        }
        operator.onCommandFailed("invalid command");
    }

    public Message onRequest(Integer transactionId, String request)
    {
        if(multiplexerLogic == null) {
            operator.onRequestFailed(transactionId, "controller NOT attached to module");
            return null;
        }

        Matcher match;
        match = openPattern.matcher(request);
        if(match.matches()) {
            return onOpenVirtualChannelRequest(transactionId, match.group(1));
        }

        match = getModulesListPattern.matcher(request);
        if(match.matches()) {
            return onGetAllModulesRequest();
        }

        operator.onRequestFailed(transactionId, "invalid request");
        return null;
    }

    public void forwardingMessage(Integer virtualChannelId, Message message)
    {
        multiplexerLogic.forwardingMessage(virtualChannelId, message);
    }

    private Message onOpenVirtualChannelRequest(Integer transactionId, String address)
    {
        try {
            return new Message(Integer.toString(multiplexerLogic.openVirtualChannel(address)));
        } catch (IllegalArgumentException e) {
            operator.onRequestFailed(transactionId, e.getMessage());
            return null;
        }
    }

    private void onCloseVirtualChannelCommand(ArgumentsReader arguments)
    {
        Integer channelId = arguments.readInteger();
        if(channelId == null) {
            operator.onCommandFailed("incorrect channel id value");
        }
        try {
            multiplexerLogic.closeVirtualChannel(channelId);
        } catch (IllegalArgumentException e) {
            operator.onCommandFailed(e.getMessage());
        }
    }

    private Message onGetAllModulesRequest()
    {
        Map<String, IBaseModule> allModules = multiplexerLogic.getAllModules();
        String response = "{";
        for(IBaseModule module : allModules.values()) {
            response += "[" + module.getModuleAddress() + " " + module.getModuleType() + "] ";
        }
        response += "}";
        return new Message(response);
    }
}
