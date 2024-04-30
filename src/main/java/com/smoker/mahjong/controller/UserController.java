package com.smoker.mahjong.controller;

import com.smoker.mahjong.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUser(@RequestParam("name") String name,
                          @RequestParam("password") String password)
    {
        return Result.okGetString();
    }

}
