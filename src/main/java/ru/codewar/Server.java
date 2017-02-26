package ru.codewar;


import ru.codewar.networking.ConnectionManager;
import ru.codewar.networking.StringDatagramSocket;
import ru.codewar.world.Player;
import ru.codewar.world.PlayerGate;
import ru.codewar.world.World;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Set;

public class Server {

    public static void main(String args[]) {

        World world = new World();
        world.addPlayer(new Player("admin"));

        PlayerGate gate = new PlayerGate(world);
        gate.addPlayer("admin", "admin");

        StringDatagramSocket loginSocket = null;
        try {
            loginSocket = new StringDatagramSocket(new InetSocketAddress(4835), null);
        } catch (IOException exception) {
            System.out.println("Can't create getLogin socket! Details: " + exception);
            return;
        }

        ConnectionManager connectionManager = new ConnectionManager(gate, loginSocket);
        connectionManager.initPortsPool(10000, 10032);

        while(true) {
            connectionManager.proceedStage(0, 0, 1);
            connectionManager.proceedStage(1, 0, 1);
            connectionManager.proceedStage(2, 0, 1);
            try {
                Thread.sleep(1);
            } catch(Exception exception) {}
        }
    }
}
