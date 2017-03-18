package ru.codewar.module;

import ru.codewar.protocol.module.ModuleOperator;

/*
    +------------------------------------------+
    | BaseModuleStack                          |
    |                                          |
    |  +------------------------------------+  |
    |  |        BaseMooduleInterface        |  |
    |  +------------------------------------+  |
    |         ^        |              |        |
    |         |        V              |        |
    |  +-----------------------+      |        |
    |  | BaseModuleController  |      |        |
    |  +-----------------------+      |        |
    |         ^        |              |        |
    |         |        V              V        |
    |  +------------------------------------+  |
    |  |           ModuleOperator           |  |
    |  +------------------------------------+  |
    |                  ^                       |
    +------------------|-----------------------+
                       V
              +-----------------+
              |     Channel     |
              +-----------------+
 */

public interface ModuleStack {

    BaseModuleInterface getBaseModule();
    BaseModuleController getController();
    ModuleOperator getOperator();

}
