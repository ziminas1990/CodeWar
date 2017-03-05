package ru.codewar.module.ship;

import ru.codewar.module.BaseModuleInterface;
import ru.codewar.module.types.positionedModule.PositionedModuleType;
import ru.codewar.module.types.rotatableModule.RotatableModuleType;
import ru.codewar.protocol.module.ModuleController;

import java.util.ArrayList;


public interface ShipModule extends BaseModuleInterface, PositionedModuleType, RotatableModuleType {}
