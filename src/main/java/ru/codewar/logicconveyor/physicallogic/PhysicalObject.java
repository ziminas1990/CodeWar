package ru.codewar.logicconveyor.physicallogic;

import org.json.JSONException;
import org.json.JSONObject;
import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;

public interface PhysicalObject {

    Point getPosition();
    double getSignature();
    double getMass();

    Vector getVelocity();

    void pushForce(Vector force);
    Vector getForce();

    default JSONObject toJson() {
        return new JSONObject()
                .put("position", getPosition().toJson())
                .put("signature", getSignature())
                .put("mass", getMass())
                .put("velocity", getVelocity().toJson());
    }

    static PhysicalObject fromJson(JSONObject parameters) {
        return new JsonReader(parameters);
    }

    class JsonReader implements PhysicalObject
    {
        public JsonReader(JSONObject parameters) {
            this.parameters = parameters;
        }

        @Override
        public Point getPosition() {
            return parameters.has("position") ? new Point(parameters.getJSONArray("position")) : new Point();
        }
        @Override
        public double getSignature() {
            try {
                return parameters.getDouble("signature");
            } catch (JSONException e) {
                return 0;
            }
        }
        @Override
        public double getMass() {
            try {
                return parameters.getDouble("mass");
            } catch (JSONException e) {
                return 0;
            }
        }
        @Override
        public Vector getVelocity() {
            return parameters.has("velocity") ? new Vector(parameters.getJSONArray("velocity")) : new Vector();
        }
        @Override
        public void pushForce(Vector force) {}
        public Vector getForce() { return new Vector(); }

        private JSONObject parameters;
    }
}
