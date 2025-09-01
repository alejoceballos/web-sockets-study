package momomo.study.websocket.client.inbound;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import momomo.study.websocket.client.session.WsSessionManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InputController {

    private final WsSessionManager wsSessionManager;

    @GetMapping("/connect-and-keep-sending/{message}")
    public ResponseEntity<String> connect(@PathVariable("message") String message)
            throws ExecutionException, InterruptedException {
        log.info("---> [Client] Connect and send: {}", message);

        return ResponseEntity.ok().body(wsSessionManager.connect(message));
    }

}
