package momomo.study.websocket.client.session;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momomo.study.websocket.client.outbound.WsClientSender;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class WsClientHandler extends TextWebSocketHandler {

    private final String message;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        log.info("---> [Client] Session {} created", session.getId());
        new WsClientSender(session).send(message);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws IOException {
        if (session.isOpen()) {
            session.close();
        }

        log.info("---> [Client] Session {} closed: {}", session.getId(), closeStatus.getCode());
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, TextMessage message) {
        log.info("---> [Client] Session {} received message {}", session.getId(), message.getPayload());
    }

}
