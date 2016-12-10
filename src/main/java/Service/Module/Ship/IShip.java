package Service.Module.Ship;

import Service.Module.Abstract.IPositionedModule;
import Service.Protocol.Module.IModuleController;

import java.util.ArrayList;

/**
 * Created by Александр on 08.12.2016.
 */
public interface IShip extends IPositionedModule {

    ArrayList<IModuleController> getModules();
}
