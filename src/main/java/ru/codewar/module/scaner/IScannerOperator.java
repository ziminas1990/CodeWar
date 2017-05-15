package ru.codewar.module.scaner;

import ru.codewar.world.CelestialBody;

import java.util.ArrayList;

public interface IScannerOperator {
    void onScanningComplete(ArrayList<CelestialBody> bodies);
}
