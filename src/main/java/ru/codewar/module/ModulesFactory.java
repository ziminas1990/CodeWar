package ru.codewar.module;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.engine.BaseEngine;
import ru.codewar.module.engine.EngineFactory;
import ru.codewar.world.IWorld;

public class ModulesFactory {

    private static Logger logger = LoggerFactory.getLogger(ModulesFactory.class);
    private static IWorld world;

    public static void attachToWorld(IWorld world) { ModulesFactory.world = world; }

    public static IPlatformedModule make(JSONObject data, IModulesPlatform platform)
    {
        try {
            String type = data.getString("type");
            if (type.equals(BaseEngine.moduleType)) {
                return EngineFactory.make(data, platform);
            }
            logger.warn("Can't make module with type \"{}\": type not supported!", data.getString("type"));
            return null;
        } catch (JSONException exception) {
            logger.warn("Can't make module \"{}\"! Invalid JSON configuration: {}", exception.toString());
            return  null;
        }
    }
}
