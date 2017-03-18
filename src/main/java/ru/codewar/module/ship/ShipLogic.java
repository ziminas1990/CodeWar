package ru.codewar.module.ship;


import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.physicallogic.PhysicalObjectImpl;
import ru.codewar.module.engine.EngineModule;

import java.util.ArrayList;

public class ShipLogic extends PhysicalObjectImpl implements ShipModule {

    private Vector orientation = new Vector(0, 1);
    private ArrayList<EngineModule> engines = new ArrayList<>();

    public ShipLogic(int objectId, double mass, double signature, Point position,
                     Vector orientation, Vector velocity) {
        super(objectId, mass, signature, position, velocity);
        this.orientation = orientation;
        this.orientation.normalize();
    }

    public void addEngine(EngineModule engine) {
        engines.add(engine);
    }

    public void proceed() {
        for(EngineModule engine : engines) {
            pushForce(EngineModule.getThrustVector(engine));
        }
    }

    // getPosition from ShipModule -> PositionedModuleType has been
    // implemented by PhysicalObjectImpl
    // @Override
    // public Point getPosition() {}

    @Override // from ShipModule -> BaseModuleInterface
    public String getType() { return "ship"; }
    @Override // from ShipModule -> BaseModuleInterface
    public String getModel() { return "noobship"; }
    @Override // from ShipModule -> BaseModuleInterface
    public String getParameters() { return ""; }

    @Override // from ShipModule -> RotatableModuleType
    public double getMaxRotationSpeed() { return 0; }
    @Override // from ShipModule -> RotatableModuleType
    public void rotate(double delta, double speed) {}
    @Override // from ShipModule -> RotatableModuleType
    public Vector getOrientation() { return orientation; }
}

