package com.smoker.mahjong;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class MahjongApplicationTests {
// F1 T1
    @Test
    public void contextLoads() {
        // 测试 Spring 应用程序上下文是否正确加载
        assertTrue(true, "Spring 应用程序上下文未能正确加载");
    }
}
