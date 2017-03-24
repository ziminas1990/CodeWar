package ru.codewar.module;

import ru.codewar.networking.Channel;
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

    public BaseModuleInterface getBaseModule();
    public BaseModuleController getController();
    public ModuleOperator getOperator();

    public void attachToChannel(Channel channel);

}
