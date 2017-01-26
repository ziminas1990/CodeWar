package ru.codewar.module.types.rotatableModule;

import ru.codewar.protocol.module.ModuleController;
import ru.codewar.util.ArgumentsReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RotatableModuleController implements ModuleController {
    private final static Pattern checkPattern = Pattern.compile("(getMaxRotateSpeed\\s*|rotate\\s+.*|orient\\s*)");
    private final static Pattern getMaxSpeedReqPattern = Pattern.compile("getMaxRotateSpeed\\s*");
    private final static Pattern rotateCommandPattern = Pattern.compile("rotate\\s+(?<ARGS>.+)");
    private final static Pattern orientationReqPattern = Pattern.compile("orient\\s*");

    RotatableModuleType module;

    public void attachToModule(RotatableModuleType module)
    {
        this.module = module;
    }

    public static boolean checkIfSupported(String message)
    {
        return checkPattern.matcher(message).matches();
    }

    public void onCommand(String command) {
        if(module == null) {
            return;
        }
        Matcher matcher;

        matcher = rotateCommandPattern.matcher(command);
        if(matcher.matches()) {
            ArgumentsReader reader = new ArgumentsReader(matcher.group("ARGS"));
            Double delta = reader.readDouble();
            Double speed = reader.readDouble();
            if(delta != null) {
                if(speed == null) {
                    speed = module.getMaxRotationSpeed();
                }
                module.rotate(delta, speed);
            }
        }
    }

    public String onRequest(Integer transactionId, String request) {
        if(module == null) {
            return "fail: controller wasn't attached to module!";
        }
        if(getMaxSpeedReqPattern.matcher(request).matches()) {
            return String.valueOf(module.getMaxRotationSpeed());
        }
        if(orientationReqPattern.matcher(request).matches()) {
            return module.getOrientation().getNormilizedX() + " " +
                    module.getOrientation().getNormilizedY();
        }
        return "fail: incorrect request";
    }
}
