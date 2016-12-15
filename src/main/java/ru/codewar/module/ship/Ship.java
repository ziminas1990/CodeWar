package ru.codewar.module.ship;

import ru.codewar.module.types.positionedModule.PositionedModuleType;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;
import ru.codewar.protocol.module.ModuleController;

import java.util.ArrayList;


public interface Ship extends PositionedModuleType, RotatableModuleType {
    ArrayList<ModuleController> getModules();
}
