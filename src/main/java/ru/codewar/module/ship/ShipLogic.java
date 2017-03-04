package ru.codewar.module.ship;


import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.physicallogic.PhysicalObjectImpl;

public class ShipLogic extends PhysicalObjectImpl implements ShipModule {

    private Vector orientation = new Vector(0, 1);

    public ShipLogic(int objectId, double mass, double signature, Point position,
                     Vector orientation, Vector velocity) {
        super(objectId, mass, signature, position, velocity);
        this.orientation = orientation;
        this.orientation.normalize();
    }

    // getPosition from ShipModule -> PositionedModuleType has been
    // implemented by PhysicalObjectImpl
    // @Override
    // public Point getPosition() {}

    @Override // from ShipModule -> RotatableModuleType
    public double getMaxRotationSpeed() { return 0; }
    @Override // from ShipModule -> RotatableModuleType
    public void rotate(double delta, double speed) {}
    @Override // from ShipModule -> RotatableModuleType
    public Vector getOrientation() { return orientation; }
}

