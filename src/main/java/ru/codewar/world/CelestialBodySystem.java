package ru.codewar.world;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.util.RandomValue;

import java.util.Random;
import java.util.Vector;

public class CelestialBodySystem {

    private static Logger logger = LoggerFactory.getLogger(CelestialBodySystem.class);

    public static CelestialBodySystem readFromJson(JSONObject params, String parentName) {
        CelestialBodySystem system = new CelestialBodySystem();
        if(!parentName.isEmpty())
            parentName += "->";
        try {
            system.type = CelestialBody.BodyType.fromString(params.getString("type"));
            system.mass = RandomValue.readFromJson(params.getJSONArray("mass"));
            system.signature = RandomValue.readFromJson(params.getJSONArray("signature"));
            system.name = (params.has("name")) ? params.getString("name") : "unnamed";
            if(params.has("parentOrbit"))
                system.parentOrbitRadius = RandomValue.readFromJson(params.getJSONArray("parentOrbit"));
            if(params.has("numberOfAsteroids")) {
                system.numberOfAsteroids = params.getInt("numberOfAsteroids");
                if(system.numberOfAsteroids > 0) {
                    system.asteroidsRadius = RandomValue.readFromJson(params.getJSONArray("asteroidsRadius"));
                    system.asteroidsOrbitRadius = RandomValue.readFromJson(params.getJSONArray("asteroidsOrbitRadius"));
                }
            }
            if(params.has("moons")) {
                JSONArray moons = params.getJSONArray("moons");
                if(moons.length() > 0)
                    system.moons = new Vector<>(moons.length());
                for(int i = 0; i < moons.length(); i++) {
                    CelestialBodySystem moon =
                            CelestialBodySystem.readFromJson(moons.getJSONObject(i), parentName + system.name);
                    if(moon != null)
                        system.moons.add(moon);
                }
            }
        } catch (JSONException exception) {
            String name = parentName + ((params.has("name")) ? params.getString("name") : "unnamed");
            logger.warn("Can't read parameters of \"{}\" system! Reason: {}", name, exception);
            return null;
        }
        return system;
    }


    CelestialBody createCentralBody(Random random) {
        return new CelestialBody(type, name, mass.getNextValue(random), signature.getNextValue(random));
    }

    // Parameters of central celestial body in system
    public CelestialBody.BodyType type;
    public String name;
    public RandomValue mass;
    public RandomValue signature;
    public RandomValue parentOrbitRadius;
    // parameters of system components
    public java.util.Vector<CelestialBodySystem> moons;
    public int numberOfAsteroids;
    public RandomValue asteroidsRadius;
    public RandomValue asteroidsOrbitRadius;
}