package ru.codewar.protocol.module;

import org.junit.Test;
import ru.codewar.networking.Channel;
import ru.codewar.protocol.module.impl.ModuleOperatorImpl;

import static org.mockito.Mockito.*;


public class ModuleOperatorTests {

    @Test
    public void proceedRequestWithImmediateResponse() {
        ModuleController mockedController = mock(ModuleController.class);
        Channel mockedChannel = mock(Channel.class);

        ModuleOperatorImpl moduleOperator = new ModuleOperatorImpl();
        moduleOperator.attachToChannel(mockedChannel);
        moduleOperator.attachToModuleController(mockedController);

        when(mockedController.onRequest(12, "test request")).thenReturn("test response");

        moduleOperator.onMessageReceived("REQ 12 test request");

        verify(mockedController).onRequest(12, "test request");
        verify(mockedChannel).sendMessage("RESP 12 test response");
    }
}
