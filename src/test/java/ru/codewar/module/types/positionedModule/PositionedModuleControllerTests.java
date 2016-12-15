package ru.codewar.module.types.positionedModule;

import org.junit.Test;
import ru.codewar.geometry.Point;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Александр on 13.12.2016.
 */
public class PositionedModuleControllerTests {

    @Test
    public void checkIfSupportedTest() {
        assertTrue(PositionedModuleControllerImpl.checkIfSupported("position"));
        assertTrue(PositionedModuleControllerImpl.checkIfSupported("position "));

        assertFalse(PositionedModuleControllerImpl.checkIfSupported("positio"));
    }

    @Test
    public void moduleNotAttachedTest() {
        PositionedModuleControllerImpl controller = new PositionedModuleControllerImpl();
        assertEquals(controller.onRequest(1, "position"), "fail: controller wasn't attached to module!");
        assertEquals(controller.onRequest(1, "orient"), "fail: controller wasn't attached to module!");
    }

    @Test
    public void incorrectRequestTest() {
        PositionedModuleType mockedModule = mock(PositionedModuleType.class);

        PositionedModuleControllerImpl controller = new PositionedModuleControllerImpl();
        controller.attachToModule(mockedModule);

        assertEquals(controller.onRequest(1, "positio"), "fail: incorrect request");
    }

    @Test
    public void positionRequestTest() {
        PositionedModuleType mockedModule = mock(PositionedModuleType.class);

        PositionedModuleControllerImpl controller = new PositionedModuleControllerImpl();
        controller.attachToModule(mockedModule);

        Point position = new Point(4, 2);
        when(mockedModule.getPosition()).thenReturn(position);
        assertEquals(controller.onRequest(1, "position"),
                position.getX() + " " + position.getY());
        verify(mockedModule, times(2)).getPosition();
    }

}
