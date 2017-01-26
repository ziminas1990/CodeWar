package ru.codewar.module.engine;

import org.junit.Test;
import ru.codewar.geometry.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


public class EngineControllerTests {

    @Test
    public void checkIfSupportedTest() {
        // Check interface, that was inherited from RotatableModuleType
        assertTrue(EngineController.checkIfSupported("getMaxRotateSpeed"));
        assertTrue(EngineController.checkIfSupported("rotate 10 1"));
        assertTrue(EngineController.checkIfSupported("orient"));
        // check interface, that specific for engine
        assertTrue(EngineController.checkIfSupported("getMaxThrust"));
        assertTrue(EngineController.checkIfSupported("getCurrentThrust"));
        assertTrue(EngineController.checkIfSupported("setThrust 333"));
    }

    @Test
    public void rotatableModuleInheritedTypeTests() {
        // Check, that interface, inherited from RotatableModuleType, work properly
        Engine mockedModule = mock(Engine.class);
        EngineController controller = new EngineController();
        controller.attachToEngine(mockedModule);

        Vector orientation = new Vector(4, 2);
        when(mockedModule.getOrientation()).thenReturn(orientation);
        assertEquals(controller.onRequest(1, "orient"),
                orientation.getNormilizedX() + " " + orientation.getNormilizedY());
        verify(mockedModule, times(2)).getOrientation();

        double maxSpeed = 20;
        when(mockedModule.getMaxRotationSpeed()).thenReturn(maxSpeed);
        assertEquals(controller.onRequest(1, "getMaxRotateSpeed"), String.valueOf(maxSpeed));
        verify(mockedModule).getMaxRotationSpeed();

        controller.onCommand("rotate -3.1 0.1");
        verify(mockedModule).rotate(Double.parseDouble("-3.1"), Double.parseDouble("0.1"));

        when(mockedModule.getMaxRotationSpeed()).thenReturn(maxSpeed);
        controller.onCommand("rotate -3.1");
        verify(mockedModule, times(2)).getMaxRotationSpeed();
        verify(mockedModule).rotate(Double.parseDouble("-3.1"), maxSpeed);
    }

    @Test
    public void getMaxAndCurrentThrust() {
        Engine mockedModule = mock(Engine.class);
        EngineController controller = new EngineController();
        controller.attachToEngine(mockedModule);

        double maxThrust = 10000;
        when(mockedModule.getMaxThrust()).thenReturn(maxThrust);
        assertEquals(controller.onRequest(1, "getMaxThrust"), String.valueOf(maxThrust));
        verify(mockedModule).getMaxThrust();

        double currentThrust = 5000;
        when(mockedModule.getCurrentThrust()).thenReturn(currentThrust);
        assertEquals(controller.onRequest(1, "getCurrentThrust"), String.valueOf(currentThrust));
        verify(mockedModule).getCurrentThrust();

    }

    @Test
    public void setThrust() {
        Engine mockedModule = mock(Engine.class);
        EngineController controller = new EngineController();
        controller.attachToEngine(mockedModule);

        double maxThrust = 10000;
        when(mockedModule.getMaxThrust()).thenReturn(maxThrust);

        double newCurrentThrust = 7000;
        controller.onCommand("setThrust " + String.valueOf(newCurrentThrust));
        verify(mockedModule).getMaxThrust();
        verify(mockedModule).setThrust(newCurrentThrust);

        double newExcessThrust = 12000;
        controller.onCommand("setThrust " + String.valueOf(newExcessThrust));
        verify(mockedModule, times(2)).getMaxThrust();
        verify(mockedModule).setThrust(maxThrust);
    }
}
