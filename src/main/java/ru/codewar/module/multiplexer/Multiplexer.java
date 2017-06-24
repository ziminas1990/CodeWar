package ru.codewar.module.multiplexer;

import ru.codewar.module.IBaseModule;
import ru.codewar.module.IModulesFactory;
import ru.codewar.module.ModuleTerminalFactory;
import ru.codewar.module.IModulesPlatform;
import ru.codewar.networking.Channel;

public class Multiplexer {
    private MultiplexerLogic logic;
    private MultiplexerController controller = new MultiplexerController();
    private MultiplexerOperator operator;

    public Multiplexer(IModulesFactory factory) {
        logic = new MultiplexerLogic(factory);
        operator = new MultiplexerOperator();
        operator.attachToModuleController(controller);
        controller.attachToMultiplexer(logic);
        controller.attachToOperator(operator);
    }

    public void attachToChannel(Channel channel) {
        operator.attachToChannel(channel);
        logic.attachToChannel(channel);
    }

    public void addModule(IBaseModule module) {
        logic.addModule(module);
    }

    public void addModulesInstalledOn(IModulesPlatform platform) {
        int totalModules = platform.getModulesCount();
        for(int index = 0; index < totalModules; index++) {
            addModule(platform.getModule(index));
        }
    }

    public MultiplexerOperator getOperator() { return operator; }
}
