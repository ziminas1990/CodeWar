package ru.codewar.module.ship;

import org.junit.Test;
import ru.codewar.module.types.positionedModule.PositionedModuleControllerTests;
import ru.codewar.module.types.rotatableModule.RotatableModuleControllerTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ShipControllerTests {

    @Test
    public void checkControllerInheritances() {
        ShipModule mockedShip = mock(ShipModule.class);
        ShipController controller = new ShipController();
        controller.attachToModule(mockedShip);

        PositionedModuleControllerTests.inheritanceChecker(controller, mockedShip);
        RotatableModuleControllerTests.inheritanceChecker(controller, mockedShip);
    }

}
