package ru.codewar.module;

import ru.codewar.module.engine.EngineController;
import ru.codewar.module.engine.EngineModule;
import ru.codewar.protocol.module.ModuleOperator;

public class ModuleTerminalFactory {

    public ModuleTerminal make(BaseModuleInterface module) {
        if(module instanceof EngineModule) {
            EngineModule engineModule = (EngineModule)module;

            EngineController controller = new EngineController();
            controller.attachToEngine(engineModule);

            ModuleOperator operator = new ModuleOperator();
            operator.attachToModuleController(controller);

            return new ModuleTerminal(engineModule, controller, operator);
        }
        return null;
    }

}
