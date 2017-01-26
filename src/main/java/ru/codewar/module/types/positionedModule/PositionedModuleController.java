package ru.codewar.module.types.positionedModule;

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

    public void onCommand(String command)
    {
        // Positioned module doesn't proceed any commands
    }

    public String onRequest(Integer transactionId, String request)
    {
        if(module == null) {
            return "fail: controller wasn't attached to module!";
        }
        if(positionReqPattern.matcher(request).matches()) {
            return module.getPosition().getX() + " " + module.getPosition().getY();
        }
        return "fail: incorrect request";
    }
}
