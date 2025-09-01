package momomo.study.websocket.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class WsClientConfig {

    @Bean
    public WebSocketClient wsClient() {
        return new StandardWebSocketClient();
    }

}
