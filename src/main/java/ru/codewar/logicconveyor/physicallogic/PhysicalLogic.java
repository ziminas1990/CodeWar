package ru.codewar.logicconveyor.physicallogic;


import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.concept.ConveyorLogic;

import java.util.concurrent.atomic.AtomicInteger;

public class PhysicalLogic implements ConveyorLogic {
    public static final double G = 6.67408E-11;
    public static final double RevG = 1 / G;

    private double secondsInTick = 0.001;
    private java.util.ArrayList<PhysicalObject> objects = new java.util.ArrayList<>();
    private java.util.ArrayList<PhysicalObject> hugeObjects = new java.util.ArrayList<>();
    private AtomicInteger nextObjectId = new AtomicInteger(0);

    public void setSecondsInTick(double secondsInTick)
    {
        this.secondsInTick = secondsInTick;
    }

    public void registerObject(PhysicalObject object) {
        if(checkIfObjectIsHuge(object)) {
            hugeObjects.add(object);
        } else {
            objects.add(object);
        }
    }

    public double getSecondsInTick() { return secondsInTick; }
    public int getTicksInSecond() { return (int)(1 / secondsInTick); }

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
    public void proceedStage(int stageId, int threadId, int totalThreads) {
        if(stageId == 0) {
            calculateAccelerationsStage(threadId);
        } else if(stageId == 1) {
            movingStage(threadId);
        }
    }

    private void calculateAccelerationsStage(int threadId) {
        int totalObjects = objects.size();
        int totalHugeObjects = hugeObjects.size();
        Vector gravityForce = new Vector();
        int idx = nextObjectId.getAndIncrement();
        while(idx < totalObjects) {
            PhysicalObject object = objects.get(idx);
            for(int hugeObjectIdx = 0; hugeObjectIdx < totalHugeObjects; hugeObjectIdx++) {
                PhysicalObject hugeObject = hugeObjects.get(hugeObjectIdx);
                gravityForce.setPosition(object.getPosition(), hugeObject.getPosition());
                if(gravityForce.getLength() < 1)
                    continue;
                gravityForce.setLength(G * object.getMass() * hugeObject.getMass() / gravityForce.getSquaredLength());
                object.pushForce(gravityForce);
            }
            idx = nextObjectId.getAndIncrement();
        }
    }

    private void movingStage(int threadId) {
        int totalObjects = objects.size();
        Vector acceleration = new Vector();
        for(int idx = nextObjectId.getAndIncrement(); idx < totalObjects; idx = nextObjectId.getAndIncrement()) {
            PhysicalObject object = objects.get(idx);
            acceleration.setPosition(object.getForce(), secondsInTick * secondsInTick / object.getMass());
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
        return 0.01 < G * object.getMass() / (object.getSignature() * object.getSignature());
    }

}
