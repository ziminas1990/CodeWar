package ru.codewar.logicconveyor.physicallogic;

import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;


public class PhysicalObjectImpl implements PhysicalObject {

    private Integer objectId;
    private double mass;
    private double signature;
    private Point position;
    private Vector velocity;
    private Vector forces;

    public PhysicalObjectImpl(int objectId, double mass, double signature) {
        this.objectId =  objectId;
        this.mass = mass;
        this.signature = signature;
        this.position = new Point();
        this.velocity = new Vector();
        this.forces = new Vector();
    }

    public PhysicalObjectImpl(int objectId, double mass, double signature, Point position) {
        this.objectId =  objectId;
        this.mass = mass;
        this.signature = signature;
        this.position = new Point(position);
        this.velocity = new Vector();
        this.forces = new Vector();
    }

    public PhysicalObjectImpl(int objectId, double mass, double signature, Point position, Vector velocity) {
        this.objectId =  objectId;
        this.mass = mass;
        this.position = new Point(position);
        this.signature = signature;
        this.velocity = new Vector(velocity);
        this.forces = new Vector();
    }

    public Integer getObjectId() { return objectId; }
    public Vector getVelocity() { return velocity; }
    public Point getPosition() { return position; }
    public double getSignature() { return signature; }
    public double getMass() { return mass; }

    public void pushForce(Vector force) { this.forces.move(force); }
    public Vector getForce() { return forces; }
}
