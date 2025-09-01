package momomo.study.websocket.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketClient;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class AfterStartup {

    private final WebSocketClient wsClient;
    private final WebSocketHandler wsHandler;

    @PostConstruct
    public void init() {
        wsClient
                .execute(wsHandler, "ws://localhost:8080/ws-endpoint")
                .thenCompose(session -> {
                    try {
                        session.sendMessage(new TextMessage("Hello from client!"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    return CompletableFuture.completedFuture(session);
                });
    }
}
