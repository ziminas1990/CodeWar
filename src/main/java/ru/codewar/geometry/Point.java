package ru.codewar.geometry;

import ru.codewar.module.positionedModule.PositionedModule;

/**
 * Created by Александр on 08.12.2016.
 */
public class Point {
    double x;
    double y;

    public Point() {}
    public Point(double x, double y) {
        setPosition(x, y);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
