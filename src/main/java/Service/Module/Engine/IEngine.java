package Service.Module.Engine;

/**
 * Created by Александр on 07.12.2016.
 */
public interface IEngine {

    double getMaxThrust();
    double getCurrentThrust();

    void setThrust(double thrust);

}
