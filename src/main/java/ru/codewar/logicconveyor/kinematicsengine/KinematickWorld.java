package ru.codewar.logicconveyor.kinematicsengine;


import ru.codewar.logicconveyor.concept.ConveyorLogic;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class KinematickWorld implements ConveyorLogic {
    private java.util.ArrayList<KinematickObject> objects = new java.util.ArrayList<>();
    private HashMap<Integer, KinematickObject> objectsMap = new HashMap<>();
    private AtomicInteger index = new AtomicInteger(0);

    public void registerObject(KinematickObject object) {
        if(objectsMap.containsKey(object.getObjectId()))
            return;
        objects.add(object);
        objectsMap.put(object.getObjectId(), object);
    }

    @Override
    public void prephare()
    {
        index.set(0);
    }

    @Override
    public void proceed(int threadId, int totalThreads) {
        int totalObjects = objects.size();
        for(int idx = index.getAndAdd(1); idx < totalObjects; idx = index.getAndAdd(1)) {
            KinematickObject object = objects.get(idx);
            object.getPosition().move(object.getVelocity());
        }
    }

}
