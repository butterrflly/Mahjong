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

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
// P4 T4
    @InjectMocks
    private UserService userService;

    private MockedStatic<UserRegistration> mockedUserRegistration;

    @BeforeEach
    public void setUp() {
        mockedUserRegistration = Mockito.mockStatic(UserRegistration.class);
    }

    @AfterEach
    public void tearDown() {
        mockedUserRegistration.close();
    }

    @Test
    public void testSignup() {
        // Given
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "success";

        mockedUserRegistration.when(() -> UserRegistration.signup(name, password)).thenReturn(mockResult);

        // When
        String result = userService.signup(name, password);

        // Then
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("code", 200);
        expectedMap.put("msg", "ok");
        expectedMap.put("data", mockResult);
        String expectedJson = JSONObject.toJSONString(expectedMap);

        assertEquals(expectedJson, result);
    }

    @Test
    public void testLogin() {
        // Given
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "success";

        mockedUserRegistration.when(() -> UserRegistration.login(name, password)).thenReturn(mockResult);

        // When
        String result = userService.login(name, password);

        // Then
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("code", 200);
        expectedMap.put("msg", "ok");
        expectedMap.put("data", mockResult);
        String expectedJson = JSONObject.toJSONString(expectedMap);

        assertEquals(expectedJson, result);
    }

    @Test
    public void testDeleteUser() {
        // Given
        String name = "testUser";
        String password = "testPassword";
        String mockResult = "success";

        mockedUserRegistration.when(() -> UserRegistration.deleteUser(name, password)).thenReturn(mockResult);

        // When
        String result = userService.deleteUser(name, password);

        // Then
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("code", 200);
        expectedMap.put("msg", "ok");
        expectedMap.put("data", mockResult);
        String expectedJson = JSONObject.toJSONString(expectedMap);

        assertEquals(expectedJson, result);
    }

    @Test
    public void testChangePassword() {
        // Given
        String name = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        String mockResult = "success";

        mockedUserRegistration.when(() -> UserRegistration.changePassword(name, oldPassword, newPassword)).thenReturn(mockResult);

        // When
        String result = userService.changePassword(name, oldPassword, newPassword);

        // Then
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("code", 200);
        expectedMap.put("msg", "ok");
        expectedMap.put("data", mockResult);
        String expectedJson = JSONObject.toJSONString(expectedMap);

        assertEquals(expectedJson, result);
    }
}
