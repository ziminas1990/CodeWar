package ru.codewar.logicconveyor.physicallogic;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;

public interface PhysicalObject {
    Integer getObjectId();
    Vector getVelocity();
    Vector getAcceleration();
    Point getPosition();
    double getSignature();
}
