package ru.codewar.logicconveyor.concept;


public class SlaveThread extends Thread {

    private ConveyorThreadContext context;
    private int threadId;
    private int totalThreads;

    SlaveThread(ConveyorThreadContext context, int threadId, int totalThreads) {
        this.context = context;
        this.threadId = threadId;
        this.totalThreads = totalThreads;
    }

    int getThreadId() { return threadId; }

    @Override
    public void run() {
        try {
            while (true) {
                context.barrier.await();
                context.logic.proceedStage(context.stage, threadId, totalThreads);
                context.barrier.await();
            }
        } catch (Exception exp) {
            System.out.println("Slave thread #" + threadId + ": awaiting failed! Details: " + exp);
            context.barrier.reset();
        }
    }
}
