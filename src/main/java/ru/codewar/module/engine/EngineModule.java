package ru.codewar.module.engine;

import ru.codewar.module.PlatformedModuleInterface;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;


/*
    EngineModule inherits:
    1. PlatformedModuleInterface - because engine is a module, that could only be installed on some platform
    2. RotatableModuleType - because engine could has his own orientation, that could not depend on
       platforms orientation
 */
public interface EngineModule extends PlatformedModuleInterface, RotatableModuleType {
    double getMaxThrust();
    double getCurrentThrust();
    void setThrust(double thrust);
}
