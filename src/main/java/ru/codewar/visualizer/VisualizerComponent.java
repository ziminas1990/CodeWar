package ru.codewar.visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class VisualizerComponent extends JComponent implements IObserver {

    private static final long serialVersionUID = 1L;

    private Camera camera = new Camera();
    private BufferedImage image;

    public VisualizerComponent(int h, int w) {
        image = new BufferedImage(h, w, BufferedImage.TYPE_INT_RGB);
    }

    public int getWidth() { return image.getWidth(); }
    public int getHeight() { return image.getHeight(); }
    public Camera getCamera() { return camera; }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D)g).drawImage(image, null, 0, 0);
    }

    @Override
    public Graphics2D getWhereToPain() {
        Graphics2D g = image.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(Color.WHITE);
        g.setTransform(camera.getAffineTransformForImage(image));
        return g;
    }

    @Override
    public void paintHasBeenUpdated() { repaint(); }
}