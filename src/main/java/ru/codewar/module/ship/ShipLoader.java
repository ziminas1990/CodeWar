package ru.codewar.module.ship;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.*;
import ru.codewar.protocol.module.ModuleOperator;

public class ShipLoader implements IModulesLoader {

    Logger logger = LoggerFactory.getLogger(ShipLoader.class);
    IModulesFactory modulesFactory;

    public ShipLoader(IModulesFactory modulesFactory) {
        this.modulesFactory = modulesFactory;
    }

    @Override // from IModulesLoader
    public boolean isSupported(String type, String model) {
        return type.equals("ship") && model.equals("base ship");
    }
    @Override // from IModulesLoader
    public boolean isPlatformed(String type, String model) { return false; }

    @Override // from IModulesLoader
    public IPlatformedModule makeModule(String type, String model, String address,
                                        JSONObject data, IModulesPlatform platform) {
        return null;
    }

    @Override // from IModulesLoader
    public IBaseModule makeModule(String type, String model, String address, JSONObject data) {
        try {
            ShipModule ship = makeShipByModel(model, address, data);
            if(ship == null) {
                logger.warn("Can't create ship {}", address);
                return null;
            }

            JSONArray modules = data.getJSONArray("modules");
            for(int id = 0; id < modules.length(); id++) {
                JSONObject parameters = modules.getJSONObject(id);
                IPlatformedModule module = modulesFactory.make(parameters, ship);
                if(module != null) {
                    ship.addModule(module);
                    module.installOnPlatform(ship);
                } else {
                    logger.warn("Can't create module #{} with address {}!",
                            id, address + "." + parameters.getString("address"));
                }
            }
            return ship;
        } catch (JSONException exception) {
            logger.warn("Can't create ship! Invalid JSON configuration: {}", exception.toString());
            return null;
        }
    }

    @Override // from IModulesLoader
    public ModuleTerminal makeTerminal(IBaseModule module)
    {
        ShipModule ship = (ShipModule)module;
        if(ship == null)
            return null;

        ShipController controller = new ShipController();
        ModuleOperator operator = new ModuleOperator();

        controller.attachToModule(ship);
        operator.attachToModuleController(controller);

        return new ModuleTerminal(module, controller, operator);
    }

    private ShipModule makeShipByModel(String model, String address, JSONObject parameters) {
        if(model.equals(BaseShip.moduleModel)) {
            return new BaseShip(address, parameters);
        }
        logger.warn("Can't create ship! Model \"{}\" is unknown!", model);
        return null;
    }
}
