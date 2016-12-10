package ServiceImpl.Protocol.Module;

import org.junit.Test;
import static org.mockito.Mockito.*;

import Service.Networking.IChannel;
import Service.Protocol.Module.IModuleController;


public class ModuleOperatorTests {
    @Test
    public void proceedRequestWithImmediateResponse() {
        IModuleController mockedController = mock(IModuleController.class);
        IChannel mockedChannel = mock(IChannel.class);

        ModuleOperator moduleOperator = new ModuleOperator();
        moduleOperator.attachToChannel(mockedChannel);
        moduleOperator.attachToModuleController(mockedController);

        when(mockedController.onRequest(12, "test request")).thenReturn("test response");

        moduleOperator.onMessageReceived("REQ 12 test request");

        verify(mockedController).onRequest(12, "test request");
        verify(mockedChannel).sendMessage("RESP 12 test response");
    }
}
