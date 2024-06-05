package com.smoker.mahjong.service;

import com.alibaba.fastjson2.JSONObject;
import com.smoker.mahjong.data.UserRegistration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class) // 使用Mockito扩展进行单元测试
public class UserServiceTest {
// P4 T4

    @InjectMocks
    private UserService userService; // 注入待测试的UserService类

    private MockedStatic<UserRegistration> mockedUserRegistration; // 模拟UserRegistration类的静态方法

    @BeforeEach
    public void setUp() {
        mockedUserRegistration = Mockito.mockStatic(UserRegistration.class); // 在每个测试方法前设置静态模拟
    }

    @AfterEach
    public void tearDown() {
        mockedUserRegistration.close(); // 在每个测试方法后关闭静态模拟
    }

    @Test
    public void testSignup() {
        // Given: 设置测试的输入参数和模拟结果
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "success";

        // 模拟UserRegistration.signup静态方法的返回值
        mockedUserRegistration.when(() -> UserRegistration.signup(name, password)).thenReturn(mockResult);

        // When: 调用待测试的方法
        String result = userService.signup(name, password);

        // Then: 验证方法的输出结果是否符合预期
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("code", 200);
        expectedMap.put("msg", "ok");
        expectedMap.put("data", mockResult);
        String expectedJson = JSONObject.toJSONString(expectedMap);

        // 断言返回的JSON字符串与预期结果相同
        assertEquals(expectedJson, result);
    }

    @Test
    public void testLogin() {
        // Given: 设置测试的输入参数和模拟结果
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "success";

        // 模拟UserRegistration.login静态方法的返回值
        mockedUserRegistration.when(() -> UserRegistration.login(name, password)).thenReturn(mockResult);

        // When: 调用待测试的方法
        String result = userService.login(name, password);

        // Then: 验证方法的输出结果是否符合预期
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("code", 200);
        expectedMap.put("msg", "ok");
        expectedMap.put("data", mockResult);
        String expectedJson = JSONObject.toJSONString(expectedMap);

        // 断言返回的JSON字符串与预期结果相同
        assertEquals(expectedJson, result);
    }

    @Test
    public void testDeleteUser() {
        // Given: 设置测试的输入参数和模拟结果
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "success";

        // 模拟UserRegistration.deleteUser静态方法的返回值
        mockedUserRegistration.when(() -> UserRegistration.deleteUser(name, password)).thenReturn(mockResult);

        // When: 调用待测试的方法
        String result = userService.deleteUser(name, password);

        // Then: 验证方法的输出结果是否符合预期
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("code", 200);
        expectedMap.put("msg", "ok");
        expectedMap.put("data", mockResult);
        String expectedJson = JSONObject.toJSONString(expectedMap);

        // 断言返回的JSON字符串与预期结果相同
        assertEquals(expectedJson, result);
    }

    @Test
    public void testChangePassword() {
        // Given: 设置测试的输入参数和模拟结果
        String name = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String mockResult = "success";

        // 模拟UserRegistration.changePassword静态方法的返回值
        mockedUserRegistration.when(() -> UserRegistration.changePassword(name, oldPassword, newPassword)).thenReturn(mockResult);

        // When: 调用待测试的方法
        String result = userService.changePassword(name, oldPassword, newPassword);

        // Then: 验证方法的输出结果是否符合预期
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("code", 200);
        expectedMap.put("msg", "ok");
        expectedMap.put("data", mockResult);
        String expectedJson = JSONObject.toJSONString(expectedMap);

        // 断言返回的JSON字符串与预期结果相同
        assertEquals(expectedJson, result);
    }
}
