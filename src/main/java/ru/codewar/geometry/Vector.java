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

    public void setPosition(Point begin, Point end) {
        super.setPosition(end.getX() - begin.getX(), end.getY() - begin.getY());
        needToUpdateLength = true;
    }

    public void setPosition(Vector other, double k) {
        super.setPosition(other.getX() * k, other.getY() * k);
        needToUpdateLength = true;
    }

    public double getNormilizedX() {
        return x / getLength();
    }

    public double getNormilizedY() {
        return y / getLength();
    }

    public void normalize() {
        x /= getLength();
        y /= getLength();
        length = 1;
    }

    public void reset() {
        x = 0;
        y = 0;
        length = 0;
        needToUpdateLength = false;
    }

    public void setLength(double length) {
        double factor = length / getLength();
        x *= factor;
        y *= factor;
        this.length = length;
    }

    public void divideLength(double k) {
        if(k == 0)
            return;
        x /= k;
        y /= k;
        this.length /= k;
    }

    public double getLength() {
        if(needToUpdateLength) {
            length = Math.hypot(this.x, this.y);
            needToUpdateLength = false;
        }
        return length;
    }

    public double getSquaredLength() {
        return x * x + y * y;
    }

}
