package ru.codewar.module.engine;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.*;
import ru.codewar.protocol.module.ModuleOperator;

/**
 * Create and configure engine instance by it JSON description
 */
public class EngineLoader implements IModulesLoader {

    private static Logger logger = LoggerFactory.getLogger(EngineLoader.class);

    @Override // from IModulesLoader
    public boolean isSupported(String type, String model) {
        return type.equals("engine") && (
                model.equals("base engine") || model.equals("platform engine"));
    }

    @Override // from IModulesLoader
    public boolean isPlatformed(String type, String model) { return true; }

    @Override // from IModulesLoader
    public IPlatformedModule makeModule(String type, String model, String address,
                                        JSONObject data, IModulesPlatform platform) {
        switch (model) {
            case "platform engine":
                return makePlatformEngine(data, platform, address);
            default:
                logger.warn("Can't create engine {}: unsupported engine model \"{}\"",
                        platform.getPlatformAddress(), model);
                return null;
        }
    }

    @Override // from IModulesLoader
    public IBaseModule makeModule(String type, String model, String address, JSONObject data) { return null; }

    @Override // from IModulesLoader
    public ModuleTerminal makeTerminal(IBaseModule module)
    {
        EngineModule engine = (EngineModule)module;
        if(engine == null)
            return null;

        EngineController controller = new EngineController();
        ModuleOperator operator = new ModuleOperator();

        controller.attachToEngine(engine);
        operator.attachToModuleController(controller);
        return new ModuleTerminal(module, controller, operator);
    }

    private static PlatformEngine makePlatformEngine(
            JSONObject parameters, IModulesPlatform platform, String address) {
        /*
          {
            "model"      : "platform engine",
            "parameters" : {
              "orientation" : "forward",
              "maxThrust"   : 10000
            }
          }
         */
        try {
            logger.trace("Creating platform engine {}", address);
            String orientation = parameters.getString("orientation");
            int maxThrust = parameters.getInt("maxThrust");

            switch (orientation) {
                case "forward":
                    return new PlatformEngine(
                            platform, address, PlatformEngine.Orientation.eOrientationForward, maxThrust);
                case "backward":
                    return new PlatformEngine(
                            platform, address, PlatformEngine.Orientation.eOrientationBackward, maxThrust);
                case "left":
                    return new PlatformEngine(
                            platform, address, PlatformEngine.Orientation.eOrientationLeft, maxThrust);
                case "right":
                    return new PlatformEngine(
                            platform, address, PlatformEngine.Orientation.eOrientationRight, maxThrust);
                default:
                    logger.warn(
                            "Can't create platform engine {}: \"orientation\" value \"{}\" is invalid!" +
                            " Expected are: forward, backward, left, right", address, orientation);
                    return null;
            }

        } catch(JSONException exception) {
            logger.warn("Can't create platform engine {}! Reason: ", address, exception);
            return null;
        }
    }
}
