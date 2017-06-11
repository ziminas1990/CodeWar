package ru.codewar.module.engine;

import org.json.JSONObject;
import org.junit.Test;
import static org.mockito.Mockito.*;

import ru.codewar.module.IModulesPlatform;
import ru.codewar.module.IPlatformedModule;

import static org.junit.Assert.*;

public class EngineFactoryTests {

    EngineLoader factory = new EngineLoader();

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
        IPlatformedModule module = factory.makeModule(
                "engine", "platform engine", "engine.forward.1",
                new JSONObject(description).getJSONObject("parameters"), platform);
        assertTrue(module instanceof PlatformEngine);
        PlatformEngine engine = (PlatformEngine)module;
        assertTrue(engine != null);
        assertEquals("ship.engine.forward.1", engine.getModuleAddress());
        assertEquals("platform engine", engine.getModuleModel());
        assertEquals(10000, engine.getMaxThrust(), 0.1);
        assertEquals(PlatformEngine.Orientation.eOrientationForward, engine.getEngineOrientation());
    }
}
