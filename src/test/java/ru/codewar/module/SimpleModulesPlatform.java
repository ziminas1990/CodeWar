package ru.codewar.module;


import ru.codewar.geometry.Vector;
import ru.codewar.logicconveyor.physicallogic.PhysicalObjectImpl;

import java.util.ArrayList;

public class SimpleModulesPlatform extends PhysicalObjectImpl implements IModulesPlatform {

    private String address;
    private ArrayList<IBaseModule> modules = new ArrayList<>();
    private Vector orientation = new Vector();

    public SimpleModulesPlatform() {
        super(1, 1);
    }
    public SimpleModulesPlatform(String address) {
        super(1, 1);
        this.address = address;
    }

    public void installModule(IBaseModule module) { modules.add(module); }
    public void setOrientation(Vector orientation) { this.orientation = orientation; }

    @Override // From IModulesPlatform
    public int getModulesCount() { return modules.size(); }
    @Override // From IModulesPlatform
    public IBaseModule getModule(int index) { return modules.get(index); }
    @Override // From IModulesPlatform
    public String getPlatformAddress() { return address; }

    @Override // From IModulesPlatform -> RotatableModuleType
    public double getMaxRotationSpeed() { return 0; }
    @Override // From IModulesPlatform -> RotatableModuleType
    public void rotate(double delta, double speed) {}
    @Override // From IModulesPlatform -> RotatableModuleType
    public Vector getOrientation() { return orientation; }

}
