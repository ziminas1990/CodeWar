package ru.codewar.module.types.rotatableModule;

import org.junit.Test;
import ru.codewar.geometry.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Александр on 15.12.2016.
 */
public class RotatableModuleTests {

    @Test
    public void checkIfSupportedTest() {
        assertTrue(RotatableModuleControllerImpl.checkIfSupported("getMaxSpeed"));
        assertTrue(RotatableModuleControllerImpl.checkIfSupported("rotate 232 323"));
        assertTrue(RotatableModuleControllerImpl.checkIfSupported("orient"));
    }

    @Test
    public void orientationRequestTest() {
        RotatableModuleType mockedModule = mock(RotatableModuleType.class);

        RotatableModuleControllerImpl controller = new RotatableModuleControllerImpl();
        controller.attachToModule(mockedModule);

        Vector orientation = new Vector(4, 2);
        when(mockedModule.getOrientation()).thenReturn(orientation);
        assertEquals(controller.onRequest(1, "orient"),
                orientation.getNormilizedX() + " " + orientation.getNormilizedY());
        verify(mockedModule, times(2)).getOrientation();
    }

    @Test
    public void getMaxSpeedTest() {
        RotatableModuleType mockedModule = mock(RotatableModuleType.class);

        RotatableModuleControllerImpl controller = new RotatableModuleControllerImpl();
        controller.attachToModule(mockedModule);

        double maxSpeed = 20;
        when(mockedModule.getMaxRotationSpeed()).thenReturn(maxSpeed);
        assertEquals(controller.onRequest(1, "getMaxSpeed"), String.valueOf(maxSpeed));
        verify(mockedModule).getMaxRotationSpeed();
    }

    @Test
    public void rotateCommandTest() {
        RotatableModuleType mockedModule = mock(RotatableModuleType.class);

        RotatableModuleControllerImpl controller = new RotatableModuleControllerImpl();
        controller.attachToModule(mockedModule);

        controller.onCommand("rotate -3.1 0.1");
        verify(mockedModule).rotate(Double.parseDouble("-3.1"), Double.parseDouble("0.1"));
    }

    @Test
    public void rotateWithMaxSpeedCommandTest() {
        RotatableModuleType mockedModule = mock(RotatableModuleType.class);

        RotatableModuleControllerImpl controller = new RotatableModuleControllerImpl();
        controller.attachToModule(mockedModule);

        double maxSpeed = 20;
        when(mockedModule.getMaxRotationSpeed()).thenReturn(maxSpeed);

        controller.onCommand("rotate -3.1");
        verify(mockedModule).getMaxRotationSpeed();
        verify(mockedModule).rotate(Double.parseDouble("-3.1"), maxSpeed);
    }
}
