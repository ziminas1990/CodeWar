package ru.codewar.module.scaner;

import org.junit.Test;
import ru.codewar.module.BaseModuleControllerTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ScannerControllerTests {

    @Test
    public void checkIfSupportedTest() {
        // check interface, that specific for engine
        assertTrue(ScannerController.checkIfSupported("scan 1e6 1000 100000"));
    }

    @Test
    public void checkControllerInheritances() {
        IScannerModule mockedScanner = mock(IScannerModule.class);

        ScannerController controller = new ScannerController();
        controller.attachToModule(mockedScanner);

        BaseModuleControllerTests.inheritanceChecker(controller, mockedScanner);
    }

    @Test
    public void scanningRequestTest() {

        IScannerModule mockedScanner = mock(IScannerModule.class);
        ScannerOperator mockedOperator = mock(ScannerOperator.class);

        ScannerController controller = new ScannerController();
        controller.attachToModule(mockedScanner);
        controller.attachToOperator(mockedOperator);

        controller.onRequest(15, "scan 1e6 1e3 100e3");

        verify(mockedScanner).scanning(15, Double.valueOf("1e6"), Double.valueOf("1e3"), Double.valueOf("100e3"));

    }
}
