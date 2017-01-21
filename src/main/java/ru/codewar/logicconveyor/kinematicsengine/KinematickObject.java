package ru.codewar.logicconveyor.kinematicsengine;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;

public interface KinematickObject {
    Integer getObjectId();
    Vector getVelocity();
    Vector getAcceleration();
    Point getPosition();
    double getSignature();
}
