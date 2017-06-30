package ru.codewar.visualizer;

import ru.codewar.logicconveyor.concept.ConveyorLogic;
import ru.codewar.world.IWorld;

import java.util.concurrent.atomic.AtomicInteger;

public class VisualizerLogic implements ConveyorLogic {

    private IWorld world;
    private IObserver observer;
    private AtomicInteger usersCounter = new AtomicInteger(0);
    private double dt = 0;

    public VisualizerLogic(IWorld world) {
        this.world = world;
    }

    public void attachToObserver(IObserver observer) {
        this.observer = observer;
    }

    @Override
    public int stagesCount() {
        return 1;
    }

    @Override
    public boolean prepareStage(int stageId) {
        int users = usersCounter.incrementAndGet();
        if(users > 1)
            usersCounter.decrementAndGet();
        return users == 1;
    }

    @Override
    public void proceedStage(int stageId, double dt, int threadId, int totalThreads) {
        if(threadId > 0)
            // only master-thread draws the world
            return;
        this.dt += dt;
        if(this.dt > 0.1) {
            this.dt = 0;
            world.drawSelf(observer.getWhereToPain());
            observer.paintHasBeenUpdated();
        }
        usersCounter.decrementAndGet();
    }
}
