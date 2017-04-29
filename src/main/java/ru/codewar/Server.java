package ru.codewar;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.database.playerloader.PlayerLoader;
import ru.codewar.database.playerloader.PlayerLoaderDummy;
import ru.codewar.database.shiploader.ShipLoader;
import ru.codewar.database.shiploader.ShipLoaderDummy;
import ru.codewar.logicconveyor.ShipsLogicConveyor;
import ru.codewar.logicconveyor.concept.MultithreadConveyor;
import ru.codewar.logicconveyor.physicallogic.PhysicalLogic;
import ru.codewar.networking.ConnectionManager;
import ru.codewar.networking.StringDatagramSocket;
import ru.codewar.world.PlayerGate;
import ru.codewar.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String args[]) {
        // General server settings:
        String version = "0.1.0";
        int loginSocketPort = 4835;
        int millisecondsInTick = 1;
        int extraThreadsNumber = 3;
        logger.info(
                "CodeWar server {} started on port {}! millisecondsInTick: {} ms; totalThreads: {}",
                version, loginSocketPort, millisecondsInTick, extraThreadsNumber);

        // Creating logics for multithread multithreadConveyor
        PhysicalLogic physicalEngine = new PhysicalLogic();
        physicalEngine.setSecondsInTick(millisecondsInTick / 1000);

        ShipsLogicConveyor shipsLogicConveyor = new ShipsLogicConveyor();

        // Creating world, loaders and player gate
        World world = new World(physicalEngine);
        ShipLoader shipLoader = new ShipLoaderDummy();
        PlayerLoader playerLoader = new PlayerLoaderDummy(shipLoader);
        PlayerGate gate = new PlayerGate(world, playerLoader);
        gate.addPlayer("admin", "admin");

        // Creating ConnectionManager:
        StringDatagramSocket loginSocket;
        try {
            loginSocket = new StringDatagramSocket(new InetSocketAddress(loginSocketPort), null);
        } catch (IOException exception) {
            logger.error("Can't create login socket! Details: {}", exception);
            return;
        }
        ConnectionManager connectionManager = new ConnectionManager(gate, loginSocket);
        connectionManager.initPortsPool(10000, 10032);

        // Adding all logics to multithreadConveyor
        MultithreadConveyor multithreadConveyor = new MultithreadConveyor(extraThreadsNumber);
        multithreadConveyor.addLogic(connectionManager);
        multithreadConveyor.addLogic(physicalEngine);
        multithreadConveyor.addLogic(shipsLogicConveyor);

        while(true) {
            multithreadConveyor.proceed();
            try {
                Thread.sleep(millisecondsInTick);
            } catch(Exception exception) {
                logger.warn("Unhandled exception has occurred during Thread.sleep: {}", exception);
            }
        }
    }
}
