package ru.codewar.module;

/**
 * Module, that must be installed on some platform
 * Inherited by modules, that must be installed on some platform, and there logic are depends
 * on position and orientation of module. Ex: engines, radar, cannons etc
 */
public interface IPlatformedModule extends IBaseModule {

    void installOnPlatform(IModulesPlatform platform);
    IModulesPlatform installedOn();
}
