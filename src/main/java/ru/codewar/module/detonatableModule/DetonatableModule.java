package ru.codewar.module.detonatableModule;

/**
 * Interface for module, that could be detonated (missile, torpedo, mine etc)
 */
public interface DetonatableModule {
    double getDestructZone();

    void detonate();
}
