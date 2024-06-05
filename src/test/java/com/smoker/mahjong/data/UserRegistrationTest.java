package com.smoker.mahjong.data;

import org.junit.jupiter.api.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UserRegistrationTest {
// P11 T11

    private static final String TEST_FILENAME = "test_users.txt"; // 测试文件名
    private static String originalFilename; // 原始文件名

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
            bw.write("existingUser existingPassword\n"); // 写入一个已存在的用户信息
        }
    }

    @AfterEach
    public void tearDown() {
        // 删除测试文件
        File file = new File(TEST_FILENAME);
        if (file.exists()) {
            file.delete(); // 如果测试文件存在，删除它
        }
    }

    @Test
    public void testSignup() {
        // 测试用户注册功能
        String result = UserRegistration.signup("newUser", "newPassword");
        assertEquals("注册成功", result); // 验证注册结果是否正确
    }

    @Test
    public void testSignupUserExists() {
        // 测试注册已存在的用户
        String result = UserRegistration.signup("existingUser", "newPassword");
        assertEquals("用户已存在", result); // 验证是否返回用户已存在的提示
    }

    @Test
    public void testLogin() {
        // 测试用户登录功能
        String result = UserRegistration.login("existingUser", "existingPassword");
        assertEquals("登录成功", result); // 验证登录结果是否正确
    }

    @Test
    public void testLoginWrongPassword() {
        // 测试使用错误密码登录
        String result = UserRegistration.login("existingUser", "wrongPassword");
        assertEquals("密码错误", result); // 验证是否返回密码错误的提示
    }

    @Test
    public void testLoginUserNotFound() {
        // 测试登录不存在的用户
        String result = UserRegistration.login("nonExistentUser", "password");
        assertEquals("用户不存在", result); // 验证是否返回用户不存在的提示
    }

    @Test
    public void testDeleteUser() {
        // 测试删除用户功能
        String result = UserRegistration.deleteUser("existingUser", "existingPassword");
        assertEquals("删除成功", result); // 验证删除结果是否正确
    }

    @Test
    public void testDeleteUserWrongPassword() {
        // 测试使用错误密码删除用户
        String result = UserRegistration.deleteUser("existingUser", "wrongPassword");
        assertEquals("密码错误", result); // 验证是否返回密码错误的提示
    }

    @Test
    public void testDeleteUserNotFound() {
        // 测试删除不存在的用户
        String result = UserRegistration.deleteUser("nonExistentUser", "password");
        assertEquals("用户不存在", result); // 验证是否返回用户不存在的提示
    }

    @Test
    public void testChangePassword() {
        // 测试修改密码功能
        String result = UserRegistration.changePassword("existingUser", "existingPassword", "newPassword");
        assertEquals("修改成功", result); // 验证修改结果是否正确
    }

    @Test
    public void testChangePasswordWrongOldPassword() {
        // 测试使用错误的旧密码修改密码
        String result = UserRegistration.changePassword("existingUser", "wrongPassword", "newPassword");
        assertEquals("密码错误", result); // 验证是否返回密码错误的提示
    }

    @Test
    public void testChangePasswordUserNotFound() {
        // 测试修改不存在的用户的密码
        String result = UserRegistration.changePassword("nonExistentUser", "password", "newPassword");
        assertEquals("用户不存在", result); // 验证是否返回用户不存在的提示
    }
}
