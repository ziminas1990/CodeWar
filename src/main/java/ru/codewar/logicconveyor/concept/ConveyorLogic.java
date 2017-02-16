package ru.codewar.logicconveyor.concept;


public interface ConveyorLogic {

    public int stagesCount();
    public void prepareStage(int stageId);
    public void proceedStage(int stageId, int threadId, int totalThreads);

}
