package ru.codewar.module.rotatableModule;

import ru.codewar.protocol.module.ModuleController;
import ru.codewar.util.ArgumentsReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RotatableModuleControllerImpl implements ModuleController {
    private final static Pattern checkPattern = Pattern.compile("(getMaxSpeed\\s*|rotate\\s+.*)");
    private final static Pattern getMaxSpeedReqPattern = Pattern.compile("getMaxSpeed\\s*");
    private final static Pattern rotateCommandPattern = Pattern.compile("rotate\\s+(?<ARGS>.+)");

    RotatableModule module;

    public void attachToModule(RotatableModule module)
    {
        this.module = module;
    }

    static boolean checkIfSupported(String message)
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
            if(delta != null && speed != null)
                module.rotate(delta, speed);
        }
    }

    public String onRequest(Integer transactionId, String request) {
        if(module == null) {
            return "fail: controller wasn't attached to module!";
        }
        if(getMaxSpeedReqPattern.matcher(request).matches()) {
            return String.valueOf(module.getMaxRotationSpeed());
        }
        return "fail: incorrect request";
    }
}
