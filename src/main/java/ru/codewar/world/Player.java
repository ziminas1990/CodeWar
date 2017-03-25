package ru.codewar.world;


import ru.codewar.module.multiplexer.Multiplexer;
import ru.codewar.module.multiplexer.MultiplexerOperator;
import ru.codewar.module.ship.ShipModule;
import ru.codewar.networking.Channel;

public class Player {

    private String login;

    private Multiplexer multiplexer = new Multiplexer();

    public Player(String login) {
        this.login = login;
    }

    public void attachToChannel(Channel channel) {
        multiplexer.attachToChannel(channel);
    }

    public void setShip(ShipModule shipLogic) {
        multiplexer.addModule(shipLogic);
    }

    public String getLogin() { return login; }

    public MultiplexerOperator getMessagesEntryPoint()
    {
        return multiplexer.getOperator();
    }


}
