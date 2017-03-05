package ru.codewar.database.playerloader;

import ru.codewar.world.Player;

public interface PlayerLoader {
    Player loadPlayer(String login);
}
