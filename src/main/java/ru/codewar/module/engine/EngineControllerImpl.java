package ru.codewar.module.engine;

import ru.codewar.module.types.rotatableModule.RotatableModuleControllerImpl;

import ru.codewar.protocol.module.ModuleController;
import ru.codewar.util.ArgumentsReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EngineControllerImpl implements ModuleController {

    private final static Pattern checkPattern =
            Pattern.compile("(getMaxThrust\\s*|getCurrentThrust\\s*|setThrust\\s+.*)");
    private final static Pattern getMaxThrustReqPattern = Pattern.compile("getMaxThrust\\s*");
    private final static Pattern getCurrentThrustReqPattern = Pattern.compile("getCurrentThrust\\s*");
    private final static Pattern setThrustCommandPattern = Pattern.compile("setThrust\\s+(?<ARGS>.+)");

    private Engine module;
    private RotatableModuleControllerImpl rotatableModuleController = new RotatableModuleControllerImpl();

    static boolean checkIfSupported(String message) {
        if(RotatableModuleControllerImpl.checkIfSupported(message))
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
        if(RotatableModuleControllerImpl.checkIfSupported(command)) {
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

    public String onRequest(Integer transactionId, String request) {
        if(module == null)
            return null;
        if(RotatableModuleControllerImpl.checkIfSupported(request)) {
            return rotatableModuleController.onRequest(transactionId, request);
        }
        if(getCurrentThrustReqPattern.matcher(request).matches()) {
            return String.valueOf(module.getCurrentThrust());
        }
        if(getMaxThrustReqPattern.matcher(request).matches()) {
            return String.valueOf(module.getMaxThrust());
        }
        return null;
    }

}
