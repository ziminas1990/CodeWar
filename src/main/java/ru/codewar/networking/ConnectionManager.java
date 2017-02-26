package ru.codewar.networking;

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

    public ConnectionManager(PlayerGate playerGate, StringDatagramSocket loginSocket)
    {
        this.loginSocket = loginSocket;
        this.playerGate = playerGate;
    }

    public void initPortsPool(int firstPort, int lastPort) {
        for(int i = firstPort; i <= lastPort; i++) {
            portsPool.push(i);
        }
    }

    @Override // from ConveyorLogic
    public int stagesCount() { return totalStages; }
    @Override // from ConveyorLogic
    public void prepareStage(int stageId) {
        connectionIdx.set(0);
    }

    @Override // from ConveyorLogic
    public void proceedStage(int stageId, int threadId, int totalThreads)
    {
        if(stageId == loginStageId) {
            if(threadId == 0) {
                // Only master-thread provides authorization
                checkLogin();
            }
        } else {
            int idx = connectionIdx.getAndIncrement();
            if (stageId == receivingStageId) {
                for (; idx < connections.size(); idx = connectionIdx.getAndIncrement()) {
                    try {
                        connections.get(idx).receiveAllMessages();
                    } catch (IOException exception) {
                        continue;
                    }
                }
            } else if (stageId == sendingStageId) {
                for (; idx < connections.size(); idx = connectionIdx.getAndIncrement()) {
                    connections.get(idx).sendAllMessages();
                }
            }
        }
    }

    private void checkLogin() {
        Message loginRequest = loginSocket.tryToReceiveMessage();
        while(loginRequest != null) {
            SocketAddress source = loginSocket.lastReceivedFrameSource();
            Player player = playerGate.login(loginRequest);
            try {
                if(player != null) {
                    if(portsPool.empty()) {
                        loginSocket.sendMessage(new Message("ERROR: can't create port for connection!"), source);
                    } else {
                        int localPort = portsPool.pop();
                        StringDatagramSocket socket =
                                new StringDatagramSocket(new InetSocketAddress(localPort), source);
                        Connection connection = new Connection(socket, player.getMessagesEntryPoint());
                        connections.add(connection);
                        player.attachToChannel(connection);
                        loginSocket.sendMessage(new Message("port " + localPort), source);
                    }
                } else {
                    loginSocket.sendMessage(new Message("ERROR: getLogin failed!"), source);
                }
            } catch (IOException exception) {}
            loginRequest = loginSocket.tryToReceiveMessage();
        }
    }

}
