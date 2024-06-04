package com.smoker.mahjong.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * WebSocketConfigTest
 * 测试WebSocketConfig类的单元测试
 */
public class WebSocketConfigTest {
// F1 T1
    /**
     * 测试 serverEndpointExporter 方法是否正确创建了 ServerEndpointExporter 对象
     */
    @Test
    public void testServerEndpointExporter() {
        // 使用 Spring 的 AnnotationConfigApplicationContext 来加载配置类
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebSocketConfig.class);

        // 从上下文中获取 serverEndpointExporter bean
        ServerEndpointExporter serverEndpointExporter = context.getBean(ServerEndpointExporter.class);

        // 验证获取到的 bean 不为空
        assertNotNull(serverEndpointExporter, "ServerEndpointExporter should not be null");

        // 验证获取到的对象类型正确
        assertTrue(serverEndpointExporter instanceof ServerEndpointExporter, "Bean should be instance of ServerEndpointExporter");

        // 关闭上下文
        context.close();
    }
}
