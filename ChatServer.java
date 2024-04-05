import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint("/chat")
public class ChatServer {

    private static Map<String, Session> sessions = new HashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("New connection: " + session.getId());
        sessions.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message from " + session.getId() + ": " + message);
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed: " + session.getId());
        sessions.remove(session.getId());
    }

    private void broadcast(String message) {
        sessions.values().forEach(session -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        // Start the WebSocket server
        WebSocketServerContainerInitializer.configureContext(ChatServer.class, null);
    }
}
