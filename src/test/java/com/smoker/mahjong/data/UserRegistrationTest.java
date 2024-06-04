package com.smoker.mahjong.data;

import org.junit.jupiter.api.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UserRegistrationTest {
// P11 T11
    private static final String TEST_FILENAME = "test_users.txt";
    private static String originalFilename;

    @BeforeAll
    public static void setUpClass() {
        // 保存原始文件名，并设置为测试文件名
        originalFilename = UserRegistration.getFilename();
        UserRegistration.setFilename(TEST_FILENAME);
    }

    @AfterAll
    public static void tearDownClass() {
        // 恢复原始文件名
        UserRegistration.setFilename(originalFilename);
    }

    @BeforeEach
    public void setUp() throws IOException {
        // 创建测试文件并写入初始数据
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TEST_FILENAME))) {
            bw.write("existingUser existingPassword\n");
        }
    }

    @AfterEach
    public void tearDown() {
        // 删除测试文件
        File file = new File(TEST_FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testSignup() {
        String result = UserRegistration.signup("newUser", "newPassword");
        assertEquals("注册成功", result);
    }

    @Test
    public void testSignupUserExists() {
        String result = UserRegistration.signup("existingUser", "newPassword");
        assertEquals("用户已存在", result);
    }

    @Test
    public void testLogin() {
        String result = UserRegistration.login("existingUser", "existingPassword");
        assertEquals("登录成功", result);
    }

    @Test
    public void testLoginWrongPassword() {
        String result = UserRegistration.login("existingUser", "wrongPassword");
        assertEquals("密码错误", result);
    }

    @Test
    public void testLoginUserNotFound() {
        String result = UserRegistration.login("nonExistentUser", "password");
        assertEquals("用户不存在", result);
    }

    @Test
    public void testDeleteUser() {
        String result = UserRegistration.deleteUser("existingUser", "existingPassword");
        assertEquals("删除成功", result);
    }

    @Test
    public void testDeleteUserWrongPassword() {
        String result = UserRegistration.deleteUser("existingUser", "wrongPassword");
        assertEquals("密码错误", result);
    }

    @Test
    public void testDeleteUserNotFound() {
        String result = UserRegistration.deleteUser("nonExistentUser", "password");
        assertEquals("用户不存在", result);
    }

    @Test
    public void testChangePassword() {
        String result = UserRegistration.changePassword("existingUser", "existingPassword", "newPassword");
        assertEquals("修改成功", result);
    }

    @Test
    public void testChangePasswordWrongOldPassword() {
        String result = UserRegistration.changePassword("existingUser", "wrongPassword", "newPassword");
        assertEquals("密码错误", result);
    }

    @Test
    public void testChangePasswordUserNotFound() {
        String result = UserRegistration.changePassword("nonExistentUser", "password", "newPassword");
        assertEquals("用户不存在", result);
    }
}
