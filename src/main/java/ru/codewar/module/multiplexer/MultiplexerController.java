package ru.codewar.module.multiplexer;

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

    private Multiplexer multiplexer;
    private ModuleOperator operator;

    public static boolean checkIfSupported(String message) {
        return checkPattern.matcher(message).matches();
    }

    public void attachToMultiplexer(Multiplexer multiplexer) { this.multiplexer = multiplexer; }

    public void attachToOperator(ModuleOperator operator) { this.operator = operator; }

    public void onCommand(String command)
    {
        if(multiplexer == null) {
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

    public String onRequest(Integer transactionId, String request)
    {
        if(multiplexer == null) {
            operator.onRequestFailed(transactionId, "controller NOT attached to module");
            return "";
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
        return "";
    }

    public void forwardingMessage(Integer virtualChannelId, String message)
    {
        multiplexer.forwardingMessage(virtualChannelId, message);
    }

    private String onOpenVirtualChannelRequest(Integer transactionId, String address)
    {
        try {
            return Integer.toString(multiplexer.openVirtualChannel(address));
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
            multiplexer.closeVirtualChannel(channelId);
        } catch (IllegalArgumentException e) {
            operator.onCommandFailed(e.getMessage());
        }
    }

    private String onGetAllModulesRequest()
    {
        Map<String, ModuleOperator> allModules = multiplexer.getAllModules();
        String response = "{";
        for(ModuleOperator module : allModules.values()) {
            response += "[" + module.getAddress() + " " + module.getType() + "] ";
        }
        response += "}";
        return response;
    }
}
