package ru.codewar.module.engine;

import org.json.JSONObject;
import org.junit.Test;
import static org.mockito.Mockito.*;

import ru.codewar.module.IModulesPlatform;

import static org.junit.Assert.*;

public class EngineFactoryTests {

    @Test
    public void makePlatformEngine() {
        IModulesPlatform platform = mock(IModulesPlatform.class);
        when(platform.getPlatformAddress()).thenReturn("ship");
        String description =
                "{\n" +
                        "  \"address\"    : \"engine.forward.1\",\n" +
                        "  \"model\"      : \"platform engine\",\n" +
                        "  \"parameters\" : {\n" +
                        "    \"orientation\" : \"forward\",\n" +
                        "    \"maxThrust\"   : 10000\n" +
                        "  }\n" +
                        "}";
        PlatformEngine engine = (PlatformEngine)EngineFactory.make(new JSONObject(description), platform);
        assertTrue(engine != null);
        assertEquals("ship.engine.forward.1", engine.getModuleAddress());
        assertEquals("platform engine", engine.getModuleModel());
        assertEquals(10000, engine.getMaxThrust(), 0.1);
        assertEquals(PlatformEngine.Orientation.eOrientationForward, engine.getEngineOrientation());
    }
}
