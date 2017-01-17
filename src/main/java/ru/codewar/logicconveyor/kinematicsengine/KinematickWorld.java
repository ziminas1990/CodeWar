package ru.codewar.logicconveyor.kinematicsengine;


import ru.codewar.logicconveyor.concept.ConveyorLogic;

import java.util.HashMap;

public class KinematickWorld implements ConveyorLogic {
    private java.util.ArrayList<KinematickObject> objects = new java.util.ArrayList<>();
    private HashMap<Integer, KinematickObject> objectsMap = new HashMap<>();

    public void registerObject(KinematickObject object) {
        if(objectsMap.containsKey(object.getObjectId()))
            return;
        objects.add(object);
        objectsMap.put(object.getObjectId(), object);
    }

    @Override
    public void proceed(int threadId, int totalThreads) {
        int totalObjects = objects.size();
        for(int idx = threadId; idx < totalObjects; idx += totalThreads) {
            KinematickObject object = objects.get(idx);
            object.getPosition().move(object.getVelocity());
        }
    }

}
