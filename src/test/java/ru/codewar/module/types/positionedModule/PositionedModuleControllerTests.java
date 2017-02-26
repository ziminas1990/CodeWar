package ru.codewar.module.types.positionedModule;

import org.junit.Test;
import ru.codewar.geometry.Point;
import ru.codewar.networking.Message;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Александр on 13.12.2016.
 */
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
        verify(mockedModule, times(2)).getPosition();
    }

}
