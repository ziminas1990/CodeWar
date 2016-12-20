package ru.codewar.logicconveyor.baseconveyor;


import java.util.concurrent.CyclicBarrier;

public class Conveyor {

    private java.util.Vector<AbstractLogic> logicsChain = new java.util.Vector<>();
    private java.util.Vector<ConveyorThread> conveyorThreads = new java.util.Vector<>();
    private CyclicBarrier barrier;

    public Conveyor(int totalThreads)
    {
        barrier = new CyclicBarrier(totalThreads);
        for(int i = 0; i < totalThreads; i++) {
            conveyorThreads.add(new ConveyorThread(this, i, totalThreads));
        }
    }

    void proceed()
    {
        conveyorThreads.forEach(ConveyorThread::start);
        for(ConveyorThread thread : conveyorThreads) {
            try {
                thread.join(0);
            } catch (InterruptedException exp) {
                System.out.println("Thread #" + thread.getThreadId() + " failed! Details: " + exp);
            }
        }
    }

    public void addLogic(AbstractLogic logic) { logicsChain.add(logic); }

    public java.util.Vector<AbstractLogic> getLogicsChain() { return logicsChain; }

    public CyclicBarrier getBarrier() { return barrier; }

}
