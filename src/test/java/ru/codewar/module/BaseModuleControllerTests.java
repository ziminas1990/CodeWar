package ru.codewar.module;

import org.json.JSONObject;
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

    // Useful to check, that other controller aggregates base module controller correctly
    // controller - instance of controller, that use PositionedModuleController
    // mockedModule - mock instance of PositionedModule, to which controller is connected
    public static void inheritanceChecker(ModuleController controller, BaseModuleInterface mockedModule) {
        checkGetModuleInfoReq(controller, mockedModule);
    }

    private static void checkGetModuleInfoReq(ModuleController controller, BaseModuleInterface mockedModule) {
        when(mockedModule.getModuleAddress()).thenReturn("Test address");
        when(mockedModule.getModuleType()).thenReturn("Test Type");
        when(mockedModule.getModuleModel()).thenReturn("Test Model");
        when(mockedModule.getModuleInfo()).thenReturn("{\"test\" : \"value\"}");

        Message response = controller.onRequest(1, "getModuleInfo ");
        JSONObject info = new JSONObject(response.data);
        assertEquals(info.getString("address"), "Test address");
        assertEquals(info.getString("type"), "Test Type");
        assertEquals(info.getString("model"), "Test Model");
        assertEquals(info.getJSONObject("parameters").get("test"), "value");

        verify(mockedModule, atLeastOnce()).getModuleAddress();
        verify(mockedModule, atLeastOnce()).getModuleType();
        verify(mockedModule, atLeastOnce()).getModuleModel();
        verify(mockedModule, atLeastOnce()).getModuleInfo();
    }

}
