package ru.codewar.world;


import ru.codewar.module.multiplexer.Multiplexer;
import ru.codewar.module.multiplexer.MultiplexerController;
import ru.codewar.module.multiplexer.MultiplexerOperator;
import ru.codewar.networking.Channel;

public class Player {

    private String login;
    private Multiplexer multiplexer;
    private MultiplexerController multiplexerController;
    private MultiplexerOperator multiplexerOperator;

    public Player(String login) {
        this.login = login;
        multiplexer = new Multiplexer();
        multiplexerController = new MultiplexerController();
        multiplexerOperator = new MultiplexerOperator("root");

        multiplexerController.attachToMultiplexer(multiplexer);
        multiplexerController.attachToOperator(multiplexerOperator);
        multiplexerOperator.attachToModuleController(multiplexerController);
    }

    public void attachToChannel(Channel channel) {
        multiplexer.attachToChannel(channel);
        multiplexerOperator.attachToChannel(channel);
    }

    public String getLogin() { return login; }

    public MultiplexerOperator getMessagesEntryPoint()
    {
        return multiplexerOperator;
    }


}
