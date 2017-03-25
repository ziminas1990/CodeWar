package ru.codewar.protocol.module;

import org.junit.Test;
import ru.codewar.networking.Channel;
import ru.codewar.networking.Message;

import static org.mockito.Mockito.*;


public class ModuleOperatorTests {

    @Test
    public void proceedRequestWithImmediateResponse() {
        ModuleController mockedController = mock(ModuleController.class);
        Channel mockedChannel = mock(Channel.class);

        ModuleOperator moduleOperator = new ModuleOperator();
        moduleOperator.attachToChannel(mockedChannel);
        moduleOperator.attachToModuleController(mockedController);

        when(mockedController.onRequest(12, "test request")).thenReturn(new Message("test response"));

        moduleOperator.onMessageReceived(new Message("test request").addHeader("REQ 12"));

        verify(mockedController).onRequest(12, "test request");
        verify(mockedChannel).sendMessage(new Message("test response").addHeader("RESP 12"));
    }

    @Test
    public void proceedRequestWithDelayedResponse() {
        ModuleController mockedController = mock(ModuleController.class);
        Channel mockedChannel = mock(Channel.class);

        ModuleOperator moduleOperator = new ModuleOperator();
        moduleOperator.attachToChannel(mockedChannel);
        moduleOperator.attachToModuleController(mockedController);

        moduleOperator.onMessageReceived(new Message("test request").addHeader("REQ 12"));

        verify(mockedController).onRequest(12, "test request");
        verify(mockedChannel, times(0)).sendMessage(null);

        moduleOperator.onResponse(12, new Message("test response"));
        verify(mockedChannel).sendMessage(new Message("test response").addHeader("RESP 12"));
    }
}
