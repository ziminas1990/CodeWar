package ru.codewar.module.types.positionedModule;

import ru.codewar.geometry.Point;
import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleController;

import java.util.regex.Pattern;

/**
 * Possible requests:
 * 1. "position" - request module position
 * 2. "orientation" - request module orientation (nut supported)
 */
public class PositionedModuleController implements ModuleController {
    private final static Pattern positionReqPattern = Pattern.compile("position\\s*");

    PositionedModuleType module;

    public void attachToModule(PositionedModuleType module)
    {
        this.module = module;
    }

    public static boolean checkIfSupported(String message)
    {
        return positionReqPattern.matcher(message).matches();
    }

    @Override // from ModuleController
    public void onCommand(String command)
    {
        // Positioned module doesn't proceedStage any commands
    }

    @Override // from ModuleController
    public Message onRequest(Integer transactionId, String request)
    {
        if(module == null) {
            return new Message("fail: controller wasn't attached to module!");
        }
        if(positionReqPattern.matcher(request).matches()) {
            Point position = module.getPosition();
            return new Message(position.getX() + " " + position.getY());
        }
        return new Message("fail: incorrect request");
    }
}
