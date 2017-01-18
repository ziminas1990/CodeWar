package ru.codewar.logicconveyor.concept;


public interface ConveyorLogic {

    public void prephare();
    public void proceed(int threadId, int totalThreads);

}
