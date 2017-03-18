package ru.codewar.world;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.database.playerloader.PlayerLoader;
import ru.codewar.networking.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerGate {

    private static Pattern loginPattern = Pattern.compile("\\s*(\\w+)\\s+(\\w+)\\s*");

    private Map<String, String> playersPasswords = new HashMap<>();
    private World world;
    private PlayerLoader playerLoader;
    private Logger logger = LoggerFactory.getLogger(PlayerGate.class);

    public PlayerGate(World world, PlayerLoader playerLoader) {
        this.world = world;
        this.playerLoader = playerLoader;
        logger.info("Player gate has been created");
    }

    public void addPlayer(String login, String password) {
        playersPasswords.put(login, password);
    }

    public Player login(Message loginRequest)
    {
        if(world == null) {
            logger.warn("Can't process login request: PlayerGate is not attached to world instance!");
            return null;
        }
        Matcher loginMatch = loginPattern.matcher(loginRequest.data);
        if(loginMatch.matches()) {
            return login(loginMatch.group(1), loginMatch.group(2));
        } else {
            logger.warn("Can't parse login request \"{}\"", loginRequest);
            return null;
        }
    }

    private Player login(String login, String password)
    {
        logger.trace("Processing login request from {}", login);
        if(!playersPasswords.get(login).equals(password)) {
            logger.info("Login request rejected for {}: password is incorrect", login);
            return null;
        }

        Player player = world.getPlayer(login);
        if(player == null) {
            // we should create player and add him to the world
            player = playerLoader.loadPlayer(login);
            if (player != null) {
                world.addPlayer(player);
            } else {
                logger.warn("Login failed: unable to create player's items!");
            }
        } else {
            logger.trace("Player's items are already created");
        }
        if(player != null) {
            logger.info("Login for {} succeed", login);
        }
        return player;
    }

}
