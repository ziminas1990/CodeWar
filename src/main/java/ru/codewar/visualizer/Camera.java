package ru.codewar.visualizer;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Camera {
    private Point center = new Point(0, 0);
    private double MPP = 1;  // meters per pixel

    public Camera move(Vector direction) {
        center.move(direction);
        return this;
    }
    public Camera scale(double factor) {
        MPP *= factor;
        return this;
    }

    public AffineTransform getAffineTransformForImage(BufferedImage image) {
        AffineTransform transform = new AffineTransform();
        transform.translate(
                image.getWidth() / 2 - center.getX(),
                image.getHeight() / 2 - center.getY());
        transform.scale(1/MPP, 1/MPP);
        return transform;
    }
}
