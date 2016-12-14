package ru.codewar.geometry;

/**
 * Created by Александр on 08.12.2016.
 */
public class Vector extends Point {
    private double length;

    public Vector() {}
    public Vector(double x, double y) {
        super(x, y);
    }
    public Vector(Point from, Point to) {
        super(to.getX() - from.getX(), to.getY() - from.getY());
    }

    @Override
    public void setPosition(double x, double y) {
        super.setPosition(this.x, this.y);
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
