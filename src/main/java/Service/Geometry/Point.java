package Service.Geometry;

/**
 * Created by Александр on 08.12.2016.
 */
public class Point {
    protected double x;
    protected double y;

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

}
