package ru.codewar.module.ship;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.IBaseModule;
import ru.codewar.module.ModulesFactory;

public class ShipFactory {

    Logger logger = LoggerFactory.getLogger(ShipFactory.class);

    public BaseShip make(JSONObject description, String rootAddress) {
        try {
            String address = rootAddress + "." + description.getString("address");
            BaseShip ship = makeShipByModel(
                    description.getString("model"), address, description.getJSONObject("parameters"));
            if(ship == null) {
                logger.warn("Can't create ship {}", address);
                return null;
            }

            JSONArray modules = description.getJSONArray("modules");
            for(int id = 0; id < modules.length(); id++) {
                JSONObject parameters = modules.getJSONObject(id);
                IBaseModule module = ModulesFactory.make(parameters, ship);
                if(module != null) {
                    ship.addModule(module);
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

    private BaseShip makeShipByModel(String model, String address, JSONObject parameters) {
        if(model.equals(BaseShip.getShipModel())) {
            return new BaseShip(address, parameters);
        }
        logger.warn("Can't create ship! Model \"{}\" is unknown!", model);
        return null;
    }

}
