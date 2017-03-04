package ru.codewar.module.ship;

import ru.codewar.module.types.positionedModule.PositionedModuleController;
import ru.codewar.module.types.rotatableModule.RotatableModuleController;
import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleController;

public class ShipController implements ModuleController {

    private PositionedModuleController positionedModuleController = new PositionedModuleController();
    private RotatableModuleController rotatableModuleController = new RotatableModuleController();

    public static boolean checkIfSupported(String message)
    {
        return PositionedModuleController.checkIfSupported(message) ||
                RotatableModuleController.checkIfSupported(message);
    }

    public void attachToModule(ShipModule ship) {
        positionedModuleController.attachToModule(ship);
        rotatableModuleController.attachToModule(ship);
    }

    @Override // ModuleController
    public void onCommand(String command) {
        if(PositionedModuleController.checkIfSupported(command)) {
            positionedModuleController.onCommand(command);
        }
        if(RotatableModuleController.checkIfSupported(command)) {
            rotatableModuleController.onCommand(command);
        }
    }

    @Override // ModuleController
    public Message onRequest(Integer transactionId, String request) {
        if(PositionedModuleController.checkIfSupported(request)) {
            return positionedModuleController.onRequest(transactionId, request);
        }
        if(RotatableModuleController.checkIfSupported(request)) {
            return rotatableModuleController.onRequest(transactionId, request);
        }
        return null;
    }

}
