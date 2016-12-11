package ru.codewar.module;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;

/**
 * Describe the module, that is placed somewhere in space and has an orientation
 */
public interface PositionedModule {
    Point getPosition();

    Vector getOrientation();
}
