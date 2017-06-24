package ru.codewar.module.scaner;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.module.*;
import ru.codewar.world.ISolarSystem;

public class ScannerLoader implements IModulesLoader {

    private static Logger logger = LoggerFactory.getLogger(ScannerLoader.class);

    private ISolarSystem solarSystem;

    public ScannerLoader(ISolarSystem solarSystem)
    {
        this.solarSystem = solarSystem;
    }

    @Override
    public boolean isSupported(String type, String model) {
        return type.equals(BaseScanner.moduleType) && model.equals(BaseScanner.moduleModel);
    }

    @Override
    public boolean isPlatformed(String type, String model) {
        return true;
    }

    @Override
    public IPlatformedModule makeModule(String type, String model,
                                        String address, JSONObject data, IModulesPlatform platform) {
        if(!type.equals(BaseScanner.moduleType)) {
            logger.warn("Can't create module with type {}!", type);
            return null;
        }

        double resolution;
        try {
            resolution = data.getDouble("resolution");
        } catch (JSONException exception) {
            logger.warn("Can't create scanner! Invalid JSON configuration: {}", exception);
            return null;
        }

        BaseScanner scanner;
        if(model.equals(BaseScanner.moduleModel)) {
            scanner = new BaseScanner(platform, address, solarSystem);
            scanner.setResolution(resolution);
        } else {
            logger.warn("Can't create {} module \"{}\"!", type, model);
            return null;
        }
        logger.debug("{} module \"{}\" has been created as {}", type, model, address);
        return scanner;
    }

    @Override
    public IBaseModule makeModule(String type, String model, String address, JSONObject data) {
        return null;
    }

    @Override
    public ModuleTerminal makeTerminal(IBaseModule module) {
        BaseScanner scanner = (BaseScanner)module;
        if(scanner == null) {
            logger.warn("Can't create terminal for {} module! Type NOT supported",
                    module.getModuleType());
            return null;
        }

        ScannerController controller = new ScannerController();
        ScannerOperator operator = new ScannerOperator();
        controller.attachToModule(scanner);
        controller.attachToOperator(operator);
        operator.attachToModuleController(controller);
        return new ModuleTerminal(scanner, controller, operator);
    }
}
