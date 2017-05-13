package ru.codewar.module;

public class PlatformedModule extends BaseModule implements IPlatformedModule {
    private IModulesPlatform platform;

    public PlatformedModule(IModulesPlatform platform,
                            String moduleAddress, String moduleType,
                            String moduleModel, String moduleInfo)
    {
        super(platform.getPlatformAddress() + "." + moduleAddress, moduleType, moduleModel, moduleInfo);
    }

    @Override // from IPlatformedModule
    public void installOnPlatform(IModulesPlatform platform) {
        this.platform = platform;
    }
    @Override // from IPlatformedModule
    public IModulesPlatform installedOn() { return platform; }
}
