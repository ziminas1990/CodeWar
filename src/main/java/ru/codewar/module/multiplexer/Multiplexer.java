package ru.codewar.module.multiplexer;


import ru.codewar.networking.Channel;
import ru.codewar.protocol.module.ModuleOperator;

public class Multiplexer {
    private MultiplexerLogic logic = new MultiplexerLogic();
    private MultiplexerController controller = new MultiplexerController();
    private MultiplexerOperator operator;

    public Multiplexer(String address) {
        operator = new MultiplexerOperator(address);
        operator.attachToModuleController(controller);
        controller.attachToMultiplexer(logic);
        controller.attachToOperator(operator);
    }

    public void attachToChannel(Channel channel) {
        operator.attachToChannel(channel);
        logic.attachToChannel(channel);
    }

    public void addModule(ModuleOperator operator) {
        logic.addModule(operator);
    }

    public MultiplexerOperator getOperator() { return operator; }
}
