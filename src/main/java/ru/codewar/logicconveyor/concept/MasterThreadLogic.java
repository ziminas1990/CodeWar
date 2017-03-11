package ru.codewar.logicconveyor.concept;

import java.util.concurrent.CyclicBarrier;

public class MasterThreadLogic {
    private MultithreadConveyor conveyor;
    private ConveyorThreadContext context = new ConveyorThreadContext();
    private int totalThreads;

    public MasterThreadLogic(MultithreadConveyor conveyor, int totalThreads) {
        this.conveyor = conveyor;
        this.totalThreads = totalThreads;
        context.barrier = new CyclicBarrier(totalThreads);
    }

    public ConveyorThreadContext getContext() { return context; }
    public int getThreadId() { return 0; }

    public void proceed() {
        // Proceeding all logic in conveyor chain once
        java.util.List<ConveyorLogic> logicChain = conveyor.getLogicChain();
        try {
            for(int logicId = 0; logicId < logicChain.size(); logicId++) {
                context.logic = logicChain.get(logicId);
                for(context.stage = 0; context.stage < context.logic.stagesCount(); context.stage++) {
                    if(context.logic.prepareStage(context.stage)) {
                        context.barrier.await();
                        context.logic.proceedStage(context.stage, 0, totalThreads);
                        // Waiting slave threads on barrier, before start next stage
                        context.barrier.await();
                    }
                }
            }
        } catch (Exception exp) {
            System.out.println("Master thread: awaiting failed! Details: " + exp);
        }
    }
}
