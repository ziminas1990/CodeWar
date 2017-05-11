package ru.codewar.logicconveyor.concept;


public interface ConveyorLogic {

    int stagesCount();

    boolean prepareStage(int stageId);

    /**
     * Called to proceed logic
     * @param stageId serial number of stage (0 <= stageId < stagedCount)
     * @param dt in-game time passed after previous call (in seconds)
     * @param threadId id of thread (0 <= threadId < totalThreads)
     * @param totalThreads total number of threads, that proceeds this logic
     */
    void proceedStage(int stageId, double dt, int threadId, int totalThreads);

}
