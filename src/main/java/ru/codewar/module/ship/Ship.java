package ru.codewar.module.ship;


import ru.codewar.logicconveyor.physicallogic.PhysicalObject;
import ru.codewar.networking.Channel;
import ru.codewar.protocol.module.ModuleOperator;

public class Ship {
    private ShipModule logic;
    private ShipController controller = new ShipController();
    private ModuleOperator operator;

    public Ship(ShipModule logic, String address) {
        this.logic = logic;
        operator = new ModuleOperator(address, logic.getModuleType());
        controller.attachToModule(logic);
        operator.attachToModuleController(controller);
    }

    public void attachToChannel(Channel channel) {
        operator.attachToChannel(channel);
    }

    public ModuleOperator getOperator() { return operator; }

    public PhysicalObject asPhysicalObject() {
        return (logic instanceof PhysicalObject) ? (PhysicalObject)logic : null;
    }
}