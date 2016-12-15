package ru.codewar.module.engine;


import ru.codewar.module.types.rotatableModule.RotatableModuleType;

public interface Engine extends RotatableModuleType {

    double getMaxThrust();

    double getCurrentThrust();

    void setThrust(double thrust);

}
