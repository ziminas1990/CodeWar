package ru.codewar.module.types.positionedModule;

import org.junit.Test;
import ru.codewar.geometry.Point;
import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleController;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PositionedModuleControllerTests {

    @Test
    public void checkIfSupportedTest() {
        assertTrue(PositionedModuleController.checkIfSupported("position"));
        assertTrue(PositionedModuleController.checkIfSupported("position "));

        assertFalse(PositionedModuleController.checkIfSupported("positio"));
    }

    @Test
    public void moduleNotAttachedTest() {
        PositionedModuleController controller = new PositionedModuleController();
        assertEquals(controller.onRequest(1, "position"), new Message("fail: controller wasn't attached to module!"));
        assertEquals(controller.onRequest(1, "orient"), new Message("fail: controller wasn't attached to module!"));
    }

    @Test
    public void incorrectRequestTest() {
        PositionedModuleType mockedModule = mock(PositionedModuleType.class);

        PositionedModuleController controller = new PositionedModuleController();
        controller.attachToModule(mockedModule);

        assertEquals(controller.onRequest(1, "positio"), new Message("fail: incorrect request"));
    }

    @Test
    public void positionRequestTest() {
        PositionedModuleType mockedModule = mock(PositionedModuleType.class);

        PositionedModuleController controller = new PositionedModuleController();
        controller.attachToModule(mockedModule);

        Point position = new Point(4, 2);
        when(mockedModule.getPosition()).thenReturn(position);
        assertEquals(
                controller.onRequest(1, "position"),
                new Message(position.getX() + " " + position.getY()));
        verify(mockedModule, times(1)).getPosition();
    }

    // Useful to check, that other controller aggregates positioned module controller correctly
    // controller - instance of controller, that use PositionedModuleController
    // mockedModule - mock instance of PositionedModule, to which controller is connected
    public static void inheritanceChecker(ModuleController controller, PositionedModuleType mockedModule) {
        Point position = new Point(4, 2);
        when(mockedModule.getPosition()).thenReturn(position);
        assertEquals(
                controller.onRequest(1, "position"),
                new Message(position.getX() + " " + position.getY()));
        verify(mockedModule, times(1)).getPosition();
    }

}
