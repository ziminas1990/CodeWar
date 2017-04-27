package ru.codewar.world;

import ru.codewar.logicconveyor.physicallogic.PhysicalLogic;

import java.util.ArrayList;
import java.util.HashSet;

public class SolarSystem implements ISolarSystem {
    private PhysicalLogic physicalEngine;

    private CelestialBody sol;
    private ArrayList<CelestialBody> planets = new ArrayList<>();
    private ArrayList<CelestialBody> moons = new ArrayList<>();
    private ArrayList<CelestialBody> asteroids = new ArrayList<>();
    private HashSet<CelestialBody> allObjects = new HashSet<>();

    public SolarSystem(PhysicalLogic physicalEngine) {
        this.physicalEngine = physicalEngine;
    }

    public void addCelestialBody(CelestialBody body) {
        if(allObjects.contains(body))
            return;

        switch (body.getType()) {
            case STAR:
                if(sol != null)
                    return;
                sol = body;
                break;
            case PLANET:
                planets.add(body);
                break;
            case MOON:
                moons.add(body);
                break;
            case ASTEROID:
                asteroids.add(body);
                break;
            default:
                return;
        }
        physicalEngine.registerObject(body);
        allObjects.add(body);
    }

    public void removeCelestialBody(CelestialBody body)
    {
        if(!allObjects.contains(body))
            return;

        switch (body.getType()) {
            case STAR:
                if(sol != body)
                    return;
                sol = null;
                break;
            case PLANET:
                planets.remove(body);
                break;
            case MOON:
                moons.remove(body);
                break;
            case ASTEROID:
                asteroids.remove(body);
                break;
            default:
                return;
        }
        physicalEngine.deregisterObject(body);
        allObjects.remove(body);
    }

    public CelestialBody getSol() { return sol; }
    public ArrayList<CelestialBody> getPlanets() { return planets; }
    public ArrayList<CelestialBody> getMoons() { return moons; }
    public ArrayList<CelestialBody> getAsteroids() { return asteroids; }

}
