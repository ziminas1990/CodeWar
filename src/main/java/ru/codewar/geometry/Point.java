package ru.codewar.geometry;

public class Point {
    double x;
    double y;

    public Point() {}
    public Point(Point other) { setPosition(other);}
    public Point(double x, double y) {
        setPosition(x, y);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(Point position) {
        setPosition(position.x, position.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void move(double dx, double dy) { setPosition(x + dx, y + dy); }
    public void move(Vector vector) { setPosition(x + vector.getX(), y + vector.getY()); }

}
