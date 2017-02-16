package ru.codewar.logicconveyor.physicallogic;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;

public interface PhysicalObject {
    Integer getObjectId();
    Point getPosition();
    double getSignature();
    double getMass();

    Vector getVelocity();

    void pushForce(Vector force);
    Vector getForce();
}
