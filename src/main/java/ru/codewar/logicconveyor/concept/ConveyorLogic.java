package ru.codewar.logicconveyor.concept;


public interface ConveyorLogic {

    int stagesCount();
    boolean prepareStage(int stageId);
    void proceedStage(int stageId, int threadId, int totalThreads);

}
