package ru.codewar.module.positionedModule;

import ru.codewar.protocol.module.ModuleController;

import java.util.regex.Pattern;

/**
 * Possible requests:
 * 1. "position" - request module position
 * 2. "orientation" - request module orientation
 */
public class PositionedModuleControllerImpl implements ModuleController {
    private final static Pattern checkPattern = Pattern.compile("(position|orient)\\s*");
    private final static Pattern positionReqPattern = Pattern.compile("position\\s*");
    private final static Pattern orientationReqPattern = Pattern.compile("orient\\s*");

    PositionedModule module;

    public void attachToModule(PositionedModule module)
    {
        this.module = module;
    }

    static boolean checkIfSupported(String message)
    {
        return checkPattern.matcher(message).matches();
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
        if(orientationReqPattern.matcher(request).matches()) {
            return module.getOrientation().getNormilizedX() + " " +
                    module.getOrientation().getNormilizedY();
        }
        return "fail: incorrect request";
    }
}
