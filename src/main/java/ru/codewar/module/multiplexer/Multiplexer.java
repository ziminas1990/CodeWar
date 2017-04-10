package ru.codewar.module.multiplexer;


import ru.codewar.module.BaseModuleInterface;
import ru.codewar.module.ModuleTerminalFactory;
import ru.codewar.module.ModulesPlatform;
import ru.codewar.networking.Channel;
import ru.codewar.protocol.module.ModuleOperator;

import javax.smartcardio.TerminalFactory;

public class Multiplexer {
    private MultiplexerLogic logic = new MultiplexerLogic(new ModuleTerminalFactory());
    private MultiplexerController controller = new MultiplexerController();
    private MultiplexerOperator operator;

    public Multiplexer() {
        operator = new MultiplexerOperator();
        operator.attachToModuleController(controller);
        controller.attachToMultiplexer(logic);
        controller.attachToOperator(operator);
    }

    public void attachToChannel(Channel channel) {
        operator.attachToChannel(channel);
        logic.attachToChannel(channel);
    }

    public void addModule(BaseModuleInterface module) {
        logic.addModule(module);
    }

    public void addModulesInstalledOn(ModulesPlatform platform) {
        int totalModules = platform.getModulesCount();
        for(int index = 0; index < totalModules; index++) {
            addModule(platform.getModule(index));
        }
    }

    public MultiplexerOperator getOperator() { return operator; }
}
