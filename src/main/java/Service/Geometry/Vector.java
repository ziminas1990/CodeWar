package Service.Geometry;

/**
 * Created by Александр on 08.12.2016.
 */
public class Vector extends Point {
    private double length;

    @Override
    public void setPosition(double x, double y) {
        super.setPosition(x, y);
    }

    public void normalize() {
        x /= length;
        y /= length;
        length = 1;
    }

    private void updateSelf()
    {
        length = Math.hypot(x, y);
    }

}
