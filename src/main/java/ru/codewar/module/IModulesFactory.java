package ru.codewar.module;

import org.json.JSONObject;

// Factory is used to create module by it's full json description
// Factory uses loaders to create concrete module
public interface IModulesFactory {
    IBaseModule make(JSONObject data);
    IPlatformedModule make(JSONObject data, IModulesPlatform platform);
    ModuleTerminal makeTerminal(IBaseModule module);
}
