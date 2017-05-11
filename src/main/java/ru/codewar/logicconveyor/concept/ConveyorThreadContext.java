package ru.codewar.logicconveyor.concept;

import java.util.concurrent.CyclicBarrier;

public class ConveyorThreadContext {
    public CyclicBarrier barrier;
    public ConveyorLogic logic;
    public int stage;
    public double dt;

}
