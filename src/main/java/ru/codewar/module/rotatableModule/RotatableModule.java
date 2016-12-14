package ru.codewar.module.rotatableModule;

public interface RotatableModule {
    double getMaxRotationSpeed();

    double rotate(double delta, double speed);
}
