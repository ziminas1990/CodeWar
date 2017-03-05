package ru.codewar.module.ship;

import ru.codewar.module.BaseModuleController;
import ru.codewar.module.types.positionedModule.PositionedModuleController;
import ru.codewar.module.types.rotatableModule.RotatableModuleController;
import ru.codewar.networking.Message;

public class ShipController extends BaseModuleController {

    private PositionedModuleController positionedModuleController = new PositionedModuleController();
    private RotatableModuleController rotatableModuleController = new RotatableModuleController();

    public static boolean checkIfSupported(String message)
    {
        return BaseModuleController.checkIfSupported(message) ||
                PositionedModuleController.checkIfSupported(message) ||
                RotatableModuleController.checkIfSupported(message);
    }

    public void attachToModule(ShipModule ship) {
        super.attachToModule(ship);
        positionedModuleController.attachToModule(ship);
        rotatableModuleController.attachToModule(ship);
    }

    @Override // ModuleController
    public void onCommand(String command) {
        if(BaseModuleController.checkIfSupported(command)) {
            super.onCommand(command);
        } else if(PositionedModuleController.checkIfSupported(command)) {
            positionedModuleController.onCommand(command);
        } else if(RotatableModuleController.checkIfSupported(command)) {
            rotatableModuleController.onCommand(command);
        }
    }

    @Override // ModuleController
    public Message onRequest(Integer transactionId, String request) {
        if(BaseModuleController.checkIfSupported(request)) {
            return super.onRequest(transactionId, request);
        } else if(PositionedModuleController.checkIfSupported(request)) {
            return positionedModuleController.onRequest(transactionId, request);
        } else if(RotatableModuleController.checkIfSupported(request)) {
            return rotatableModuleController.onRequest(transactionId, request);
        }
        return null;
    }
}
