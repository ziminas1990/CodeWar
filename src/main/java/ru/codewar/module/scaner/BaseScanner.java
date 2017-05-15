package ru.codewar.module.scaner;

import ru.codewar.module.IModulesPlatform;
import ru.codewar.module.PlatformedModule;
import ru.codewar.world.CelestialBody;
import ru.codewar.world.ISolarSystem;

import java.util.ArrayList;

public class BaseScanner extends PlatformedModule implements IScannerModule {

    private static final double speedOfLight = 3 * 10e8;

    public static final String moduleType = "scanner";

    private IScannerOperator operator;
    private ISolarSystem system;
    private double sqrResolution;

    // Current scanning parameters
    private double sqrDistance;
    private double minSignature, maxSignature;
    private double timeToFinishScanning;

    public BaseScanner(IModulesPlatform platform, String address, ISolarSystem system) {
        this(platform, address, "base scanner", system);
    }

    protected BaseScanner(IModulesPlatform platform, String address, String model, ISolarSystem system) {
        super(platform, address, moduleType, model, "");
        this.system = system;
    }

    public void setSqrResolution(double sqrResolution) {
        this.sqrResolution = sqrResolution * sqrResolution;
    }

    // from IScannerModule
    public double getSqrResolution() { return Math.sqrt(sqrResolution); }
    @Override // from IScannerModule
    public void attachToOperator(IScannerOperator operator) {
        this.operator = operator;
    }
    @Override // from IScannerModule
    public boolean scanning(double distance, double minSignature, double maxSignature) {
        if(operator == null || system == null)
            return false;
        if(distance < 1) {
            // scanning for asteroid/moons is NOT required, so just immediately
            // return sol and planets
            ArrayList<CelestialBody> result = new ArrayList<>(system.getPlanets().size() + 1);
            result.addAll(system.getStars());
            result.addAll(system.getPlanets());
            operator.onScanningComplete(result);
            return true;
        }
        this.minSignature = minSignature;
        this.maxSignature = maxSignature;
        sqrDistance = distance * distance;
        timeToFinishScanning = 2 * distance / speedOfLight;
        return true;
    }

    @Override // from IScannerModule -> IPlatformedModule -> IBaseModule
    public void alive(double dt) {
        super.alive(dt);

        if(timeToFinishScanning >= dt) {
            timeToFinishScanning -= dt;
        } else {
            timeToFinishScanning = 0;
            if(operator == null)
                return;
            ArrayList<CelestialBody> result = new ArrayList<>();
            system.getMoons().stream()
                    .filter(this::isCovered)
                    .peek(result::add);
            system.getAsteroids().stream()
                    .filter(this::isCovered)
                    .peek(result::add);
            operator.onScanningComplete(result);
        }
    }

    private boolean isCovered(CelestialBody body) {
        if(body.getSignature() > maxSignature || body.getSignature() < minSignature)
            return false;
        double sqrR = installedOn().getPosition().getSquareOfDistanceTo(body.getPosition());
        if(sqrR > sqrDistance)
            return false;
        return sqrResolution > body.getSqrOfSignature() / sqrR;
    }

}
