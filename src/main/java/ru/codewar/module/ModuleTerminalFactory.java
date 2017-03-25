package ru.codewar.module;

import ru.codewar.module.engine.EngineController;
import ru.codewar.module.engine.BaseEngine;
import ru.codewar.protocol.module.ModuleOperator;

public class ModuleTerminalFactory {

    public ModuleTerminal make(BaseModuleInterface module) {
        if(module.getModuleType().equals(BaseEngine.moduleType)) {
            return new ModuleTerminal(module, new EngineController(), new ModuleOperator());
        }
        return null;
    }

}
