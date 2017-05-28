package ru.codewar.module.scaner;

import ru.codewar.world.CelestialBody;

import java.util.ArrayList;

public class MockedScannerOperator extends ScannerOperator {

    ArrayList<CelestialBody> scannedBodies;

    @Override
    public void onScanningComplete(int transactionId, ArrayList<CelestialBody> bodies) {
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
