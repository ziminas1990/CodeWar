package ru.codewar.module.engine;


import ru.codewar.geometry.Vector;
import ru.codewar.module.BaseModule;
import ru.codewar.module.ModulesPlatform;

// Engine module is used to produce force to physical object, engine is installed on.
// Engine orientation defines the direction of jet, so the produced thrust has opposite direction
public class BaseEngine extends BaseModule implements EngineModule {

    public static final String moduleType = "engine";

    private double maxThrust;
    private double currentThrust;
    private ModulesPlatform platform;

    public BaseEngine(ModulesPlatform platform, String address, double maxThrust) {
        this(platform, address, "base engine", "", maxThrust);
    }

    protected BaseEngine(ModulesPlatform platform, String address, String moduleModel,
                         String moduleInfo, double maxThrust) {
        super(platform.getPlatformAddress() + "." + address, moduleType, moduleModel, moduleInfo);
        this.platform = platform;
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

    @Override // from EngineModule -> PlatformedModuleInterface
    public void installOnPlatform(ModulesPlatform platform) {
        this.platform = platform;
    }
    @Override // from EngineModule -> PlatformedModuleInterface
    public ModulesPlatform installedOn() {
        return this.platform;
    }

    // Platform engine is not rotatable, but it's orientation is depend on platform orientation
    @Override // from EngineModule -> PlatformedModuleInterface -> RotatableModuleType
    public double getMaxRotationSpeed() { return 0; }
    @Override // from EngineModule -> PlatformedModuleInterface -> RotatableModuleType
    public void rotate(double delta, double speed) { }
    @Override // from EngineModule -> PlatformedModuleInterface -> RotatableModuleType
    public Vector getOrientation() { return new Vector(); }

}
