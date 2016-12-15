package ru.codewar.module.antiMissileCannon;

import ru.codewar.module.types.positionedModule.PositionedModuleType;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;

public interface AntiMissileCannon extends PositionedModuleType, RotatableModuleType {
    void fire(int detonateTimeout);
    int ticksToReload();
}
