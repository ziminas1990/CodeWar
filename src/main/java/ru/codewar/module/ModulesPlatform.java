package ru.codewar.module;

import ru.codewar.logicconveyor.physicallogic.PhysicalObject;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;

import java.util.ArrayList;

/**
 * Interface for class, that could carrying a number of modules (modules could be install on platform)
 * Modules platform inherits:
 * 1. PhysicalObject - because it has a position in space
 * 2. RotatableModuleType - because it has an orientation and, possibly, could be rotated
 */
public interface ModulesPlatform extends PhysicalObject, RotatableModuleType {

    int getModulesCount();
    IBaseModule getModule(int index);
    default String getPlatformAddress() { return ""; }

    default ArrayList<IBaseModule> getAllModules() {
        ArrayList<IBaseModule> modulesList = new ArrayList<>();
        int totalModules = getModulesCount();
        for(int i = 0; i < totalModules; i++) {
            modulesList.add(getModule(i));
        }
        return modulesList;
    }
}
