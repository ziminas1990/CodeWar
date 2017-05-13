package ru.codewar.module.engine;

import ru.codewar.geometry.Vector;
import ru.codewar.module.ModulesPlatform;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;

// PlatformEngine is engine, that installed on some rotatable module and oriented to one of
// for possible directions: "Forward", "Backward", "Left" and "Right"
// This engine is not rotatable, so MaxRotationSpeed is 0
// Orientation of engine is depend of orientation of platform, that engine is installed on
public class PlatformEngine extends BaseEngine {

    public enum Orientation {
        eOrientationForward { public String toString() { return "forward"; }},
        eOrientationBackward { public String toString() { return "backward"; }},
        eOrientationLeft { public String toString() { return "left"; }},
        eOrientationRight { public String toString() { return "right"; }}
    }

    private Orientation orientation;

    public PlatformEngine(ModulesPlatform platform, String address, Orientation orientation, double maxThrust) {
        super(platform, address, maxThrust);
        this.orientation = orientation;
    }

    public Orientation getEngineOrientation() { return orientation; }

    @Override // from BaseEngine -> IBaseModule
    public String getModuleModel() { return "platform engine"; }
    @Override // from BaseEngine -> IBaseModule
    public String getModuleInfo() { return "orientation = " + orientation; }

    @Override // from BaseEngine -> RotatableModuleType
    public Vector getOrientation() {
        if(installedOn() == null)
            return new Vector();
        switch (orientation) {
            case eOrientationForward:
                return installedOn().getOrientation();
            case eOrientationBackward:
                return installedOn().getOrientation().getBackward();
            case eOrientationRight:
                return installedOn().getOrientation().getRightDirection();
            case eOrientationLeft:
                return installedOn().getOrientation().getLeftDirection();
            default:
                return new Vector();
        }
    }
}
