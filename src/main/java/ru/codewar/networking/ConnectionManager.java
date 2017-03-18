package ru.codewar.networking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.codewar.logicconveyor.concept.ConveyorLogic;
import ru.codewar.world.Player;
import ru.codewar.world.PlayerGate;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionManager implements ConveyorLogic {

    private final static int loginStageId = 0;
    private final static int receivingStageId = 1;
    private final static int sendingStageId = 2;
    private final static int totalStages = 3;

    private PlayerGate playerGate;
    private StringDatagramSocket loginSocket;

    private AtomicInteger connectionIdx = new AtomicInteger(0);
    private Stack<Integer> portsPool = new Stack<Integer>();
    private ArrayList<Connection> connections = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    public ConnectionManager(PlayerGate playerGate, StringDatagramSocket loginSocket)
    {
        logger.info("connection manager has been created");
        this.loginSocket = loginSocket;
        this.playerGate = playerGate;
    }

    public void initPortsPool(int firstPort, int lastPort) {
        logger.info("Connection manager ports pool set: {} - {}", firstPort, lastPort);
        for(int i = firstPort; i <= lastPort; i++) {
            portsPool.push(i);
        }
    }

    @Override // from ConveyorLogic
    public int stagesCount() { return totalStages; }
    @Override // from ConveyorLogic
    public boolean prepareStage(int stageId) {
        connectionIdx.set(0);
        return stageId == loginStageId || connections.size() > 0;
    }

    @Override // from ConveyorLogic
    public void proceedStage(int stageId, int threadId, int totalThreads)
    {
        if(stageId == loginStageId) {
            if(threadId == 0) {
                // Only master-thread provides authorization
                while(checkLogin());
            }
        } else {
            int idx = connectionIdx.getAndIncrement();
            if (stageId == receivingStageId) {
                for (; idx < connections.size(); idx = connectionIdx.getAndIncrement()) {
                    try {
                        connections.get(idx).receiveAllMessages();
                    } catch (IOException exception) {
                        logger.warn(
                                "Exception occurred during receiving messages from {}: {}",
                                connections.get(idx).getClientAddress(), exception);
                    }
                }
            } else if (stageId == sendingStageId) {
                for (; idx < connections.size(); idx = connectionIdx.getAndIncrement()) {
                    connections.get(idx).sendAllMessages();
                }
            }
        }
    }

    private boolean checkLogin() {
        Message loginRequest = loginSocket.tryToReceiveMessage();
        if(loginRequest == null)
            return false;
        SocketAddress source = loginSocket.lastReceivedFrameSource();
        logger.debug("Login request \"{}\" received from {}", loginRequest, source);
        Player player = playerGate.login(loginRequest);
        try {
            if(player != null) {
                if(portsPool.empty()) {
                    logger.warn("Can't create port for new connection!");
                    loginSocket.sendMessage(new Message("ERROR: can't create port for connection!"), source);
                } else {
                    int localPort = portsPool.pop();
                    StringDatagramSocket socket =
                            new StringDatagramSocket(new InetSocketAddress(localPort), source);
                    Connection connection = new Connection(socket, player.getMessagesEntryPoint());
                    connections.add(connection);
                    player.attachToChannel(connection);
                    loginSocket.sendMessage(new Message("port " + localPort), source);
                    logger.info("New port has been assigned for client {}: {}", source, localPort);
                }
            } else {
                loginSocket.sendMessage(new Message("ERROR: getLogin failed!"), source);
            }
        } catch (IOException exception) {
            logger.warn("Exception occurred during processing login request: {}", exception);
        }
        return true;
    }

}
