package ru.codewar.visualizer;

import ru.codewar.geometry.Point;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Picaso {
    private static Ellipse2D.Double ellipse = new Ellipse2D.Double(0, 0, 0, 0);

    public static void drawCircle(Graphics2D g, Point center, double r) {
        ellipse.x = center.getX() - r;
        ellipse.y = center.getY() - r;
        ellipse.width  = 2 * r;
        ellipse.height = 2 * r;
        g.draw(ellipse);
    }
}
