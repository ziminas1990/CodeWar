package ru.codewar.module.guidedMissile;

import ru.codewar.module.detonatableModule.DetonatableModule;
import ru.codewar.module.engine.Engine;
import ru.codewar.module.positionedModule.PositionedModule;
import ru.codewar.module.rotatableModule.RotatableModule;


public interface GuidedMissile extends DetonatableModule, PositionedModule, Engine, RotatableModule {

    void lunch();

    boolean isLaunched();

}
