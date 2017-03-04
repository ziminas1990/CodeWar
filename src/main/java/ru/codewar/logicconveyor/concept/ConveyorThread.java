package ru.codewar.logicconveyor.concept;


public class ConveyorThread extends Thread {

    private MultithreadConveyor conveyor;
    private int threadId;
    private int totalThreads;

    ConveyorThread(MultithreadConveyor conveyor, int threadId, int totalThreads) {
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

    public void waitOtherThreads()
    {
        try {
            conveyor.getBarrier().await();
        } catch (Exception exp) {
            System.out.println("Thread #" + threadId + ": awaiting failed! Details: " + exp);
        }
    }

    public void singleshotLogic() {
        // Proceeding all logic in conveyor chain once
        java.util.List<ConveyorLogic> logicChain = conveyor.getLogicChain();
        for(int logicId = 0; logicId < logicChain.size(); logicId++) {
            ConveyorLogic logic = logicChain.get(logicId);
            waitOtherThreads();
            for(int stage = 0; stage < logic.stagesCount(); stage++) {
                if(threadId == 0) {
                    // Master-thread prepare logic to next stage
                    logic.prepareStage(stage);
                }
                // Waiting other threads on barrier before run logic
                waitOtherThreads();
                logic.proceedStage(stage, threadId, totalThreads);
                waitOtherThreads();
            }
        }
    }

}
