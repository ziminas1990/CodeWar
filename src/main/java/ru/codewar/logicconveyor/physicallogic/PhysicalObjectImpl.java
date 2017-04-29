package ru.codewar.logicconveyor.physicallogic;

import org.json.JSONObject;
import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.physicallogic.PhysicalObject;


public class PhysicalObjectImpl implements PhysicalObject {

    private double mass;
    private double signature;
    private Point position;
    private Vector velocity;
    private Vector forces;

    public PhysicalObjectImpl(double mass, double signature) {
        this.mass = mass;
        this.signature = signature;
        this.position = new Point();
        this.velocity = new Vector();
        this.forces = new Vector();
    }

    public PhysicalObjectImpl(double mass, double signature, Point position) {
        this.mass = mass;
        this.signature = signature;
        this.position = new Point(position);
        this.velocity = new Vector();
        this.forces = new Vector();
    }

    public PhysicalObjectImpl(double mass, double signature, Point position, Vector velocity) {
        this.mass = mass;
        this.position = new Point(position);
        this.signature = signature;
        this.velocity = new Vector(velocity);
        this.forces = new Vector();
    }

    public PhysicalObjectImpl(JSONObject parameters) {
        PhysicalObject data = PhysicalObject.fromJson(parameters);
        this.mass = data.getMass();
        this.signature = data.getSignature();
        this.position = data.getPosition();
        this.velocity = data.getVelocity();
        this.forces = data.getForce();
    }

    public Vector getVelocity() { return velocity; }
    public Point getPosition() { return position; }
    public double getSignature() { return signature; }
    public double getMass() { return mass; }

    public void pushForce(Vector force) { this.forces.move(force); }
    public Vector getForce() { return forces; }
}
