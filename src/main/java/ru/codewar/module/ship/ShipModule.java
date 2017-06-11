package ru.codewar.module.ship;

import ru.codewar.module.IBaseModule;
import ru.codewar.module.IModulesPlatform;
import ru.codewar.module.types.positionedModule.PositionedModuleType;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;

/*
    ShipModule extends:
    1. IBaseModule - because ship is also a module
    2. IModulesPlatform - because ship has number of modules, installed on it
    3. PositionedModuleType, RotatableModuleType - because ship is physical object, that has a position
       and could be rotated
 */
public interface ShipModule extends IBaseModule, IModulesPlatform, PositionedModuleType, RotatableModuleType
{
    void addModule(IBaseModule module);
}
