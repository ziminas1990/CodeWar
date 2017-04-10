package ru.codewar.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.engine.EngineController;
import ru.codewar.module.engine.EngineModule;
import ru.codewar.module.ship.ShipController;
import ru.codewar.module.ship.ShipModule;
import ru.codewar.protocol.module.ModuleOperator;

/* ModuleTerminalFactory is used to create infrastructure, that allow to control module remotely.
   Most modules has the same operator implementation, that parse "REQ", "CMD" and make "IND" messages. But some
   modules could have its own specific operator implementation (like MultiplexerOperator)
   Each module has its own specific controller implementation, that implement a module specific protocol.
   This factory create a proper pair of controller + operator, to provide remote communication with module
 */
public class ModuleTerminalFactory {

    static Logger logger = LoggerFactory.getLogger(ModuleTerminalFactory.class);

    public ModuleTerminal make(BaseModuleInterface module) {
        logger.info("Creating terminal for \"{}\" module \"{}\" requested",
                module.getModuleType(), module.getModuleAddress());

        BaseModuleController controller = makeController(module);
        if(controller == null) {
            logger.warn("Can't create module controller!");
            return null;
        }

        ModuleOperator operator = new ModuleOperator();
        operator.attachToModuleController(controller);
        return new ModuleTerminal(module, controller, operator);
    }

    private BaseModuleController makeController(BaseModuleInterface module) {
        if(module instanceof EngineModule) {
            EngineController controller = new EngineController();
            controller.attachToEngine((EngineModule)module);
            return controller;
        } else if(module instanceof ShipModule) {
            ShipController controller = new ShipController();
            controller.attachToModule((ShipModule)module);
            return controller;
        }

        return null;
    }

}
