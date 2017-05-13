package ru.codewar.module.engine;

import ru.codewar.module.IPlatformedModule;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;

/*
    EngineModule inherits:
    1. IPlatformedModule - because engine is a module, that should only be installed on some platform
    2. RotatableModuleType - because engine could has his own orientation, that could not depend on
       platforms orientation
 */
public interface EngineModule extends IPlatformedModule, RotatableModuleType {
    double getMaxThrust();
    double getCurrentThrust();
    void setThrust(double thrust);
}
