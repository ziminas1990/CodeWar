package ru.codewar.module;


public interface IBaseModule {

    String getModuleAddress();
    String getModuleType();
    String getModuleModel();
    String getModuleInfo();

    /**
     * Let module's logic to imitate work for some in-game time interval
     * @param dt in-game time to work (in milliseconds)
     */
    default void alive(double dt) {}
}
