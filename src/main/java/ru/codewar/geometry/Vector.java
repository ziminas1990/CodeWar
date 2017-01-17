package ru.codewar.geometry;

public class Vector extends Point {
    private double length;

    public Vector() {}
    public Vector(double x, double y) {
        setPosition(x, y);
    }
    public Vector(Vector other) { super(other); }
    public Vector(Point from, Point to) {
        super(to.getX() - from.getX(), to.getY() - from.getY());
    }

    @Override
    public void setPosition(double x, double y) {
        super.setPosition(x, y);
        updateSelf();
    }

    public double getNormilizedX() {
        return x / length;
    }

    public double getNormilizedY() {
        return y / length;
    }

    public void normalize() {
        this.x /= length;
        this.y /= length;
        length = 1;
    }

    private void updateSelf()
    {
        length = Math.hypot(this.x, this.y);
    }

}
