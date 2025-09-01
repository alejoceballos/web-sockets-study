package momomo.study.websocket.client.session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;

import java.util.concurrent.ExecutionException;

@Component
public class WsSessionManager {

    private final String wsUri;
    private final WebSocketClient wsClient;

    public WsSessionManager(
            @Value("${ws.uri}") String wsUri,
            WebSocketClient wsClient) {
        this.wsUri = wsUri;
        this.wsClient = wsClient;
    }

    public String connect(String message) throws ExecutionException, InterruptedException {
        return wsClient
                .execute(new WsClientHandler(message), wsUri)
                .get()
                .getId();
    }

}
