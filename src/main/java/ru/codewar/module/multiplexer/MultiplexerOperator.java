package ru.codewar.module.multiplexer;

import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleController;
import ru.codewar.protocol.module.ModuleOperator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MultiplexerLogic operator extends base operations list REQ/CMD, and add new operation "VC",
 * that means "VirtualChannel". It allows to transparently forward messages through multiplexer to
 * destination module
 */
public class MultiplexerOperator extends ModuleOperator {

    private final Pattern virtualChannelPattern = Pattern.compile("VC\\s+(\\w+)\\s+(.*)");

    private MultiplexerController controller;

    public void attachToModuleController(ModuleController module) {
        if(module instanceof MultiplexerController) {
            super.attachToModuleController(module);
            controller = (MultiplexerController)module;
        } else {
            throw new IllegalArgumentException("only MultiplexerController could be attached to MultiplexerOperator");
        }
    }

    public void onMessageReceived(Message message) {
        // Received a message, we should read it header and call an
        // appropriate function of the module
        Matcher result = virtualChannelPattern.matcher(message.data);
        if (result.matches()) {
            Integer virtualChannelId = Integer.valueOf(result.group(1));
            controller.forwardingMessage(virtualChannelId, new Message(result.group(2)));
        } else {
            super.onMessageReceived(message);
        }
    }
}
