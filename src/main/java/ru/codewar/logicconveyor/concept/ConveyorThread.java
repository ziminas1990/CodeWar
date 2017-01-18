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
        // Waiting other threads on barrier before first logic
        try {
            conveyor.getBarrier().await();
        } catch (Exception exp) {
            System.out.println("Thread #" + threadId + ": awaiting failed! Details: " + exp);
        }
        // Proceeding all logic in conveyor chain once
        long start = System.currentTimeMillis();
        java.util.List<ConveyorLogic> logicChain = conveyor.getLogicChain();
        for(int logicId = 0; logicId < logicChain.size(); logicId++) {
            logicChain.get(logicId).proceed(threadId, totalThreads);
            // Waiting other threads on barrier after each logic
            try {
                conveyor.getBarrier().await();
            } catch (Exception exp) {
                System.out.println("Thread #" + threadId + ": awaiting failed! Details: " + exp);
            }
        }

        // If proceeding is slow, printing statistic after every proceed
        long proceedTime = System.currentTimeMillis() - start;
        if(proceedTime > 50)
            System.out.print(proceedTime + "\t");
    }

}
