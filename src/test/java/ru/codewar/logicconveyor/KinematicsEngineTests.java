package ru.codewar.logicconveyor;

import org.junit.Test;
import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.concept.Conveyor;
import ru.codewar.logicconveyor.kinematicsengine.KinematickObject;
import ru.codewar.logicconveyor.kinematicsengine.KinematickObjectImpl;
import ru.codewar.logicconveyor.kinematicsengine.KinematickWorld;

import static org.junit.Assert.assertEquals;

public class KinematicsEngineTests {

    @Test
    public void simpleMultithreadTest() {

        int extraThreads = 3;
        int totalObjects = 1000000;
        int totalTicks = 5;

        Conveyor conveyor = new Conveyor(extraThreads);
        KinematickWorld world = new KinematickWorld();
        conveyor.addLogic(world);

        java.util.List<KinematickObject> createdObjects = new java.util.ArrayList<>();

        // ObjectId value is the same as object start position coordinates and velocity vector coordinates
        // This fact will be useful, when checking objects position
        for(int i = 0; i < totalObjects; i++) {
            KinematickObject object = new KinematickObjectImpl(i, 1, new Point(i, i), new Vector(i, i));
            world.registerObject(object);
            object.getAcceleration().setPosition(i, i);
            createdObjects.add(object);
        }

        for(int i = 0; i < totalTicks; i++) {
            conveyor.proceed();
        }

        for(KinematickObject object : createdObjects) {
            double vt = object.getObjectId() * totalTicks;
            double at2 = object.getObjectId() * totalTicks * totalTicks;
            assertEquals(object.getObjectId() + vt + at2/2, object.getPosition().getX(), 1);
            assertEquals(object.getObjectId() + vt + at2/2, object.getPosition().getY(), 1);
        }

    }

}
