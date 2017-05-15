package ru.codewar.world;

import java.util.ArrayList;

public interface ISolarSystem {

    ArrayList<CelestialBody> getStars();
    ArrayList<CelestialBody> getPlanets();
    ArrayList<CelestialBody> getMoons();
    ArrayList<CelestialBody> getAsteroids();

    default void addCelestialBody(CelestialBody body) {
        switch (body.getType()) {
            case STAR:
                getStars().add(body);
                break;
            case PLANET:
                getPlanets().add(body);
                break;
            case MOON:
                getMoons().add(body);
                break;
            case ASTEROID:
                getAsteroids().add(body);
                break;
            default:
        }
    }

    default void removeCelestialBody(CelestialBody body) {
        switch (body.getType()) {
            case STAR:
                getStars().remove(body);
                break;
            case PLANET:
                getPlanets().remove(body);
                break;
            case MOON:
                getMoons().remove(body);
                break;
            case ASTEROID:
                getAsteroids().remove(body);
                break;
            default:
        }
    }
}
