package ru.codewar.logicconveyor.concept;


public class ConveyorThread extends Thread {

    private Conveyor conveyor;
    private int threadId;
    private int totalThreads;

    ConveyorThread(Conveyor conveyor, int threadId, int totalThreads) {
        this.conveyor = conveyor;
        this.threadId = threadId;
        this.totalThreads = totalThreads;
    }

    int getThreadId() { return threadId; }

    @Override
    public void run() {
        while(true) {
            singleshotLogic();
        }
    }

    public void singleshotLogic() {
        // Proceeding all logics in conveyor chain once
        java.util.List<ConveyorLogic> logicChain = conveyor.getLogicsChain();
        for(int logicId = 0; logicId < logicChain.size(); logicId++) {
            try {
                conveyor.getBarrier().await();
            } catch (Exception exp) {
                System.out.println("Thread #" + threadId + ": awaiting failed! Details: " + exp);
            }
            logicChain.get(logicId).proceed(threadId, totalThreads);
        }
    }

}
