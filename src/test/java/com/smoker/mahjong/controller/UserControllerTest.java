package com.smoker.mahjong.controller;

import com.smoker.mahjong.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
// P4 T4

    // MockMvc 用于模拟 HTTP 请求和响应
    private MockMvc mockMvc;

    // Mock UserService 对象，用于模拟 UserService 的行为
    @Mock
    private UserService userService;

    // 注入模拟的 UserService 到 UserController 中
    @InjectMocks
    private UserController userController;

    // 在每个测试之前进行初始化
    @BeforeEach
    public void setUp() {
        // 使用 MockMvcBuilders 创建 MockMvc 实例
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    // 测试用户注册接口
    @Test
    public void testSignup() throws Exception {
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "{\"code\":200,\"msg\":\"ok\",\"data\":\"success\"}";

        // 模拟 UserService 的 signup 方法返回预期的结果
        when(userService.signup(name, password)).thenReturn(mockResult);

        // 使用 MockMvc 模拟发送 POST 请求，并验证响应
        mockMvc.perform(post("/user/signup")
                        .param("name", name)
                        .param("password", password))
                .andExpect(status().isOk()) // 验证状态码是否为 200
                .andExpect(content().json(mockResult)); // 验证返回的 JSON 是否符合预期

        // 验证 UserService 的 signup 方法是否被调用一次
        verify(userService, times(1)).signup(name, password);
    }

    // 测试用户登录接口
    @Test
    public void testLogin() throws Exception {
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "{\"code\":200,\"msg\":\"ok\",\"data\":\"success\"}";

        // 模拟 UserService 的 login 方法返回预期的结果
        when(userService.login(name, password)).thenReturn(mockResult);

        // 使用 MockMvc 模拟发送 POST 请求，并验证响应
        mockMvc.perform(post("/user/login")
                        .param("name", name)
                        .param("password", password))
                .andExpect(status().isOk()) // 验证状态码是否为 200
                .andExpect(content().json(mockResult)); // 验证返回的 JSON 是否符合预期

        // 验证 UserService 的 login 方法是否被调用一次
        verify(userService, times(1)).login(name, password);
    }

    // 测试删除用户接口
    @Test
    public void testDeleteUser() throws Exception {
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "{\"code\":200,\"msg\":\"ok\",\"data\":\"success\"}";

        // 模拟 UserService 的 deleteUser 方法返回预期的结果
        when(userService.deleteUser(name, password)).thenReturn(mockResult);

        // 使用 MockMvc 模拟发送 POST 请求，并验证响应
        mockMvc.perform(post("/user/deleteUser")
                        .param("name", name)
                        .param("password", password))
                .andExpect(status().isOk()) // 验证状态码是否为 200
                .andExpect(content().json(mockResult)); // 验证返回的 JSON 是否符合预期

        // 验证 UserService 的 deleteUser 方法是否被调用一次
        verify(userService, times(1)).deleteUser(name, password);
    }

    // 测试修改用户密码接口
    @Test
    public void testChangePassword() throws Exception {
        String name = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String mockResult = "{\"code\":200,\"msg\":\"ok\",\"data\":\"success\"}";

        // 模拟 UserService 的 changePassword 方法返回预期的结果
        when(userService.changePassword(name, oldPassword, newPassword)).thenReturn(mockResult);

        // 使用 MockMvc 模拟发送 POST 请求，并验证响应
        mockMvc.perform(post("/user/changePassword")
                        .param("name", name)
                        .param("oldPassword", oldPassword)
                        .param("newPassword", newPassword))
                .andExpect(status().isOk()) // 验证状态码是否为 200
                .andExpect(content().json(mockResult)); // 验证返回的 JSON 是否符合预期

        // 验证 UserService 的 changePassword 方法是否被调用一次
        verify(userService, times(1)).changePassword(name, oldPassword, newPassword);
    }
}
