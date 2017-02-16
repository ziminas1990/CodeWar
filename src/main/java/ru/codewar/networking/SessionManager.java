package ru.codewar.networking;


import ru.codewar.logicconveyor.concept.ConveyorLogic;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionManager implements ConveyorLogic {

    private class Session implements Channel {
        private AbstractSocket socket;
        private NetworkTerminal userLogic;

        private ArrayList<String> messagesToSend = new ArrayList<>();

        public Session(AbstractSocket socket, NetworkTerminal userLogic) {
            this.socket = socket;
            this.userLogic = userLogic;
        }

        public void sendMessage(String message) {
            messagesToSend.add(message);
        }

        public void receiveAllMessages() {
            String message = socket.receiveMessage();
            while(!message.isEmpty()) {
                userLogic.onMessageReceived(message);
                message = socket.receiveMessage();
            }
        }

        public void sendAllMessages() {
            for(String message : messagesToSend) {
                socket.sendMessage(message);
            }
        }
    }

    private ArrayList<Session> sessions;
    private AtomicInteger index = new AtomicInteger(0);

    public void createNewSession(AbstractSocket socket, NetworkTerminal userLogic) {
        Session session = new Session(socket, userLogic);
        sessions.add(session);
    }

    public int stagesCount() { return 1; }

    public void prepareStage(int stage) { index.set(0); }

    public void proceedStage(int stage, int threadId, int totalThreads)
    {
        Session session = sessions.get(index.getAndIncrement());
        session.receiveAllMessages();
        session.sendAllMessages();
    }
}
