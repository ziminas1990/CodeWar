package ru.codewar;


import ru.codewar.logicconveyor.concept.MultithreadConveyor;
import ru.codewar.logicconveyor.physicallogic.PhysicalLogic;
import ru.codewar.networking.ConnectionManager;
import ru.codewar.networking.StringDatagramSocket;
import ru.codewar.world.Player;
import ru.codewar.world.PlayerGate;
import ru.codewar.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String args[]) {

        // General server settings:
        int millisecondsInTick = 1;
        int extraThreadsNumber = 3;

        // Creating logics for multithread multithreadConveyor

        PhysicalLogic physicalEngine = new PhysicalLogic();
        physicalEngine.setSecondsInTick(millisecondsInTick / 1000);

        World world = new World(physicalEngine);

        PlayerGate gate = new PlayerGate(world);
        gate.addPlayer("admin", "admin");

        // Creating ConnectionManager:
        StringDatagramSocket loginSocket;
        try {
            loginSocket = new StringDatagramSocket(new InetSocketAddress(4835), null);
        } catch (IOException exception) {
            System.out.println("Can't create getLogin socket! Details: " + exception);
            return;
        }
        ConnectionManager connectionManager = new ConnectionManager(gate, loginSocket);
        connectionManager.initPortsPool(10000, 10032);

        // Adding all logics to multithreadConveyor
        MultithreadConveyor multithreadConveyor = new MultithreadConveyor(extraThreadsNumber);
        multithreadConveyor.addLogic(connectionManager);
        multithreadConveyor.addLogic(physicalEngine);

        while(true) {
            multithreadConveyor.proceed();
            try {
                Thread.sleep(millisecondsInTick);
            } catch(Exception exception) {}
        }
    }
}
