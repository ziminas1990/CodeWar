package ru.codewar.module.engine;


import ru.codewar.geometry.Vector;
import ru.codewar.module.IModulesPlatform;
import ru.codewar.module.PlatformedModule;

// Engine module is used to produce force to physical object, engine is installed on.
// Engine orientation defines the direction of jet, so the produced thrust has opposite direction
public class BaseEngine extends PlatformedModule implements EngineModule {

    public static final String moduleType = "engine";
    public static final String moduleModel = "base engine";

    private double maxThrust;
    private double currentThrust;

    public BaseEngine(IModulesPlatform platform, String address, double maxThrust) {
        this(platform, address, moduleModel, "", maxThrust);
    }

    protected BaseEngine(IModulesPlatform platform, String address, String moduleModel,
                         String moduleInfo, double maxThrust) {
        super(platform, address, moduleType, moduleModel, moduleInfo);
        installOnPlatform(platform);
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

    // Platform engine is not rotatable, but it's orientation is depend on platform orientation
    @Override // from EngineModule -> RotatableModuleType
    public double getMaxRotationSpeed() { return 0; }
    @Override // from EngineModule -> RotatableModuleType
    public void rotate(double delta, double speed) { }
    @Override // from EngineModule -> RotatableModuleType
    public Vector getOrientation() { return new Vector(); }

}
