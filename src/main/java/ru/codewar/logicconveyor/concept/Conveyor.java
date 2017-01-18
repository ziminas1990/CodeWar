package ru.codewar.logicconveyor.concept;

import java.util.concurrent.CyclicBarrier;

public class Conveyor {

    private java.util.List<ConveyorLogic> logicChain = new java.util.ArrayList<>();
    private ConveyorThread masterThreadLogic;
    private java.util.Vector<ConveyorThread> slaveThreads;
    private CyclicBarrier barrier;

    public Conveyor(int slaveThreadsCount)    {
        int totalThreads = slaveThreadsCount + 1;
        barrier = new CyclicBarrier(totalThreads);

        masterThreadLogic = new ConveyorThread(this, 0, totalThreads);
        slaveThreads = new java.util.Vector<>(slaveThreadsCount);
        for(int i = 1; i < totalThreads; i++) {
            ConveyorThread thread = new ConveyorThread(this, i, totalThreads);
            slaveThreads.add(thread);
            thread.start();
        }
    }

    public void proceed()
    {
        // We are not starting master thread logic in separate thread, but just running it
        // in current thread. Slave threads are waiting for master thread on barrier
        long start = System.currentTimeMillis();
        for(ConveyorLogic logic : logicChain)
            logic.prephare();
        masterThreadLogic.singleshotLogic();

        // If proceeding is slow, printing statistic after every proceed
        long proceedTime = System.currentTimeMillis() - start;
        if(proceedTime > 50) {
            // Waiting while slave threads print their statistic
            try { Thread.sleep(1); } catch (Exception ex) {}
            System.out.println("\t | total " + proceedTime + " ms");
        }
    }

    public void addLogic(ConveyorLogic logic) {
        logicChain.add(logic);
    }

    public java.util.List<ConveyorLogic> getLogicChain() { return logicChain; }

    public CyclicBarrier getBarrier() { return barrier; }

}
