package com.smoker.mahjong.data;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class UserRegistration {
    // 模拟数据库储存用户信息

    public void register(String name, String password) {}

    public String login(String name, String password) {
        return null;
    }

    public void deleteUser(String name) {}



    public ArrayList<String> readFile(String filename){
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e){}
        return lines;
    }
}
