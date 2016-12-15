package ru.codewar.module.types.detonatableModule;

/**
 * Interface for module, that could be detonated (missile, torpedo, mine etc)
 */
public interface DetonatableModuleType {
    double getDestructZone();

    void detonate();
}
