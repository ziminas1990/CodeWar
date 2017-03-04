package ru.codewar.module.types.rotatableModule;

import ru.codewar.geometry.Vector;

public interface RotatableModuleType {
    double getMaxRotationSpeed();

    void rotate(double delta, double speed);

    Vector getOrientation();
}
