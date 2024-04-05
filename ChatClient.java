import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.websocket.*;

import java.net.URI;

public class ChatClient extends Application {

    private WebSocketContainer container;
    private Session session;

    private TextArea chatArea;
    private TextField messageField;

    @Override
    public void start(Stage primaryStage) {
        connectToServer();

        chatArea = new TextArea();
        messageField = new TextField();
        messageField.setOnAction(event -> sendMessage());

        VBox root = new VBox(chatArea, messageField);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Chat");
        primaryStage.show();
    }

    private void connectToServer() {
        container = ContainerProvider.getWebSocketContainer();
        try {
            URI uri = new URI("ws://localhost:8080/chat");
            session = container.connectToServer(new Endpoint() {
                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    System.out.println("Connected to server");
                }

                @Override
                public void onMessage(Session session, String message) {
                    chatArea.appendText(message + "\n");
                }

                @Override
                public void onClose(Session session, CloseReason closeReason) {
                    System.out.println("Connection closed: " + closeReason);
                }
            }, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        try {
            session.getBasicRemote().sendText(messageField.getText());
            messageField.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
