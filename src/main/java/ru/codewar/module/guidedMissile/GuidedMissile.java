package ru.codewar.module.guidedMissile;

import ru.codewar.module.types.detonatableModule.DetonatableModuleType;
import ru.codewar.module.engine.Engine;
import ru.codewar.module.types.positionedModule.PositionedModuleType;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;


public interface GuidedMissile extends DetonatableModuleType, PositionedModuleType, Engine, RotatableModuleType {

    void lunch();

    boolean isLaunched();

}
