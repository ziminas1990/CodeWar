package ru.codewar.module.engine;

import org.junit.Test;
import ru.codewar.geometry.Vector;
import ru.codewar.module.IModulesPlatform;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class PlatformEngineTests {

    public boolean areVectorsVerySimilar(Vector a, Vector b) {
        return a.scalarProduct(b) > 0.999;
    }

    @Test
    public void getEngineOrientationTest() {

        IModulesPlatform platform = mock(IModulesPlatform.class);
        Vector platformOrientation = new Vector(1, 1).normalize();

        PlatformEngine forwardEngine =
                new PlatformEngine(platform, "engine", PlatformEngine.Orientation.eOrientationForward, 10000);
        PlatformEngine backwardEngine =
                new PlatformEngine(platform, "engine", PlatformEngine.Orientation.eOrientationBackward, 10000);
        PlatformEngine leftEngine =
                new PlatformEngine(platform, "engine", PlatformEngine.Orientation.eOrientationLeft, 10000);
        PlatformEngine rightEngine =
                new PlatformEngine(platform, "engine", PlatformEngine.Orientation.eOrientationRight, 10000);

        forwardEngine.installOnPlatform(platform);
        backwardEngine.installOnPlatform(platform);
        leftEngine.installOnPlatform(platform);
        rightEngine.installOnPlatform(platform);

        when(platform.getOrientation()).thenReturn(platformOrientation);

        assertEquals(platformOrientation, forwardEngine.getOrientation());
        assertEquals(0.999, backwardEngine.getOrientation().scalarProduct(platformOrientation.rotate(Math.PI)), 0.001);
        platformOrientation.rotate(-Math.PI);

        assertEquals(0.999, leftEngine.getOrientation().scalarProduct(platformOrientation.rotate(Math.PI/2)), 0.001);
        platformOrientation.rotate(-Math.PI/2);

        assertEquals(0.999, rightEngine.getOrientation().scalarProduct(platformOrientation.rotate(-Math.PI/2)), 0.001);
        platformOrientation.rotate(Math.PI/2);
    }

}
