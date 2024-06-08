package com.smoker.mahjong.controller;

import com.smoker.mahjong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * UserController class handles user-related operations such as signup, login, logout, delete user, and change password.
 * It uses UserService to perform these operations.
 */
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired // Injects the UserService from the Spring container
    private UserService userService;

    /**
     * Handles user signup.
     *
     * @param name the username
     * @param password the password
     * @return a message indicating the result of the signup operation
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@RequestParam("name") String name,
                         @RequestParam("password") String password) {
        return userService.signup(name, password);
    }

    /**
     * Handles user logout.
     *
     * @param name the username
     * @return a message indicating the result of the logout operation
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(@RequestParam("name") String name) {
        return userService.logout(name);
    }

    /**
     * Handles user login.
     *
     * @param name the username
     * @param password the password
     * @return a message indicating the result of the login operation
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("name") String name,
                        @RequestParam("password") String password) {
        return userService.login(name, password);
    }

    /**
     * Handles deleting a user.
     *
     * @param name the username
     * @param password the password
     * @return a message indicating the result of the delete operation
     */
    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUser(@RequestParam("name") String name,
                             @RequestParam("password") String password) {
        return userService.deleteUser(name, password);
    }

    /**
     * Handles changing a user's password.
     *
     * @param name the username
     * @param oldPassword the old password
     * @param newPassword the new password
     * @return a message indicating the result of the change password operation
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(@RequestParam("name") String name,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword) {
        return userService.changePassword(name, oldPassword, newPassword);
    }

}
