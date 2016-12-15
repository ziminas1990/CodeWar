package ru.codewar.module.types.rotatableModule;

import ru.codewar.geometry.Vector;

public interface RotatableModuleType {
    double getMaxRotationSpeed();

    double rotate(double delta, double speed);

    Vector getOrientation();
}
