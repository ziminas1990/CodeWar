package ru.codewar.module.engine;

import ru.codewar.geometry.Vector;
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

    private RotatableModuleType platform;
    private Orientation orientation;

    public PlatformEngine(String address, Orientation orientation, double maxThrust) {
        super(address, maxThrust);
        this.orientation = orientation;
    }

    public void installOnPlatform(RotatableModuleType platform) {
        this.platform = platform;
    }

    public Orientation getEngineOrientation() { return orientation; }


    @Override // from BaseEngine -> BaseModuleInterface
    public String getModuleModel() { return "platform engine"; }
    @Override // from BaseEngine -> BaseModuleInterface
    public String getModuleInfo() { return "orientation = " + orientation; }

    @Override // from BaseEngine -> RotatableModuleType
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
