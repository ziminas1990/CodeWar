package ru.codewar.module.positionedModule;

import org.junit.Test;
import ru.codewar.geometry.Point;
import ru.codewar.geometry.Vector;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Александр on 13.12.2016.
 */
public class PositionedModuleControllerTests {

    @Test
    public void checkIfSupportedTest() {
        assertTrue(PositionedModuleControllerImpl.checkIfSupported("position"));
        assertTrue(PositionedModuleControllerImpl.checkIfSupported("orient"));
        assertTrue(PositionedModuleControllerImpl.checkIfSupported("position "));
        assertTrue(PositionedModuleControllerImpl.checkIfSupported("orient "));

        assertFalse(PositionedModuleControllerImpl.checkIfSupported("positio"));
        assertFalse(PositionedModuleControllerImpl.checkIfSupported("orint"));
    }

    @Test
    public void moduleNotAttachedTest() {
        PositionedModuleControllerImpl controller = new PositionedModuleControllerImpl();
        assertEquals(controller.onRequest(1, "position"), "fail: controller wasn't attached to module!");
        assertEquals(controller.onRequest(1, "orient"), "fail: controller wasn't attached to module!");
    }

    @Test
    public void incorrectRequestTest() {
        PositionedModule mockedModule = mock(PositionedModule.class);

        PositionedModuleControllerImpl controller = new PositionedModuleControllerImpl();
        controller.attachToModule(mockedModule);

        assertEquals(controller.onRequest(1, "positio"), "fail: incorrect request");
    }

    @Test
    public void positionRequestTest() {
        PositionedModule mockedModule = mock(PositionedModule.class);

        PositionedModuleControllerImpl controller = new PositionedModuleControllerImpl();
        controller.attachToModule(mockedModule);

        Point position = new Point(4, 2);
        when(mockedModule.getPosition()).thenReturn(position);
        assertEquals(controller.onRequest(1, "position"),
                position.getX() + " " + position.getY());
        verify(mockedModule, times(2)).getPosition();
    }

    @Test
    public void orientationRequestTest() {
        PositionedModule mockedModule = mock(PositionedModule.class);

        PositionedModuleControllerImpl controller = new PositionedModuleControllerImpl();
        controller.attachToModule(mockedModule);

        Vector orientation = new Vector(4, 2);
        when(mockedModule.getOrientation()).thenReturn(orientation);
        assertEquals(controller.onRequest(1, "orient"),
                orientation.getNormilizedX() + " " + orientation.getNormilizedY());
        verify(mockedModule, times(2)).getOrientation();
    }
}
