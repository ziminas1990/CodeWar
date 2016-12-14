package ru.codewar.module.ship;

import ru.codewar.module.positionedModule.PositionedModule;
import ru.codewar.protocol.module.ModuleController;

import java.util.ArrayList;


public interface Ship extends PositionedModule {

    ArrayList<ModuleController> getModules();
}
