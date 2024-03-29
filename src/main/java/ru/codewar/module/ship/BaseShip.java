package ru.codewar.module.ship;

import org.json.JSONObject;
import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.physicallogic.PhysicalObjectImpl;
import ru.codewar.module.IBaseModule;
import ru.codewar.module.engine.BaseEngine;
import ru.codewar.module.engine.EngineModule;

import java.util.ArrayList;

public class BaseShip extends PhysicalObjectImpl implements ShipModule {

    public static final String moduleType = "ship";
    public static final String moduleModel = "base ship";

    private String address = "";
    private Vector orientation = new Vector(0, 1);
    private ArrayList<IBaseModule> modules = new ArrayList<>();
    private ArrayList<BaseEngine> engines = new ArrayList<>();

    public BaseShip(String address, JSONObject data) {
        super(data.getJSONObject("physical object"));
        this.address = address;
        this.orientation = new Vector(data.getJSONArray("orientation"));
    }

    public BaseShip(String address, double mass, double signature, Point position,
                    Vector orientation, Vector velocity) {
        super(mass, signature, position, velocity);
        this.address = address;
        this.orientation = orientation;
        this.orientation.normalize();
    }

    public void addModule(IBaseModule module) {
        if(module instanceof EngineModule) {
            engines.add((BaseEngine)module);
        }
        modules.add(module);
    }

    // Usually called from ShipsLogicConveyor
    public void proceed() {
        for(BaseEngine engine : engines) {
            pushForce(engine.getThrustVector());
        }
    }

    // getPosition from ShipModule -> PositionedModuleType has been
    // implemented by PhysicalObjectImpl
    // @Override
    // public Point getPosition() {}

    @Override // from ShipModule -> IBaseModule
    public String getModuleAddress() { return address; }
    @Override // from ShipModule -> IBaseModule
    public String getModuleType() { return "ship"; }
    @Override // from ShipModule -> IBaseModule
    public String getModuleModel() { return "base ship"; }
    @Override // from ShipModule -> IBaseModule
    public String getModuleInfo() { return ""; }

    @Override // from ShipModule -> RotatableModuleType
    public double getMaxRotationSpeed() { return 0; }
    @Override // from ShipModule -> RotatableModuleType
    public void rotate(double delta, double speed) {}
    @Override // from ShipModule -> RotatableModuleType
    public Vector getOrientation() { return orientation; }

    @Override // from ShipModule -> IModulesPlatform
    public int getModulesCount() { return modules.size(); }
    @Override // from ShipModule -> IModulesPlatform
    public IBaseModule getModule(int index) { return modules.get(index); }
    @Override // from ShipModule -> IModulesPlatform
    public String getPlatformAddress() { return getModuleAddress(); }
    @Override // from ShipModule -> IModulesPlatform
    public  ArrayList<IBaseModule> getAllModules() { return modules; }
}

