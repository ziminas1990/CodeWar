package ru.codewar.logicconveyor.physicallogic;

import org.json.JSONObject;
import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;


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
        this.mass = parameters.getInt("mass");
        this.signature = parameters.getInt("signature");
        if(parameters.has("position")) {
            this.position = new Point(parameters.getJSONArray("position"));
        } else {
            this.position = new Point();
        }
        if(parameters.has("velocity")) {
            this.velocity = new Vector(parameters.getJSONArray("velocity"));
        } else {
            this.velocity = new Vector();
        }
    }

    public Vector getVelocity() { return velocity; }
    public Point getPosition() { return position; }
    public double getSignature() { return signature; }
    public double getMass() { return mass; }

    public void pushForce(Vector force) { this.forces.move(force); }
    public Vector getForce() { return forces; }
}
