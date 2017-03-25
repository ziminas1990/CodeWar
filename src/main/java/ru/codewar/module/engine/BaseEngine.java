package ru.codewar.module.engine;


import ru.codewar.geometry.Vector;
import ru.codewar.module.BaseModuleInterface;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;

// Engine module is used to produce force to physical object, engine is installed on.
// Engine orientation defines the direction of jet, so the produced thrust has opposite direction
public class BaseEngine implements EngineModule {

    public static final String moduleType = "engine";

    private String address;
    private double maxThrust;
    private double currentThrust;

    public BaseEngine(String address, double maxThrust) {
        this.address = address;
        this.maxThrust = maxThrust;
    }

    public Vector getThrustVector() {
        return new Vector(getOrientation()).setLength(getCurrentThrust());
    }

    @Override // from EngineModule
    public double getMaxThrust() {
        return maxThrust;
    }
    @Override // from EngineModule
    public double getCurrentThrust() {
        return currentThrust;
    }
    @Override // from EngineModule
    public void setThrust(double thrust) {
        if(this.maxThrust < thrust)
            thrust = this.maxThrust;
        this.currentThrust = thrust;
    }

    @Override // from BaseModuleInterface
    public String getModuleAddress() { return address; }
    @Override // from BaseModuleInterface
    public String getModuleType() { return moduleType; }
    @Override // from BaseModuleInterface
    public String getModuleModel() { return "base engine"; }
    @Override // from BaseModuleInterface
    public String getModuleInfo() { return ""; }

    // Platform engine is not rotatable, but it's orientation is depend on platform orientation
    @Override // from RotatableModuleType
    public double getMaxRotationSpeed() { return 0; }
    @Override // from RotatableModuleType
    public void rotate(double delta, double speed) { }
    @Override // from RotatableModuleType
    public Vector getOrientation() { return new Vector(); }

}
