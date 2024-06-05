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
// T4 P4
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testSignup() throws Exception {
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "{\"code\":200,\"msg\":\"ok\",\"data\":\"success\"}";

        when(userService.signup(name, password)).thenReturn(mockResult);

        mockMvc.perform(post("/user/signup")
                        .param("name", name)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().json(mockResult));

        verify(userService, times(1)).signup(name, password);
    }

    @Test
    public void testLogin() throws Exception {
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "{\"code\":200,\"msg\":\"ok\",\"data\":\"success\"}";

        when(userService.login(name, password)).thenReturn(mockResult);

        mockMvc.perform(post("/user/login")
                        .param("name", name)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().json(mockResult));

        verify(userService, times(1)).login(name, password);
    }

    @Test
    public void testDeleteUser() throws Exception {
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "{\"code\":200,\"msg\":\"ok\",\"data\":\"success\"}";

        when(userService.deleteUser(name, password)).thenReturn(mockResult);

        mockMvc.perform(post("/user/deleteUser")
                        .param("name", name)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().json(mockResult));

        verify(userService, times(1)).deleteUser(name, password);
    }

    @Test
    public void testChangePassword() throws Exception {
        String name = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String mockResult = "{\"code\":200,\"msg\":\"ok\",\"data\":\"success\"}";

        when(userService.changePassword(name, oldPassword, newPassword)).thenReturn(mockResult);

        mockMvc.perform(post("/user/changePassword")
                        .param("name", name)
                        .param("oldPassword", oldPassword)
                        .param("newPassword", newPassword))
                .andExpect(status().isOk())
                .andExpect(content().json(mockResult));

        verify(userService, times(1)).changePassword(name, oldPassword, newPassword);
    }
}
