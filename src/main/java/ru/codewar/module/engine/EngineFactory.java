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

    public static EngineModule make(JSONObject description) {
        logger.trace("Creating engine module by description: " + description);
        try {
            String model = description.getString("model");
            switch (model) {
                case "platform engine":
                    return makePlatformEngine(description.getJSONObject("parameters"));
                default:
                    logger.warn("Unsupported engine model \"{}\"", model);
                    return null;
            }
        } catch(JSONException exception) {
            logger.warn("Can't create engine: {}", exception.toString());
            return null;
        }
    }

    private static PlatformEngine makePlatformEngine(JSONObject parameters) {
        /*
          {
            "model"      : "platform engine",
            "parameters" : {
              "orientation" : "forward",
              "maxThrust"   : 10000
            }
          }
         */
        logger.trace("Creating platform engine");
        try {
            String orientation = parameters.getString("orientation");
            int maxThrust = parameters.getInt("maxThrust");

            switch (orientation) {
                case "forward":
                    return new PlatformEngine(PlatformEngine.Orientation.eOrientationForward, maxThrust);
                case "backward":
                    return new PlatformEngine(PlatformEngine.Orientation.eOrientationBackward, maxThrust);
                case "left":
                    return new PlatformEngine(PlatformEngine.Orientation.eOrientationLeft, maxThrust);
                case "right":
                    return new PlatformEngine(PlatformEngine.Orientation.eOrientationRight, maxThrust);
                default:
                    logger.warn("Can't create platform engine: \"orientation\" value \"{}\" is invalid!" +
                            "Expected are: forward, backward, left, right", orientation);
                    return null;
            }

        } catch(JSONException exception) {
            logger.warn("Can't create platform engine! Reason: {}", exception);
            return null;
        }
    }
}
