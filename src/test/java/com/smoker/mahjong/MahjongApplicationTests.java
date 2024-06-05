package com.smoker.mahjong;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import(MahjongApplicationTests.Config.class)
public class MahjongApplicationTests {
// P1 T1

    @Configuration
    public static class Config {
        // 排除WebSocketConfig类，防止在测试环境中加载WebSocket相关的Bean
        // 可以在此处添加其他需要排除的配置类
    }

    @Test
    public void contextLoads() {
        // 测试 Spring 应用程序上下文是否正确加载
        assertTrue(true, "Spring 应用程序上下文未能正确加载");
    }
}
