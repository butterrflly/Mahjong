package com.smoker.mahjong.utils;

import com.alibaba.fastjson2.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Result {

    public static String okGetString() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "ok");
        String json = JSONObject.toJSONString(map);
        return json;
    }
}
