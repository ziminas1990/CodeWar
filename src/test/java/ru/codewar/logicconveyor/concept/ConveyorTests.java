package ru.codewar.logicconveyor.concept;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConveyorTests {
    @Test
    public void simpleMultiThreadTest() {
        int extraThreads  = 50;
        int totalProceeds = 100;
        int totalThreads  = extraThreads + 1;
        Conveyor conveyor = new Conveyor(extraThreads);

        MockedConveyorLogic mockedLogic = new MockedConveyorLogic(totalThreads, 100);
        conveyor.addLogic(mockedLogic);

        for(int i = 0; i < totalProceeds; i++) {
            conveyor.proceed();
        }

        for(int i = 0; i < totalThreads; i++) {
            assertEquals(totalProceeds, mockedLogic.getTotalProceeds(i));
        }
    }
}
