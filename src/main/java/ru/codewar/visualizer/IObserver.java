package ru.codewar.visualizer;

import java.awt.*;

public interface IObserver {
    Graphics2D getWhereToPain();
    void paintHasBeenUpdated();
}
