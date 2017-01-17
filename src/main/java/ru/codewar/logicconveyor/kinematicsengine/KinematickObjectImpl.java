package ru.codewar.logicconveyor.kinematicsengine;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;


public class KinematickObjectImpl implements KinematickObject {

    private Integer objectId;
    private Vector velocity;
    private Point position;
    private double signature;

    public KinematickObjectImpl(int objectId, double signature) {
        this.objectId =  objectId;
        this.signature = signature;
    }

    public KinematickObjectImpl(int objectId, double signature, Point position) {
        this.objectId =  objectId;
        this.signature = signature;
        this.position = new Point(position);
    }

    public KinematickObjectImpl(int objectId, double signature, Point position, Vector velocity) {
        this.objectId =  objectId;
        this.position = new Point(position);
        this.signature = signature;
        this.velocity = new Vector(velocity);
    }

    public Integer getObjectId() { return objectId; }
    public Vector getVelocity() { return velocity; }
    public Point getPosition() { return position; }
    public double getSignature() { return signature; }
}
