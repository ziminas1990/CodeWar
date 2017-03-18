package ru.codewar.geometry;

public class Vector extends Point {
    private double length;  // Access only via getLength() - lazy semantics is used
    private boolean needToUpdateLength = true;

    public Vector() {
        needToUpdateLength = false;
    }
    public Vector(double x, double y) {
        setPosition(x, y);
    }
    public Vector(Vector other) { super(other); }
    public Vector(Point from, Point to) {
        super(to.getX() - from.getX(), to.getY() - from.getY());
    }

    private Vector(double x, double y, double length) {
        this.x = x;
        this.y = y;
        this.length = length;
        needToUpdateLength = false;
    }

    public Vector clone() {
        return (needToUpdateLength) ? new Vector(x, y) : new Vector(x, y, length);
    }

    public double scalarProduct(Vector other) {
        return (x * other.x + y * other.y) / (getLength() * other.getLength());
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

    public Vector normalize() {
        x /= getLength();
        y /= getLength();
        length = 1;
        return this;
    }

    public void reset() {
        x = 0;
        y = 0;
        length = 0;
        needToUpdateLength = false;
    }

    public Vector setLength(double length) {
        double factor = length / getLength();
        x *= factor;
        y *= factor;
        this.length = length;
        return this;
    }

    public Vector divideLength(double k) {
        if(k == 0)
            return this;
        x /= k;
        y /= k;
        this.length /= k;
        return this;
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

    public Vector rotate(double angle) {
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);
        double oldX = x;
        x = x * cosAngle - y * sinAngle;
        y = oldX * sinAngle + y * cosAngle;
        return this;
    }

    public Vector rotateTo(double x, double y) {
        double oldLength = getLength();
        this.x = x;
        this.y = y;
        setLength(oldLength);
        return this;
    }

    public Vector getLeftDirection() {
        return (needToUpdateLength) ? new Vector(-y, x) : new Vector(-y, x, length);
    }

    public Vector turnToLeft() {
        double oldX = x;
        x = -y;
        y = oldX;
        return this;
    }

    public Vector getRightDirection() {
        return (needToUpdateLength) ? new Vector(y, -x) : new Vector(y, -x, length);
    }

    public Vector turnToRight() {
        double oldX = x;
        x = y;
        y = -oldX;
        return this;
    }

    public Vector getBackward() {
        return (needToUpdateLength) ? new Vector(-x, -y) : new Vector(-x, -y, length);
    }

    public Vector turnToBackward() {
        x = -x;
        y = -y;
        return this;
    }

}
