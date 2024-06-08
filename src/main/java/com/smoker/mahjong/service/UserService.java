package com.smoker.mahjong.service;

import com.alibaba.fastjson2.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.smoker.mahjong.data.UserRegistration;
import org.springframework.stereotype.Service;

@Service // Annotation marks this class as a service and injects it into the Spring container
public class UserService {

    /**
     * Handles user signup
     * @param name - The username
     * @param password - The password
     * @return JSON string with the signup result
     */
    public String signup(String name, String password) {
        String result = UserRegistration.signup(name, password);
        return handleResult(result);
    }

    /**
     * Handles user login
     * @param name - The username
     * @param password - The password
     * @return JSON string with the login result
     */
    public String login(String name, String password) {
        String result = UserRegistration.login(name, password);
        return handleResult(result);
    }

    /**
     * Handles user logout
     * @param name - The username
     * @return JSON string with the logout result
     */
    public String logout(String name) {
        String result = UserRegistration.logout(name);
        return handleResult(result);
    }

    /**
     * Handles user deletion
     * @param name - The username
     * @param password - The password
     * @return JSON string with the delete user result
     */
    public String deleteUser(String name, String password) {
        String result = UserRegistration.deleteUser(name, password);
        return handleResult(result);
    }

    /**
     * Handles changing the user's password
     * @param name - The username
     * @param oldPassword - The current password
     * @param newPassword - The new password
     * @return JSON string with the change password result
     */
    public String changePassword(String name, String oldPassword, String newPassword) {
        String result = UserRegistration.changePassword(name, oldPassword, newPassword);
        return handleResult(result);
    }

    /**
     * Formats the result into a standardized JSON string
     * @param result - The result to format
     * @return JSON string containing the formatted result
     */
    public String handleResult(String result) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200); // Add a status code to the result map
        map.put("msg", "ok"); // Add a message to the result map
        map.put("data", result); // Add the actual result to the result map
        return JSONObject.toJSONString(map);
    }
}
