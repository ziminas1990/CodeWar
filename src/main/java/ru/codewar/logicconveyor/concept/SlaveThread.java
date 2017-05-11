package ru.codewar.logicconveyor.concept;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlaveThread extends Thread {

    private Logger logger = LoggerFactory.getLogger(SlaveThread.class);
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
                context.logic.proceedStage(context.stage, context.dt, threadId, totalThreads);
                context.barrier.await();
            }
        } catch (Exception exp) {
            logger.warn("Slave thread #" + threadId + ": awaiting failed! Details: ", exp);
            context.barrier.reset();
        }
    }
}
