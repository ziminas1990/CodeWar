package ru.codewar.logicconveyor.newtonengine;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;

public interface NewtonObject {

    Integer getId();

    double getMass();

    double getSignature();

    Point getPosition();

    Vector getVelocity();

}
