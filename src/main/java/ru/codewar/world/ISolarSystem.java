package ru.codewar.world;

import java.util.ArrayList;

public interface ISolarSystem {

    void addCelestialBody(CelestialBody body);
    void removeCelestialBody(CelestialBody body);
    CelestialBody getSol();
    ArrayList<CelestialBody> getPlanets();
    ArrayList<CelestialBody> getMoons();
    ArrayList<CelestialBody> getAsteroids();
}
