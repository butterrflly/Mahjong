package com.smoker.mahjong.data;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the UserRegistration class.
 */
public class UserRegistrationTest {

    private static final String TEST_FILENAME = "test_users.txt";

    @BeforeEach
    public void setUp() throws IOException {
        UserRegistration.setFilename(TEST_FILENAME);
        // Ensure the test file is clean before each test
        new BufferedWriter(new FileWriter(TEST_FILENAME, false)).close();
    }

    @AfterEach
    public void tearDown() {
        File file = new File(TEST_FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testSignup() {
        // Test user signup functionality
        String result = UserRegistration.signup("user1", "password1");
        assertEquals("注册成功", result); // Translation: "Registration successful"

        // Verify the user is added
        ArrayList<String> users = UserRegistration.readFile(TEST_FILENAME);
        assertEquals(1, users.size());
        assertEquals("user1 password1 off-line", users.get(0));
    }

    @Test
    public void testSignupUserAlreadyExists() {
        // Test signing up a user that already exists
        UserRegistration.signup("user1", "password1");
        String result = UserRegistration.signup("user1", "password1");
        assertEquals("用户已存在", result); // Translation: "User already exists"
    }

    @Test
    public void testLogin() {
        // Test user login functionality
        UserRegistration.signup("user1", "password1");
        String result = UserRegistration.login("user1", "password1");
        assertEquals("登录成功", result); // Translation: "Login successful"

        // Verify the user's status is updated to on-line
        ArrayList<String> users = UserRegistration.readFile(TEST_FILENAME);
        assertEquals("user1 password1 on-line", users.get(0));
    }

    @Test
    public void testLoginIncorrectPassword() {
        // Test login with incorrect password
        UserRegistration.signup("user1", "password1");
        String result = UserRegistration.login("user1", "wrongpassword");
        assertEquals("密码错误", result); // Translation: "Incorrect password"
    }

    @Test
    public void testLoginUserAlreadyLoggedIn() {
        // Test login when user is already logged in
        UserRegistration.signup("user1", "password1");
        UserRegistration.login("user1", "password1");
        String result = UserRegistration.login("user1", "password1");
        assertEquals("用户已登录", result); // Translation: "User already logged in"
    }

    @Test
    public void testLoginUserNotExists() {
        // Test login when user does not exist
        String result = UserRegistration.login("user1", "password1");
        assertEquals("用户不存在", result); // Translation: "User does not exist"
    }

    @Test
    public void testLogout() {
        // Test user logout functionality
        UserRegistration.signup("user1", "password1");
        UserRegistration.login("user1", "password1");
        String result = UserRegistration.logout("user1");
        assertEquals("退出成功", result); // Translation: "Logout successful"

        // Verify the user's status is updated to off-line
        ArrayList<String> users = UserRegistration.readFile(TEST_FILENAME);
        assertEquals("user1 password1 off-line", users.get(0));
    }

    @Test
    public void testLogoutUserNotLoggedIn() {
        // Test logout when user is not logged in
        UserRegistration.signup("user1", "password1");
        String result = UserRegistration.logout("user1");
        assertEquals("用户未登录", result); // Translation: "User not logged in"
    }

    @Test
    public void testLogoutUserNotExists() {
        // Test logout when user does not exist
        String result = UserRegistration.logout("user1");
        assertEquals("用户不存在", result); // Translation: "User does not exist"
    }

    @Test
    public void testDeleteUser() {
        // Test user deletion functionality
        UserRegistration.signup("user1", "password1");
        String result = UserRegistration.deleteUser("user1", "password1");
        assertEquals("删除成功", result); // Translation: "Deletion successful"

        // Verify the user is removed
        ArrayList<String> users = UserRegistration.readFile(TEST_FILENAME);
        assertEquals(0, users.size());
    }

    @Test
    public void testDeleteUserIncorrectPassword() {
        // Test deleting a user with incorrect password
        UserRegistration.signup("user1", "password1");
        String result = UserRegistration.deleteUser("user1", "wrongpassword");
        assertEquals("密码错误", result); // Translation: "Incorrect password"
    }

    @Test
    public void testDeleteUserNotExists() {
        // Test deleting a user that does not exist
        String result = UserRegistration.deleteUser("user1", "password1");
        assertEquals("用户不存在", result); // Translation: "User does not exist"
    }

    @Test
    public void testChangePassword() {
        // Test changing the password of a user
        UserRegistration.signup("user1", "password1");
        String result = UserRegistration.changePassword("user1", "password1", "newpassword");
        assertEquals("修改成功", result); // Translation: "Password changed successfully"

        // Verify the password is updated
        ArrayList<String> users = UserRegistration.readFile(TEST_FILENAME);
        assertEquals("user1 newpassword off-line", users.get(0));
    }

    @Test
    public void testChangePasswordIncorrectOldPassword() {
        // Test changing password with incorrect old password
        UserRegistration.signup("user1", "password1");
        String result = UserRegistration.changePassword("user1", "wrongpassword", "newpassword");
        assertEquals("密码错误", result); // Translation: "Incorrect password"
    }

    @Test
    public void testChangePasswordUserNotExists() {
        // Test changing password for a user that does not exist
        String result = UserRegistration.changePassword("user1", "password1", "newpassword");
        assertEquals("用户不存在", result); // Translation: "User does not exist"
    }
}
