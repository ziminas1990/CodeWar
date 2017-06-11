package ru.codewar.module;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.engine.EngineLoader;
import ru.codewar.world.IWorld;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ModulesFactory implements IModulesFactory {

    private Logger logger = LoggerFactory.getLogger(ModulesFactory.class);
    private ArrayList<IModulesLoader> loaders;
    private IWorld world;

    public ModulesFactory() {
        loaders.add(new EngineLoader());
    }

    public void attachToWorld(IWorld world) { this.world = world; }

    @Override // from IModulesFactory
    public IBaseModule make(JSONObject data)
    {
        try {
            String type = data.getString("type");
            String model = data.getString("model");
            String address = data.getString("address");
            JSONObject params = data.getJSONObject("parameters");

            for(IModulesLoader factory : loaders) {
                if(factory.isSupported(type, model)) {
                    return factory.makeModule(type, model, address, params);
                }
            }
            logger.warn("Can't find factory for module: \"{}\" type: \"{}\" model: \"{}\"!", address, type, model);
            return null;
        } catch (JSONException exception) {
            logger.warn("Can't make module! Invalid JSON configuration: {}", exception.toString());
            return  null;
        }
    }

    @Override // from IModulesFactory
    public IPlatformedModule make(JSONObject data, IModulesPlatform platform)
    {
        try {
            String type = data.getString("type");
            String model = data.getString("model");
            String address = data.getString("address");
            JSONObject params = data.getJSONObject("parameters");

            for(IModulesLoader factory : loaders) {
                if(factory.isSupported(type, model) && factory.isPlatformed(type, model)) {
                    return factory.makeModule(type, model, address, params, platform);
                }
            }
            logger.warn("Can't find factory for module: \"{}\" type: \"{}\" model: \"{}\"!", address, type, model);
            return null;
        } catch (JSONException exception) {
            logger.warn("Can't make module! Invalid JSON configuration: {}", exception.toString());
            return  null;
        }
    }
}
