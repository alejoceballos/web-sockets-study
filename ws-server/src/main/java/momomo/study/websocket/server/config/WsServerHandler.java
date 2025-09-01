package momomo.study.websocket.server.config;

import lombok.extern.slf4j.Slf4j;
import momomo.study.websocket.server.outbound.WsServerSender;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.*;

import static io.micrometer.common.util.StringUtils.isNotBlank;

@Slf4j
@Component
public class WsServerHandler extends TextWebSocketHandler {

    ConcurrentMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    ConcurrentMap<String, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        log.info("---> [Server] Connection established with session {}", session.getUri());
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        log.info("---> [Server] Connection closed for session {}", session.getId());

        sessions.remove(session.getId(), session);
        log.info("---> [Server] Session {} removed", session.getId());

        futures.get(session.getId()).cancel(true);
        log.info("---> [Server] Heartbeat task for session {} cancelled", session.getId());

        futures.remove(session.getId());
        log.info("---> [Server] Heartbeat task for session {} removed", session.getId());
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        log.info("---> [Server] Session {} received the message {}",
                session.getId(),
                message.getPayload());

        if (!sessions.containsKey(session.getId()) && isNotBlank(message.getPayload())) {
            log.info("---> [Server] Session {} will be stored and won't accept any more messages", session.getId());
            sessions.put(session.getId(), session);

            ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {
                        log.info("---> [Server] Will send heartbeat to session {}", sessions.get(session.getId()).getId());

                        new WsServerSender(sessions.get(session.getId()))
                                .send("10s heartbeat because you sent a message with %s"
                                        .formatted(message.getPayload()));
                    },
                    10,
                    10,
                    TimeUnit.SECONDS);
            futures.put(session.getId(), future);
        } else {
            log.info("---> [Server] Session {} won't accept more messages", sessions.get(session.getId()).getId());
        }
    }

}
