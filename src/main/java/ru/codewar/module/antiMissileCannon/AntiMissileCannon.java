package ru.codewar.module.antiMissileCannon;

import ru.codewar.module.positionedModule.PositionedModule;
import ru.codewar.module.rotatableModule.RotatableModule;

public interface AntiMissileCannon extends PositionedModule, RotatableModule {
    void fire(int detonateTimeout);
    int ticksToReload();
}
