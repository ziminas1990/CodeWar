package ru.codewar.logicconveyor.physicallogic;

import org.junit.Test;
import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.concept.MultithreadConveyor;

import static org.junit.Assert.assertEquals;

public class PhysicalLogicTests {

    double getOrbitRadius(double mass, double t)
    {
        return Math.pow(PhysicalLogic.G * mass * t * t / (4 * Math.PI * Math.PI), 1.0/3.0);
    }

    double getRequiredVelocity(double mass, double r)
    {
        return Math.pow(PhysicalLogic.G * mass / r, 0.5);
    }

    @Test
    public void simpleMultithreadTest() {

        int extraThreads = 3;
        int totalObjectsInCenter = 3;
        int totalOrbits = 2;
        int totalObjectsOnEachOrbit = 100;

        int ticksInSecond = 1000;

        MultithreadConveyor multithreadConveyor = new MultithreadConveyor(extraThreads);
        PhysicalLogic world = new PhysicalLogic();
        multithreadConveyor.addLogic(world);

        java.util.List<PhysicalObject> createdObjects = new java.util.ArrayList<>();

        double centralObjectsTotalMass = 1 / PhysicalLogic.G;

        for(int i = 0; i < totalObjectsInCenter; i++) {
            PhysicalObject centralObject = new PhysicalObjectImpl(centralObjectsTotalMass / totalObjectsInCenter, 1);
            world.registerObject(centralObject);
        }

        double t = 10;
        for(int objId = 1; objId <= totalOrbits; objId++, t *= 2) {
            double R = getOrbitRadius(centralObjectsTotalMass, t);
            double v = getRequiredVelocity(centralObjectsTotalMass, R) / ticksInSecond;
            for(int copies = 0; copies < totalObjectsOnEachOrbit; copies++) {
                PhysicalObject object = new PhysicalObjectImpl(1, R, new Point(R, 0), new Vector(0, v));
                world.registerObject(object);
                createdObjects.add(object);
            }
        }

        int totalTicks = (int)(t * ticksInSecond / 2);
        for(int i = 0; i < totalTicks; i++) {
            if(i % 1000 == 0)
                System.out.println("Tick #" + i + " / " + totalTicks);
            multithreadConveyor.proceed(ticksInSecond / 1000);
        }

        for(PhysicalObject object : createdObjects) {
            assertEquals(0, object.getPosition().getY(), 0.2);
        }
    }

}
