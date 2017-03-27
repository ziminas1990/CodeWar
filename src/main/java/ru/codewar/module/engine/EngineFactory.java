package ru.codewar.module.engine;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create and configure engine instance by it JSON description
 */
public class EngineFactory {

    private static Logger logger = LoggerFactory.getLogger(EngineFactory.class);

    public static BaseEngine make(JSONObject description, String address) {
        try {
            String model = description.getString("model");
            switch (model) {
                case "platform engine":
                    return makePlatformEngine(address, description.getJSONObject("parameters"));
                default:
                    logger.warn("Can't create engine {}: unsupported engine model \"{}\"", address, model);
                    return null;
            }
        } catch(JSONException exception) {
            logger.warn("Can't create engine! Invalid JSON configuration: {}", exception.toString());
            return null;
        }
    }

    private static PlatformEngine makePlatformEngine(String address, JSONObject parameters) {
        /*
          {
            "model"      : "platform engine",
            "parameters" : {
              "orientation" : "forward",
              "maxThrust"   : 10000
            }
          }
         */
        logger.trace("Creating platform engine {}", address);
        try {
            String orientation = parameters.getString("orientation");
            int maxThrust = parameters.getInt("maxThrust");

            switch (orientation) {
                case "forward":
                    return new PlatformEngine(address, PlatformEngine.Orientation.eOrientationForward, maxThrust);
                case "backward":
                    return new PlatformEngine(address, PlatformEngine.Orientation.eOrientationBackward, maxThrust);
                case "left":
                    return new PlatformEngine(address, PlatformEngine.Orientation.eOrientationLeft, maxThrust);
                case "right":
                    return new PlatformEngine(address, PlatformEngine.Orientation.eOrientationRight, maxThrust);
                default:
                    logger.warn("Can't create platform engine {}: \"orientation\" value \"{}\" is invalid!" +
                            "Expected are: forward, backward, left, right", address, orientation);
                    return null;
            }

        } catch(JSONException exception) {
            logger.warn("Can't create platform engine {}! Reason: {}", address, exception);
            return null;
        }
    }
}
