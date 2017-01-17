package ru.codewar.logicconveyor.kinematicsengine;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;

public interface KinematickObject {
    Integer getObjectId();
    Vector getVelocity();
    Point getPosition();
    double getSignature();
}
