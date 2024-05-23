package com.smoker.mahjong.service;

import com.alibaba.fastjson2.JSONObject;
import java.util.HashMap;
import java.util.Map;
import com.smoker.mahjong.data.UserRegistration;
import org.springframework.stereotype.Service;

@Service // 注解：标记当前是个service类，并注入到spring容器中
public class UserService {
    public String signup(String name, String password) {
        String result = UserRegistration.signup(name, password);
        return handleResult(result);
    }

    public String login(String name, String password) {
        String result = UserRegistration.login(name, password);
        return handleResult(result);
    }

    public String deleteUser(String name, String password) {
        String result = UserRegistration.deleteUser(name, password);
        return handleResult(result);
    }

    public String changePassword(String name, String oldPassword, String newPassword) {
        String result = UserRegistration.changePassword(name, oldPassword, newPassword);
        return handleResult(result);
    }

    public String handleResult(String result) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "ok");
        map.put("data", result);
        return JSONObject.toJSONString(map);
    }
}
