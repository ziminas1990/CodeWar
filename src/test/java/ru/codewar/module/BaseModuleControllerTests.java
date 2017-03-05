package ru.codewar.module;

import org.junit.Test;
import ru.codewar.networking.Message;
import ru.codewar.protocol.module.ModuleController;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class BaseModuleControllerTests {

    @Test
    public void checkIfSupportedTest() {
        assertTrue(BaseModuleController.checkIfSupported("getModuleInfo "));
    }

    @Test
    public void getModuleInfoTest() {
        BaseModuleInterface mockedModule = mock(BaseModuleInterface.class);
        BaseModuleController controller = new BaseModuleController();
        controller.attachToModule(mockedModule);

        checkGetModuleInfoReq(controller, mockedModule);
    }

    // Useful to check, that other controller aggregates positioned module controller correctly
    // controller - instance of controller, that use PositionedModuleController
    // mockedModule - mock instance of PositionedModule, to which controller is connected
    public static void inheritanceChecker(ModuleController controller, BaseModuleInterface mockedModule) {
        checkGetModuleInfoReq(controller, mockedModule);
    }

    private static void checkGetModuleInfoReq(ModuleController controller, BaseModuleInterface mockedModule) {
        when(mockedModule.getType()).thenReturn("Test Type");
        when(mockedModule.getModel()).thenReturn("Test Model");
        when(mockedModule.getParameters()).thenReturn("Test Parameters");

        Message response = controller.onRequest(1, "getModuleInfo ");
        assertEquals(
                "Type: \"Test Type\", Model: \"Test Model\", Parameters: \"Test Parameters\"",
                response.data);

        verify(mockedModule).getType();
        verify(mockedModule).getModel();
        verify(mockedModule).getParameters();
    }

}
