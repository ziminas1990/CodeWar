package ru.codewar.logicconveyor.concept;

import java.util.concurrent.CyclicBarrier;

public class Conveyor {

    private java.util.List<ConveyorLogic> logicsChain = new java.util.ArrayList<>();
    private ConveyorThread masterThreadLogic;
    private java.util.Vector<ConveyorThread> extraThreads;
    private CyclicBarrier barrier;

    public Conveyor(int extraThreadsCount)    {
        int totalThreads = extraThreadsCount + 1;
        barrier = new CyclicBarrier(totalThreads);

        masterThreadLogic = new ConveyorThread(this, 0, totalThreads);
        extraThreads = new java.util.Vector<>(extraThreadsCount);
        for(int i = 1; i < totalThreads; i++) {
            ConveyorThread thread = new ConveyorThread(this, i, totalThreads);
            extraThreads.add(thread);
            thread.start();
        }
    }

    public void proceed()
    {
        // We are not starting master thread logic in separate thread, but just running it
        // in current thread. Extra threads are waiting for master thread on barrier
        masterThreadLogic.singleshotLogic();
    }

    public void addLogic(ConveyorLogic logic) {
        logicsChain.add(logic);
    }

    public java.util.List<ConveyorLogic> getLogicsChain() { return logicsChain; }

    public CyclicBarrier getBarrier() { return barrier; }

}
