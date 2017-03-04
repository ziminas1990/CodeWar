package ru.codewar.logicconveyor;

import org.junit.Test;
import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.concept.MultithreadConveyor;
import ru.codewar.logicconveyor.physicallogic.PhysicalObject;
import ru.codewar.logicconveyor.physicallogic.PhysicalObjectImpl;
import ru.codewar.logicconveyor.physicallogic.PhysicalLogic;

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
        int totalObjectsInCenter = 5;
        int totalOrbits = 2;
        int totalObjectsOnEachOrbit = 200;

        MultithreadConveyor multithreadConveyor = new MultithreadConveyor(extraThreads);
        PhysicalLogic world = new PhysicalLogic();
        world.setSecondsInTick(0.001);
        multithreadConveyor.addLogic(world);

        java.util.List<PhysicalObject> createdObjects = new java.util.ArrayList<>();

        double centralObjectsTotalMass = 1 / PhysicalLogic.G;

        for(int i = 0; i < totalObjectsInCenter; i++) {
            PhysicalObject centralObject = new PhysicalObjectImpl(0, centralObjectsTotalMass / totalObjectsInCenter, 1);
            world.registerObject(centralObject);
        }

        double t = 10;
        for(int objId = 1; objId <= totalOrbits; objId++, t *= 2) {
            double R = getOrbitRadius(centralObjectsTotalMass, t);
            double v = getRequiredVelocity(centralObjectsTotalMass, R) / world.getTicksInSecond();
            for(int copies = 0; copies < totalObjectsOnEachOrbit; copies++) {
                PhysicalObject object = new PhysicalObjectImpl(objId, 1, R, new Point(R, 0), new Vector(0, v));
                world.registerObject(object);
                createdObjects.add(object);
            }
        }

        int totalTicks = (int)(t * world.getTicksInSecond() / 2);
        for(int i = 0; i < totalTicks; i++) {
            if(i % 1000 == 0)
                System.out.println("Tick #" + i + " / " + totalTicks);
            multithreadConveyor.proceed();
        }

        for(PhysicalObject object : createdObjects) {
            assertEquals(0, object.getPosition().getY(), 0.2);
        }
    }

}
