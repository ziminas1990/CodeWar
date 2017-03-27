package ru.codewar.module;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.engine.BaseEngine;
import ru.codewar.module.engine.EngineFactory;

public class ModulesFactory {

    private static Logger logger = LoggerFactory.getLogger(ModulesFactory.class);

    public static BaseModuleInterface make(JSONObject data, String address) {
        try {
            String type = data.getString("type");
            if (type.equals(BaseEngine.moduleType)) {
                return EngineFactory.make(data, address);
            }
            logger.warn("Can't make module with type \"{}\": type not supported!", data.getString("type"));
            return null;
        } catch (JSONException exception) {
            logger.warn("Can't make module \"{}\"! Invalid JSON configuration: {}", exception.toString());
            return  null;
        }
    }
}
