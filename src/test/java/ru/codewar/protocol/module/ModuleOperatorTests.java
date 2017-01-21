package ru.codewar.protocol.module;

import org.junit.Test;
import ru.codewar.networking.Channel;

import static org.mockito.Mockito.*;


public class ModuleOperatorTests {

    @Test
    public void proceedRequestWithImmediateResponse() {
        ModuleController mockedController = mock(ModuleController.class);
        Channel mockedChannel = mock(Channel.class);

        ModuleOperator moduleOperator = new ModuleOperator();
        moduleOperator.attachToChannel(mockedChannel);
        moduleOperator.attachToModuleController(mockedController);

        when(mockedController.onRequest(12, "test request")).thenReturn("test response");

        moduleOperator.onMessageReceived("REQ 12 test request");

        verify(mockedController).onRequest(12, "test request");
        verify(mockedChannel).sendMessage("RESP 12 test response");
    }

    @Test
    public void proceedRequestWithDelayedResponse() {
        ModuleController mockedController = mock(ModuleController.class);
        Channel mockedChannel = mock(Channel.class);

        ModuleOperator moduleOperator = new ModuleOperator();
        moduleOperator.attachToChannel(mockedChannel);
        moduleOperator.attachToModuleController(mockedController);

        moduleOperator.onMessageReceived("REQ 12 test request");

        verify(mockedController).onRequest(12, "test request");
        verify(mockedChannel, times(0)).sendMessage(null);

        moduleOperator.onResponse(12, "test response");
        verify(mockedChannel).sendMessage("RESP 12 test response");
    }
}
