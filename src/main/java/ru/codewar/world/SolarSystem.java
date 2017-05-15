package ru.codewar.world;

import ru.codewar.logicconveyor.physicallogic.PhysicalLogic;

import java.util.ArrayList;
import java.util.HashSet;

public class SolarSystem implements ISolarSystem {
    private PhysicalLogic physicalEngine;

    private ArrayList<CelestialBody> stars = new ArrayList<>();
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
        if(physicalEngine != null)
            physicalEngine.registerObject(body);
        allObjects.add(body);
        ISolarSystem.super.addCelestialBody(body);
    }

    public void removeCelestialBody(CelestialBody body)
    {
        if(!allObjects.contains(body))
            return;
        if(physicalEngine != null)
            physicalEngine.deregisterObject(body);
        allObjects.remove(body);
        ISolarSystem.super.removeCelestialBody(body);
    }

    public ArrayList<CelestialBody> getStars() { return stars; }
    public ArrayList<CelestialBody> getPlanets() { return planets; }
    public ArrayList<CelestialBody> getMoons() { return moons; }
    public ArrayList<CelestialBody> getAsteroids() { return asteroids; }

}
