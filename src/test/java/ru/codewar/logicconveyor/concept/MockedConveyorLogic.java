package ru.codewar.logicconveyor.concept;

public class MockedConveyorLogic implements ConveyorLogic {

    private java.util.Vector<Integer> totalProceeds;
    private int timeToProceed;

    MockedConveyorLogic(int totalThreads, int timeToProceed) {
        this.timeToProceed = timeToProceed;
        totalProceeds = new java.util.Vector<>(totalThreads);
        for(int i = 0; i < totalThreads; i++)
            totalProceeds.add(new Integer(0));
    }

    @Override
    public int stagesCount() { return 1; }

    @Override
    public boolean prepareStage(int stageId) { return true; }

    @Override
    public void proceedStage(int stageId, double dt, int threadId, int totalThreads) {
        totalProceeds.set(threadId, totalProceeds.get(threadId) + 1);
        if(timeToProceed > 0) {
            for(int i = threadId; i < timeToProceed; i += totalThreads) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException exp) {
                }
            }
        }
    }

    public int getTotalProceeds(int threadId) { return totalProceeds.get(threadId); }
}
