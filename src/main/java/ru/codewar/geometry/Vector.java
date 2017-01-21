package ru.codewar.geometry;

public class Vector extends Point {
    private double length;  // Access only via getLength() - lazy semantics is used
    private boolean needToUpdateLength;

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
        needToUpdateLength = true;
    }

    public double getNormilizedX() {
        return x / getLength();
    }

    public double getNormilizedY() {
        return y / getLength();
    }

    public void normalize() {
        this.x /= getLength();
        this.y /= getLength();
        length = 1;
    }

    public double getLength() {
        if(needToUpdateLength) {
            length = Math.hypot(this.x, this.y);
        }
        return length;
    }

}
