package ru.codewar.module;

public class BaseModule implements IBaseModule {

    private String moduleAddress;
    private String moduleType;
    private String moduleModel;
    private String moduleInfo;

    public BaseModule(String moduleAddress, String moduleType,
                      String moduleModel, String moduleInfo) {
        this.moduleAddress = moduleAddress;
        this.moduleType = moduleType;
        this.moduleModel = moduleModel;
        this.moduleInfo = moduleInfo;
    }

    @Override // from IBaseModule
    public String getModuleAddress() { return moduleAddress; }
    @Override // from IBaseModule
    public String getModuleType() { return moduleType; }
    @Override // from IBaseModule
    public String getModuleModel() { return moduleModel; }
    @Override // from IBaseModule
    public String getModuleInfo() { return moduleInfo; }

}
