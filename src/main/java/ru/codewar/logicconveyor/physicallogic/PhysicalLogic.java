package ru.codewar.logicconveyor.physicallogic;


import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.concept.ConveyorLogic;

import java.util.concurrent.atomic.AtomicInteger;

public class PhysicalLogic implements ConveyorLogic {
    public static final double G = 6.67408E-11;

    private java.util.ArrayList<PhysicalObject> objects = new java.util.ArrayList<>();
    private java.util.ArrayList<PhysicalObject> hugeObjects = new java.util.ArrayList<>();
    private java.util.HashSet<PhysicalObject> allObjectsSet = new java.util.HashSet<>();

    private AtomicInteger nextObjectId = new AtomicInteger(0);

    public synchronized void registerObject(PhysicalObject object) {
        if(allObjectsSet.contains(object))
            return;
        objects.add(object);
        allObjectsSet.add(object);
        if(checkIfObjectIsHuge(object)) {
            hugeObjects.add(object);
        }
    }
    public synchronized void deregisterObject(PhysicalObject object) {
        if(!allObjectsSet.contains(object))
            return;
        allObjectsSet.remove(object);
        objects.remove(object);
        if(checkIfObjectIsHuge(object)) {
            hugeObjects.remove(object);
        }
    }

    @Override
    public int stagesCount() {
        // Two stages: calculating accelerations and moving objects
        return 2;
    }

    @Override
    public boolean prepareStage(int stageId)
    {
        nextObjectId.set(0);
        return objects.size() > 0;
    }

    @Override
    public void proceedStage(int stageId, double dt, int threadId, int totalThreads) {
        switch (stageId) {
            case 0:
                calculateAccelerationsStage();
                return;
            case 1:
                movingStage(dt);
        }
    }

    private void calculateAccelerationsStage() {
        int totalObjects = objects.size();
        int totalHugeObjects = hugeObjects.size();
        Vector gravityForce = new Vector();
        int idx = nextObjectId.getAndIncrement();

        // for optimization reasons
        double hugeObjectsSqrSignatures[] = new double[hugeObjects.size()];
        for(int hugeObjectIdx = 0; hugeObjectIdx < totalHugeObjects; hugeObjectIdx++) {
            hugeObjectsSqrSignatures[hugeObjectIdx] = Math.pow(hugeObjects.get(hugeObjectIdx).getSignature(), 2);
        }

        while(idx < totalObjects) {
            PhysicalObject object = objects.get(idx);
            for(int hugeObjectIdx = 0; hugeObjectIdx < totalHugeObjects; hugeObjectIdx++) {
                PhysicalObject hugeObject = hugeObjects.get(hugeObjectIdx);
                gravityForce.setPosition(object.getPosition(), hugeObject.getPosition());
                double sqrDistance = gravityForce.getSquaredLength();
                // if small objects "inside" huge objects, we assume that it on surface of huge object
                if(sqrDistance < hugeObjectsSqrSignatures[hugeObjectIdx]) {
                    if(sqrDistance < 1)
                        continue;
                    sqrDistance = hugeObjectsSqrSignatures[hugeObjectIdx];
                }
                gravityForce.setLength(G * object.getMass() * hugeObject.getMass() / sqrDistance);
                object.pushForce(gravityForce);
            }
            idx = nextObjectId.getAndIncrement();
        }
    }

    private void movingStage(double dt) {
        int totalObjects = objects.size();
        Vector acceleration = new Vector();
        for(int idx = nextObjectId.getAndIncrement(); idx < totalObjects; idx = nextObjectId.getAndIncrement()) {
            PhysicalObject object = objects.get(idx);
            acceleration.setPosition(object.getForce(), dt * dt / object.getMass());
            object.getPosition().move(
                    object.getVelocity().getX() + acceleration.getX() / 2,
                    object.getVelocity().getY() + acceleration.getY() / 2);
            object.getVelocity().move(acceleration.getX() , acceleration.getY());
            object.getForce().reset();
        }
    }

    private boolean checkIfObjectIsHuge(PhysicalObject object)
    {
        // The object is huge, if acceleration of gravity on its surface is more than 0.01 m/s^2
        return 0.1 < G * object.getMass() / (object.getSignature() * object.getSignature());
    }

}
