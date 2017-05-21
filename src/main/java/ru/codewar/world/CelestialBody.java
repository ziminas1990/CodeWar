package ru.codewar.world;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.physicallogic.PhysicalObject;
import ru.codewar.logicconveyor.physicallogic.PhysicalObjectImpl;


public class CelestialBody extends PhysicalObjectImpl {

    static Logger logger = LoggerFactory.getLogger(CelestialBody.class);

    public enum BodyType {
        UNKNOWN { public String toString() { return "Unknown"; }},
        STAR { public String toString() { return "Star"; }},
        PLANET { public String toString() { return "Planet"; }},
        MOON { public String toString() { return "Moon"; }},
        ASTEROID { public String toString() { return "Asteroid"; }};

        public static BodyType fromString(String value) {
            switch (value.toLowerCase()) {
                case "star":
                    return STAR;
                case "planet":
                    return PLANET;
                case "moon":
                    return MOON;
                case "asteroid":
                    return ASTEROID;
                default:
                    return UNKNOWN;
            }
        }
    }

    private BodyType type = BodyType.UNKNOWN;
    private double sqrSignature = 0;
    private String name = "";

    public CelestialBody(BodyType type, String name,
                         double mass, double signature, Point position, Vector velocity)
    {
        super(mass, signature, position, velocity);
        this.type = type;
        this.name = name;
        this.sqrSignature = signature * signature;
    }

    public CelestialBody(JSONObject parameters)
    {
        super(parameters);
        try {
            type = BodyType.fromString(parameters.getString("type"));
            name = parameters.has("name") ? parameters.getString("name") : "unnamed";
        } catch (JSONException exception) {
            logger.warn("Can't create celestial body! Reason: " + exception);
        }
    }

    public JSONObject toJson() {
        return super.toJson().put("type", type.toString()).put("name", getName());
    }
    public double getSqrOfSignature() { return sqrSignature; }

    public BodyType getType() { return type; }
    public String getName() { return name; }

}
