package ru.codewar.logicconveyor.physicallogic;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;


public class PhysicalObjectImpl implements PhysicalObject {

    private Integer objectId;
    private Point position;
    private Vector velocity;
    private Vector acceleration;
    private double signature;

    public PhysicalObjectImpl(int objectId, double signature) {
        this.objectId =  objectId;
        this.signature = signature;
        this.position = new Point();
        this.velocity = new Vector();
        this.acceleration = new Vector();
    }

    public PhysicalObjectImpl(int objectId, double signature, Point position) {
        this.objectId =  objectId;
        this.signature = signature;
        this.position = new Point(position);
        this.velocity = new Vector();
        this.acceleration = new Vector();
    }

    public PhysicalObjectImpl(int objectId, double signature, Point position, Vector velocity) {
        this.objectId =  objectId;
        this.position = new Point(position);
        this.signature = signature;
        this.velocity = new Vector(velocity);
        this.acceleration = new Vector();
    }

    public PhysicalObjectImpl(int objectId, double signature, Point position, Vector velocity, Vector acceleration) {
        this.objectId =  objectId;
        this.position = new Point(position);
        this.signature = signature;
        this.velocity = new Vector(velocity);
        this.acceleration = new Vector(acceleration);
    }

    public Integer getObjectId() { return objectId; }
    public Vector getVelocity() { return velocity; }
    public Vector getAcceleration() { return acceleration; }
    public Point getPosition() { return position; }
    public double getSignature() { return signature; }
}
