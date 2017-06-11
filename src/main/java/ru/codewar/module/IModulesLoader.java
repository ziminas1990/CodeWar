package ru.codewar.module;

import org.json.JSONObject;
import ru.codewar.protocol.module.ModuleOperator;

// Loader is used to create a concrete instance of module by it's type/model and parameters
// It's also used to create controller and operator instance for concrete module to provide
// remote control
public interface IModulesLoader {

    // returns true, if this factory is able to create module
    boolean isSupported(String type, String model);
    // returns true, if described module is platformed module
    boolean isPlatformed(String type, String model);

    // Creating platformed module
    // Should be used, if either isSupported() or isPlatformed() returned TRUE
    IPlatformedModule makeModule(String type, String model, String address,
                                 JSONObject data, IModulesPlatform platform);

    // Creating non-platformed module
    // Should be used, if isSupported() returned TRUE and isPlatformed() returned FALSE
    IBaseModule makeModule(String type, String model, String address, JSONObject data);

    // Creating controller for module and linking it with module
    BaseModuleController makeController(IBaseModule module);

    // Creating operator for module and linking it with module and controller
    ModuleOperator makeOperator(IBaseModule module, BaseModuleController controller);
}