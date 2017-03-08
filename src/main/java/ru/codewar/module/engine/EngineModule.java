package ru.codewar.module.engine;


import ru.codewar.module.BaseModuleInterface;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;

// Engine module is used to produce force to physical object, engine is installed on.
// Engine orientation defines the direction of jet, so the produced thrust has opposite direction
public interface EngineModule extends BaseModuleInterface, RotatableModuleType {

    double getMaxThrust();

    double getCurrentThrust();

    void setThrust(double thrust);

}
