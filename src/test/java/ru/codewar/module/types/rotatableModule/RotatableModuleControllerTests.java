package ru.codewar.module.types.rotatableModule;

import org.junit.Test;
import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;
import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Александр on 15.12.2016.
 */
public class RotatableModuleControllerTests {

    @Test
    public void checkIfSupportedTest() {
        assertTrue(RotatableModuleController.checkIfSupported("getMaxRotateSpeed"));
        assertTrue(RotatableModuleController.checkIfSupported("rotate 232 323"));
        assertTrue(RotatableModuleController.checkIfSupported("orient"));
    }

    @Test
    public void orientationRequestTest() {
        RotatableModuleType mockedModule = mock(RotatableModuleType.class);

        RotatableModuleController controller = new RotatableModuleController();
        controller.attachToModule(mockedModule);

        Vector orientation = new Vector(4, 2);
        when(mockedModule.getOrientation()).thenReturn(orientation);
        assertEquals(
                controller.onRequest(1, "orient"),
                new Message(orientation.getNormilizedX() + " " + orientation.getNormilizedY()));
        verify(mockedModule).getOrientation();
    }

    @Test
    public void getMaxSpeedTest() {
        RotatableModuleType mockedModule = mock(RotatableModuleType.class);

        RotatableModuleController controller = new RotatableModuleController();
        controller.attachToModule(mockedModule);

        double maxSpeed = 20;
        when(mockedModule.getMaxRotationSpeed()).thenReturn(maxSpeed);
        assertEquals(
                controller.onRequest(1, "getMaxRotateSpeed"),
                new Message(String.valueOf(maxSpeed)));
        verify(mockedModule).getMaxRotationSpeed();
    }

    @Test
    public void rotateCommandTest() {
        RotatableModuleType mockedModule = mock(RotatableModuleType.class);

        RotatableModuleController controller = new RotatableModuleController();
        controller.attachToModule(mockedModule);

        controller.onCommand("rotate -3.1 0.1");
        verify(mockedModule).rotate(Double.parseDouble("-3.1"), Double.parseDouble("0.1"));
    }

    @Test
    public void rotateWithMaxSpeedCommandTest() {
        RotatableModuleType mockedModule = mock(RotatableModuleType.class);

        RotatableModuleController controller = new RotatableModuleController();
        controller.attachToModule(mockedModule);

        double maxSpeed = 20;
        when(mockedModule.getMaxRotationSpeed()).thenReturn(maxSpeed);

        controller.onCommand("rotate -3.1");
        verify(mockedModule).getMaxRotationSpeed();
        verify(mockedModule).rotate(Double.parseDouble("-3.1"), maxSpeed);
    }

    // Useful to check, that other controller aggregates positioned module controller correctly
    // controller - instance of controller, that use PositionedModuleController
    // mockedModule - mock instance of PositionedModule, to which controller is connected
    public static void inheritanceChecker(ModuleController controller, RotatableModuleType mockedModule) {
        Point position = new Point(4, 2);

        Vector orientation = new Vector(4, 2);
        double maxSpeed = 20;
        when(mockedModule.getOrientation()).thenReturn(orientation);
        when(mockedModule.getMaxRotationSpeed()).thenReturn(maxSpeed);

        assertEquals(
                controller.onRequest(1, "orient"),
                new Message(orientation.getNormilizedX() + " " + orientation.getNormilizedY()));
        verify(mockedModule).getOrientation();

        assertEquals(
                controller.onRequest(1, "getMaxRotateSpeed"),
                new Message(String.valueOf(maxSpeed)));
        verify(mockedModule).getMaxRotationSpeed();

        controller.onCommand("rotate -3.1 0.1");
        verify(mockedModule).rotate(Double.parseDouble("-3.1"), Double.parseDouble("0.1"));
    }
}
