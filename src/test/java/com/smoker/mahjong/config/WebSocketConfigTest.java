package com.smoker.mahjong.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class WebSocketConfigTest {
// P1 T1

    @MockBean
    private ServerEndpointExporter serverEndpointExporter; // 模拟ServerEndpointExporter bean

    @Configuration
    @Import(WebSocketConfig.class)  // 引入 WebSocketConfig 配置类
    public static class Config {
        // 这里可以添加其他需要引入的配置类或Bean
    }

    @Test
    public void testServerEndpointExporterBean() {
        // 测试是否成功创建了 ServerEndpointExporter bean
        assertNotNull(serverEndpointExporter, "ServerEndpointExporter bean should not be null");
    }
}
