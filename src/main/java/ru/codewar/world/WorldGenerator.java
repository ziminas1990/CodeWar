package ru.codewar.world;

import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.physicallogic.PhysicalLogic;
import ru.codewar.logicconveyor.physicallogic.PhysicalObject;

import java.util.Random;

public class WorldGenerator {

    Random random;

    public WorldGenerator(long seed) {
        random = new Random(seed);
    }

    public void generateWorld(CelestialBodySystem centralBody, World world) {
        CelestialBody body = centralBody.createCentralBody(random);
        world.getSolarSystem().addCelestialBody(body);
        createBodyEnviroment(body, centralBody, world.getSolarSystem());
    }

    public void createBodyEnviroment(CelestialBody body, CelestialBodySystem params, ISolarSystem system) {
        for(CelestialBodySystem moonParams : params.moons) {
            if(moonParams.parentOrbitRadius == null)
                continue;
            CelestialBody moon = moonParams.createCentralBody(random);
            system.addCelestialBody(moon);
            setObjectToOrbit(moon, body, moonParams.parentOrbitRadius.getNextValue(random));
            createBodyEnviroment(moon, moonParams, system);
        }

        for(int i = 0; i < params.numberOfAsteroids; i++) {
            double signature = params.asteroidsRadius.getNextValue(random);
            double mass = 3000 * 1.333 * Math.PI * Math.pow(signature, 3);
            CelestialBody asteroid = new CelestialBody(
                    CelestialBody.BodyType.ASTEROID, "", mass, signature);
            setObjectToOrbit(asteroid, body, params.asteroidsOrbitRadius.getNextValue(random));
            system.addCelestialBody(asteroid);
        }
    }

    // Set the position and velocity for orbiter object
    private void setObjectToOrbit(PhysicalObject orbiter, PhysicalObject orbited, double R) {
        double a = Math.PI * random.nextDouble();

        // place object on some point in orbit
        orbiter.getPosition().setPosition(orbited.getPosition());
        orbiter.getPosition().move(R * Math.cos(a), R * Math.sin(a));

        // set orbiter velocity
        a += Math.PI/2;
        double velocity = Math.sqrt(PhysicalLogic.G * orbited.getMass() / R);
        Vector velocityVector = new Vector(velocity * Math.cos(a), velocity * Math.sin(a));
        velocityVector.move(orbited.getVelocity());
        orbiter.getVelocity().setPosition(velocityVector);
    }


}
