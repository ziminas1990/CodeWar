package Service.Module.Abstract;

import Service.Geometry.Point;
import Service.Geometry.Vector;

/**
 * Descibe the module, that is placed somewhere in space and has an orientation
 */
public interface IPositionedModule {
    Point getPosition();
    Vector getOrientation();
}
