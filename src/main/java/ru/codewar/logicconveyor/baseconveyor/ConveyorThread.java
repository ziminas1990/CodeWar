package ru.codewar.logicconveyor.baseconveyor;


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
        for(AbstractLogic logic : conveyor.getLogicsChain()) {
            logic.proceed(threadId, totalThreads);
            try {
                conveyor.getBarrier().await();
            } catch (Exception exp) {
                System.out.println("Thread #" + threadId + ": awaiting failed! Details: " + exp);
            }
        }
    }

}
