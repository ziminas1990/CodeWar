import ServiceImpl.Protocol.Module.ModuleOperatorTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Created by Александр on 15.10.2016.
 */

public class Server {
    public static void main(String args[])
    {
        System.out.println("Running synergy server");

        Result result = JUnitCore.runClasses(ModuleOperatorTests.class);
        for (Failure failure : result.getFailures()) {
          System.out.println(failure.toString());
        }
    }
}
