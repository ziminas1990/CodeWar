package ru.codewar.module.engine;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.ModulesPlatform;

/**
 * Create and configure engine instance by it JSON description
 */
public class EngineFactory {

    private static Logger logger = LoggerFactory.getLogger(EngineFactory.class);

    public static BaseEngine make(JSONObject description, ModulesPlatform platform) {
        String address = ".invalid";
        try {
            String model = description.getString("model");
            address = description.getString("address");
            switch (model) {
                case "platform engine":
                    return makePlatformEngine(description.getJSONObject("parameters"), platform, address);
                default:
                    logger.warn("Can't create engine {}: unsupported engine model \"{}\"",
                            platform.getPlatformAddress(), model);
                    return null;
            }
        } catch(JSONException exception) {
            logger.warn("Can't create engine {}! Invalid JSON configuration: {}",
                    platform.getPlatformAddress() + address, exception.toString());
            return null;
        }
    }

    private static PlatformEngine makePlatformEngine(
            JSONObject parameters, ModulesPlatform platform, String address) {
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
