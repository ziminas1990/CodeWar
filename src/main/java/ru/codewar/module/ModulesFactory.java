package ru.codewar.module;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.engine.EngineLoader;
import ru.codewar.module.scaner.ScannerLoader;
import ru.codewar.module.ship.ShipLoader;
import ru.codewar.protocol.module.ModuleOperator;
import ru.codewar.world.IWorld;

import java.util.ArrayList;
import java.util.function.Predicate;

public class ModulesFactory implements IModulesFactory {

    private Logger logger = LoggerFactory.getLogger(ModulesFactory.class);
    private ArrayList<IModulesLoader> loaders = new ArrayList<>();

    public ModulesFactory(IWorld world) {
        loaders.add(new EngineLoader());
        loaders.add(new ShipLoader(this));
        loaders.add(new ScannerLoader(world.getSolarSystem()));
    }

    @Override // from IModulesFactory
    public IBaseModule make(JSONObject data)
    {
        try {
            String type = data.getString("type");
            String model = data.getString("model");
            String address = data.getString("address");
            JSONObject params = data.getJSONObject("parameters");

            for(IModulesLoader loader : loaders) {
                if(loader.isSupported(type, model)) {
                    return loader.makeModule(type, model, address, params);
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

            for(IModulesLoader loader : loaders) {
                if(loader.isSupported(type, model) && loader.isPlatformed(type, model)) {
                    return loader.makeModule(type, model, address, params, platform);
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
    public ModuleTerminal makeTerminal(IBaseModule module)
    {
        for(IModulesLoader loader : loaders) {
            if (loader.isSupported(module.getModuleType(), module.getModuleModel())) {
                return loader.makeTerminal(module);
            }
        }
        logger.warn("Can't create terminal for {} module \"{}\" ({})",
                module.getModuleType(), module.getModuleModel(), module.getModuleAddress());
        return null;
    }
}
