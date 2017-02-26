package ru.codewar.world;

import ru.codewar.networking.Message;
import ru.codewar.networking.NetworkTerminal;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerGate {

    private static Pattern loginPattern = Pattern.compile("\\s*(\\w+)\\s+(\\w+)\\s*");

    Map<String, String> playersPasswords = new HashMap<>();
    World world;

    public PlayerGate(World world) {
        this.world = world;
    }

    public void addPlayer(String login, String password) {
        playersPasswords.put(login, password);
    }

    public Player login(Message loginRequest)
    {
        if(world == null) {
            return null;
        }
        Matcher loginMatch = loginPattern.matcher(loginRequest.data);
        if(loginMatch.matches()) {
            return login(loginMatch.group(1), loginMatch.group(2));
        } else {
            return null;
        }
    }

    private Player login(String login, String password)
    {
        if(!playersPasswords.get(login).equals(password)) {
            return null;
        }
        return world.getPlayer(login);
    }

}
