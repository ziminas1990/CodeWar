package ru.codewar.module.scaner;

import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleOperator;
import ru.codewar.world.CelestialBody;

import java.util.ArrayList;

public class ScannerOperator extends ModuleOperator {

    public void onScanningComplete(int transactionId, ArrayList<CelestialBody> bodies) {
        // information about bodies will be represented as JSON array
        StringBuilder response = new StringBuilder();
        response.append("scanned {");
        for(CelestialBody body : bodies)
            response.append(body.toJson());
        response.append("}");
        onResponse(transactionId, new Message(response.toString()));
    }
}
