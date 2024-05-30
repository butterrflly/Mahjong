package com.smoker.mahjong.controller;

import com.smoker.mahjong.utils.Result;
import com.smoker.mahjong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://172.20.10.2:5173")
public class UserController {

    @Autowired // 引用容器中的service
    private UserService userService;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@RequestParam("name") String name,
                         @RequestParam("password") String password)
    {
        return userService.signup(name, password);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam("name") String name,
                        @RequestParam("password") String password)
    {
        // 注册逻辑
        return userService.login(name, password);
    }


    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public String deleteUser(@RequestParam("name") String name,
                             @RequestParam("password") String password)
    {
        return userService.deleteUser(name, password);
    }


    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(@RequestParam("name") String name,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword)
    {
        return userService.changePassword(name, oldPassword, newPassword);
    }

}
