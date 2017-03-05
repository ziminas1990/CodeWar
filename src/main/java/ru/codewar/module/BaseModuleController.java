package ru.codewar.module;

import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleController;

import java.util.regex.Pattern;

/*
Base class for every module controller. It implements basic command and requests,
that each module should support.

If you're going to make your own module controller, you should extend this class, as
it shown below:

    public class SomeAwesomeController extends BaseModuleController {

        @Override // ModuleController
        public static boolean checkIfSupported(String message)
        {
            return BaseModuleController.checkIfSupported(message);
        }

        public void attachToModule(ShipModule ship) {
            super.attachToModule(ship);
        }

        @Override // ModuleController
        public void onCommand(String command) {
            if(BaseModuleController.checkIfSupported(command)) {
                super.onCommand(command);
            }
        }

        @Override // ModuleController
        public Message onRequest(Integer transactionId, String request) {
            if(BaseModuleController.checkIfSupported(request)) {
                return super.onRequest(transactionId, request);
            }
            return null;
        }
    }

It is also possible to implement other controller without extending BaseModuleController:

    public class SomeAwesomeController {
        BaseModuleController baseModuleController;

        @Override // ModuleController
        public static boolean checkIfSupported(String message)
        {
            return BaseModuleController.checkIfSupported(message);
        }

        public void attachToModule(ShipModule ship) {
            baseModuleController.attachToModule(ship);
        }

        @Override // ModuleController
        public void onCommand(String command) {
            if(BaseModuleController.checkIfSupported(command)) {
                baseModuleController.onCommand(command);
            }
        }

        @Override // ModuleController
        public Message onRequest(Integer transactionId, String request) {
            if(BaseModuleController.checkIfSupported(request)) {
                return baseModuleController.onRequest(transactionId, request);
            }
            return null;
        }
    }

 */

public class BaseModuleController implements ModuleController {

    private final static Pattern checkPattern = Pattern.compile("getModuleInfo\\s*");
    private final static Pattern getModuleInfo = Pattern.compile("getModuleInfo\\s*");

    private BaseModuleInterface module;

    public static boolean checkIfSupported(String message)
    {
        return checkPattern.matcher(message).matches();
    }

    public void attachToModule(BaseModuleInterface module) { this.module = module; }

    public void onCommand(String command) {}

    public Message onRequest(Integer transactionId, String request) {
        if(getModuleInfo.matcher(request).matches()) {
            return new Message("Type: \"" + module.getType() + "\", Model: \"" + module.getModel() +
                    "\", Parameters: \"" + module.getParameters() + "\"");
        }
        return null;
    }
}
