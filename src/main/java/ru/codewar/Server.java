package ru.codewar;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codewar.database.playerloader.PlayerLoader;
import ru.codewar.database.playerloader.PlayerLoaderDummy;
import ru.codewar.logicconveyor.ShipsLogicConveyor;
import ru.codewar.logicconveyor.concept.MultithreadConveyor;
import ru.codewar.logicconveyor.physicallogic.PhysicalLogic;
import ru.codewar.module.ModulesFactory;
import ru.codewar.networking.ConnectionManager;
import ru.codewar.networking.StringDatagramSocket;
import ru.codewar.util.JsonStreamReader;
import ru.codewar.world.CelestialBodySystem;
import ru.codewar.world.PlayerGate;
import ru.codewar.world.WorldGenerator;
import ru.codewar.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    private static final int MILLION = 1000000;
    public static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String args[]) {
        // General server settings:
        String version = "0.1.0";
        int loginSocketPort = 4835;
        int millisecondsInTick = 2;
        int extraThreadsNumber = 3;
        logger.info(
                "CodeWar server {} started on port {}! millisecondsInTick: {} ms; totalThreads: {}",
                version, loginSocketPort, millisecondsInTick, extraThreadsNumber);

        // Creating logics for multithread multithreadConveyor
        PhysicalLogic physicalEngine = new PhysicalLogic();

        ShipsLogicConveyor shipsLogicConveyor = new ShipsLogicConveyor();

        // Creating world, loaders and player gate
        World world = new World(physicalEngine);
        JSONObject worldParameters = JsonStreamReader.read(
                Server.class.getResourceAsStream("database/worlds/SinglePlanetWorld.json"));
        if(worldParameters == null) {
            logger.error("Can't read world parameters!");
            return;
        } else {
            CelestialBodySystem centralBodyEnviroment = CelestialBodySystem.readFromJson(worldParameters, "world");
            if(centralBodyEnviroment == null) {
                logger.error("Can't parse world parameters!");
                return;
            }
            WorldGenerator generator = new WorldGenerator(0);
            generator.generateWorld(centralBodyEnviroment, world);
        }

        ModulesFactory modulesFactory = new ModulesFactory(world);
        PlayerLoader playerLoader = new PlayerLoaderDummy(modulesFactory);
        PlayerGate gate = new PlayerGate(world , playerLoader);
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
            long nanosecProceeded = multithreadConveyor.proceed(millisecondsInTick);
            long nanosecToSleep = millisecondsInTick * MILLION - nanosecProceeded;
            if(nanosecToSleep > MILLION) {
                try {
                    Thread.sleep(nanosecToSleep / MILLION, (int)(nanosecToSleep % MILLION));
                } catch (Exception exception) {
                    logger.warn("Unhandled exception has occurred during Thread.sleep: {}", exception);
                }
            }
        }
    }
}
