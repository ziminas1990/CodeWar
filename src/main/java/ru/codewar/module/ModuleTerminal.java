package ru.codewar.module;

import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;
import ru.codewar.networking.NetworkTerminal;
import ru.codewar.protocol.module.ModuleController;
import ru.codewar.protocol.module.ModuleOperator;

/**
 * This class is a container for all module's infrastructure, that is required
 * to provide communication with module via an appropriate protocol
 */
public class ModuleTerminal implements NetworkTerminal {

    IBaseModule module;
    ModuleController controller;
    ModuleOperator operator;

    public ModuleTerminal(IBaseModule module, ModuleController controller, ModuleOperator operator) {
        this.module = module;
        this.controller = controller;
        this.operator = operator;
    }

    public IBaseModule getModule() { return module; }
    public ModuleController getController() { return controller; }
    public ModuleOperator getOperator() { return operator; }

    @Override // from NetworkTerminal
    public void attachToChannel(Channel channel) {
        operator.attachToChannel(channel);
    }
    @Override // from NetworkTerminal
    public void onMessageReceived(Message message) { operator.onMessageReceived(message); }

}
