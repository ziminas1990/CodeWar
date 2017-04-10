package ru.codewar.world;


import ru.codewar.module.multiplexer.Multiplexer;
import ru.codewar.module.multiplexer.MultiplexerOperator;
import ru.codewar.module.ship.ShipModule;
import ru.codewar.networking.Channel;

public class Player {

    private String login;
    private Multiplexer multiplexer = new Multiplexer();
    private ShipModule ship;

    public Player(String login) {
        this.login = login;
    }

    public void attachToChannel(Channel channel) {
        multiplexer.attachToChannel(channel);
    }

    public void setShip(ShipModule shipLogic) {
        ship = shipLogic;
        multiplexer.addModule(shipLogic);
        multiplexer.addModulesInstalledOn(ship);
    }

    public String getLogin() { return login; }

    public MultiplexerOperator getMessagesEntryPoint()
    {
        return multiplexer.getOperator();
    }
    public ShipModule getActiveShip() { return ship; }

}
