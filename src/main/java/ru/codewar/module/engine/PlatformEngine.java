package ru.codewar.module.engine;

import ru.codewar.geometry.Vector;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;

// PlatformEngine is engine, that installed on some rotatable module and oriented to one of
// for possible directions: "Forward", "Backward", "Left" and "Right"
// This engine is not rotatable, so MaxRotationSpeed is 0
// Orientation of engine is depend of orientation of platform, that engine is installed on
public class PlatformEngine implements EngineModule {

    public enum Orientation {
        eOrientationForward { public String toString() { return "forward"; }},
        eOrientationBackward { public String toString() { return "backward"; }},
        eOrientationLeft { public String toString() { return "left"; }},
        eOrientationRight { public String toString() { return "right"; }}
    }

    private RotatableModuleType platform;
    private Orientation orientation;
    private double maxThrust;
    private double currentThrust;

    public PlatformEngine(Orientation orientation, double maxThrust) {
        this.orientation = orientation;
        this.maxThrust = maxThrust;
    }

    public void installOnPlatform(RotatableModuleType platform) {
        this.platform = platform;
    }

    public Orientation getEngineOrientation() { return orientation; }

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

    @Override // from EngineModule -> BaseModuleInterface
    public String getType() { return "engine"; }
    @Override // from EngineModule -> BaseModuleInterface
    public String getModel() { return "simple engine"; }
    @Override // from EngineModule -> BaseModuleInterface
    public String getParameters() { return "orientation = " + orientation; }

    // Platform engine is not rotatable, but it's orientation is depend on platform orientation
    @Override // from EngineModule -> RotatableModuleType
    public double getMaxRotationSpeed() { return 0; }
    @Override // from EngineModule -> RotatableModuleType
    public void rotate(double delta, double speed) { }
    @Override // from EngineModule -> RotatableModuleType
    public Vector getOrientation() {
        if(platform == null)
            return new Vector();
        switch (orientation) {
            case eOrientationForward:
                return platform.getOrientation();
            case eOrientationBackward:
                return platform.getOrientation().getBackward();
            case eOrientationRight:
                return platform.getOrientation().getRightDirection();
            case eOrientationLeft:
                return platform.getOrientation().getLeftDirection();
            default:
                return new Vector();
        }
    }
}
