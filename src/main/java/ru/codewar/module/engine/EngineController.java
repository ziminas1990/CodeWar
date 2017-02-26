package ru.codewar.module.engine;

import ru.codewar.module.types.rotatableModule.RotatableModuleController;

import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleController;
import ru.codewar.util.ArgumentsReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EngineController implements ModuleController {

    private final static Pattern checkPattern =
            Pattern.compile("(getMaxThrust\\s*|getCurrentThrust\\s*|setThrust\\s+.*)");
    private final static Pattern getMaxThrustReqPattern = Pattern.compile("getMaxThrust\\s*");
    private final static Pattern getCurrentThrustReqPattern = Pattern.compile("getCurrentThrust\\s*");
    private final static Pattern setThrustCommandPattern = Pattern.compile("setThrust\\s+(?<ARGS>.+)");

    private Engine module;
    private RotatableModuleController rotatableModuleController = new RotatableModuleController();

    static boolean checkIfSupported(String message) {
        if(RotatableModuleController.checkIfSupported(message))
            return true;
        return checkPattern.matcher(message).matches();
    }

    public void attachToEngine(Engine engine) {
        rotatableModuleController.attachToModule(engine);
        this.module = engine;
    }

    public void onCommand(String command) {
        if(module == null)
            return;
        if(RotatableModuleController.checkIfSupported(command)) {
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
