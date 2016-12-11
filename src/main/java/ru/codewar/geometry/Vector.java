package ru.codewar.geometry;

/**
 * Created by Александр on 08.12.2016.
 */
public class Vector extends Point {
    private double length;

    @Override
    public void setPosition(double x, double y) {
        super.setPosition(this.x, this.y);
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
