package ru.codewar.logicconveyor.concept;

public class MultithreadConveyor {

    private java.util.List<ConveyorLogic> logicChain = new java.util.ArrayList<>();
    private MasterThreadLogic masterThreadLogic;
    private java.util.Vector<SlaveThread> slaveThreads;

    public MultithreadConveyor(int slaveThreadsCount)    {
        int totalThreads = slaveThreadsCount + 1;

        masterThreadLogic = new MasterThreadLogic(this, totalThreads);
        slaveThreads = new java.util.Vector<>(slaveThreadsCount);
        for(int i = 1; i < totalThreads; i++) {
            slaveThreads.add(new SlaveThread(masterThreadLogic.getContext(), i, totalThreads));
            slaveThreads.lastElement().start();
        }
    }

    /**
     * Proceed conveyor logic once
     * @param dt number of milliseconds passed after previous proceed() call
     * @return return number of nanoseconds, that took to proceed conveyor
     */
    public long proceed(long dt)
    {
        // We are not starting master thread logic in separate thread, but just running it
        // in current thread. Slave threads are waiting for master thread on barrier
        long start = System.nanoTime();
        masterThreadLogic.proceed(dt * 0.001);

        long nanosecProceeded = System.nanoTime() - start;
        if(nanosecProceeded > 50e6) {
            // Waiting while slave threads print their statistic
            try { Thread.sleep(1); } catch (Exception ex) {}
            System.out.println("proceedStage(): ~" + nanosecProceeded / 1000000 + " ms");
        }
        return nanosecProceeded;
    }

    public void addLogic(ConveyorLogic logic) {
        logicChain.add(logic);
    }

    public java.util.List<ConveyorLogic> getLogicChain() { return logicChain; }

}
