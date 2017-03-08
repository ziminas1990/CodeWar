package ru.codewar.module.engine;

import ru.codewar.module.BaseModuleController;
import ru.codewar.module.types.rotatableModule.RotatableModuleController;

import ru.codewar.networking.Message;
import ru.codewar.util.ArgumentsReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EngineController extends BaseModuleController {

    private final static Pattern checkPattern =
            Pattern.compile("(getMaxThrust\\s*|getCurrentThrust\\s*|setThrust\\s+.*)");
    private final static Pattern getMaxThrustReqPattern = Pattern.compile("getMaxThrust\\s*");
    private final static Pattern getCurrentThrustReqPattern = Pattern.compile("getCurrentThrust\\s*");
    private final static Pattern setThrustCommandPattern = Pattern.compile("setThrust\\s+(?<ARGS>.+)");

    private EngineModule module;
    private RotatableModuleController rotatableModuleController = new RotatableModuleController();

    public static boolean checkIfSupported(String message) {
        return BaseModuleController.checkIfSupported(message) ||
                RotatableModuleController.checkIfSupported(message) ||
                checkPattern.matcher(message).matches();
    }

    public void attachToEngine(EngineModule engine) {
        super.attachToModule(engine);
        rotatableModuleController.attachToModule(engine);
        this.module = engine;
    }

    public void onCommand(String command) {
        if(module == null)
            return;
        if(BaseModuleController.checkIfSupported(command)) {
            super.onCommand(command);
            return;
        } else if(RotatableModuleController.checkIfSupported(command)) {
            rotatableModuleController.onCommand(command);
            return;
        }
        Matcher match = setThrustCommandPattern.matcher(command);
        if(match.matches()) {
            ArgumentsReader reader = new ArgumentsReader(match.group("ARGS"));
            Double thrust = reader.readDouble();
            if(thrust == null) {
                return;
            }
            thrust = Math.min(thrust, module.getMaxThrust());
            module.setThrust(thrust);
        }
    }

    public Message onRequest(Integer transactionId, String request) {
        if(module == null)
            return null;
        if(BaseModuleController.checkIfSupported(request)) {
            return super.onRequest(transactionId, request);
        }
        if(RotatableModuleController.checkIfSupported(request)) {
            return rotatableModuleController.onRequest(transactionId, request);
        }
        if(getCurrentThrustReqPattern.matcher(request).matches()) {
            return new Message(String.valueOf(module.getCurrentThrust()));
        }
        if(getMaxThrustReqPattern.matcher(request).matches()) {
            return new Message(String.valueOf(module.getMaxThrust()));
        }
        return null;
    }

}
