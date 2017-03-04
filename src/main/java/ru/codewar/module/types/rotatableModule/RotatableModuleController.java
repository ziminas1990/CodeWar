package ru.codewar.module.types.rotatableModule;

import ru.codewar.geometry.Vector;
import ru.codewar.networking.Message;
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
        Matcher matcher = rotateCommandPattern.matcher(command);
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

    public Message onRequest(Integer transactionId, String request) {
        if(module == null) {
            return new Message("fail: controller wasn't attached to module!");
        }
        if(getMaxSpeedReqPattern.matcher(request).matches()) {
            return new Message(String.valueOf(module.getMaxRotationSpeed()));
        }
        if(orientationReqPattern.matcher(request).matches()) {
            Vector orientation = module.getOrientation();
            return new Message(
                    orientation.getNormilizedX() + " " +
                            orientation.getNormilizedY());
        }
        return new Message("fail: incorrect request");
    }
}
