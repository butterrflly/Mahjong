package com.smoker.mahjong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Configuration class for WebSocket.
 * This class is responsible for configuring WebSocket support in the application.
 */
@Configuration
public class WebSocketConfig {

    /**
     * Bean to enable WebSocket support in the application.
     * The ServerEndpointExporter bean will automatically register all endpoints annotated with @ServerEndpoint.
     *
     * @return an instance of ServerEndpointExporter
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
