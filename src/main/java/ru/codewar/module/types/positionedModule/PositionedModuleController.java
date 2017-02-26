package ru.codewar.module.types.positionedModule;

import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleController;

import java.util.regex.Pattern;

/**
 * Possible requests:
 * 1. "position" - request module position
 * 2. "orientation" - request module orientation
 */
public class PositionedModuleController implements ModuleController {
    private final static Pattern positionReqPattern = Pattern.compile("position\\s*");

    PositionedModuleType module;

    public void attachToModule(PositionedModuleType module)
    {
        this.module = module;
    }

    static boolean checkIfSupported(String message)
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
            return new Message(module.getPosition().getX() + " " + module.getPosition().getY());
        }
        return new Message("fail: incorrect request");
    }
}
