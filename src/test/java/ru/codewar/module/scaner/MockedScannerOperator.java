package ru.codewar.module.scaner;

import ru.codewar.world.CelestialBody;

import java.util.ArrayList;

public class MockedScannerOperator implements IScannerOperator {

    ArrayList<CelestialBody> scannedBodies;

    public void onScanningComplete(ArrayList<CelestialBody> bodies) {
        scannedBodies = bodies;
    }

    public boolean hasBodyWithName(String name) {
        if(scannedBodies == null)
            return false;
        for(CelestialBody body : scannedBodies)
            if(body.getName().equals(name))
                return true;
        return false;
    }
}
