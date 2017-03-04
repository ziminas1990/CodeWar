package ru.codewar.world;


import ru.codewar.module.multiplexer.Multiplexer;
import ru.codewar.module.multiplexer.MultiplexerOperator;
import ru.codewar.module.ship.Ship;
import ru.codewar.module.ship.ShipController;
import ru.codewar.module.ship.ShipModule;
import ru.codewar.networking.Channel;
import ru.codewar.protocol.module.ModuleOperator;

public class Player {

    private String login;

    private Multiplexer multiplexer = new Multiplexer("root");

    private Ship ship;

    public Player(String login) {
        this.login = login;
    }

    public void attachToChannel(Channel channel) {
        multiplexer.attachToChannel(channel);
    }

    public void setShip(ShipModule shipLogic) {
        ship = new Ship(shipLogic, "ship");
        multiplexer.addModule(ship.getOperator());
    }
    public Ship getShip() { return ship; }

    public String getLogin() { return login; }

    public MultiplexerOperator getMessagesEntryPoint()
    {
        return multiplexer.getOperator();
    }


}
