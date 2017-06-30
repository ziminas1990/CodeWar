package ru.codewar.world;

import java.awt.*;

public interface IWorld {

    ISolarSystem getSolarSystem();

    default void drawSelf(Graphics2D g) { getSolarSystem().drawSelf(g); }

}
