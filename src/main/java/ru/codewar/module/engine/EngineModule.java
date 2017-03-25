package ru.codewar.module.engine;


import ru.codewar.module.BaseModuleInterface;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;

public interface EngineModule extends BaseModuleInterface, RotatableModuleType {
    public double getMaxThrust();
    public double getCurrentThrust();
    public void setThrust(double thrust);
}
