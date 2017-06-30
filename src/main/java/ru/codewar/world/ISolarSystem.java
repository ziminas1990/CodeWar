package ru.codewar.world;

import java.awt.*;
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

    default void drawSelf(Graphics2D g) {
        getStars().forEach(body -> body.drawSelf(g));
        getPlanets().forEach(body -> body.drawSelf(g));
        getMoons().forEach(body -> body.drawSelf(g));
        getAsteroids().forEach(body -> body.drawSelf(g));
    }
}
