package com.smoker.mahjong.data;


import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class UserRegistration {
    // 模拟数据库储存用户信息

    private static String filename = "users.txt";

    public static String register(String name, String password) {
        ArrayList<String> users = readFile(filename);
        for (String user : users) {
            String[] userInfo = user.split(" ");
            if (Objects.equals(userInfo[0], name)) {
                return "用户已存在";
            }
        }

        ArrayList<String> newUser = new ArrayList<>();

        newUser.add(name + " " + password);

        writeFile(filename, newUser, true);

        return "注册成功";
    }

    public static String login(String name, String password) {
        ArrayList<String> users = readFile(filename);
        for (String user : users) {
            String[] userInfo = user.split(" ");
            if (Objects.equals(userInfo[0], name)) {
                if (!Objects.equals(userInfo[1], password)) {
                    return "密码错误";
                }
                return "登录成功";
            }
        }
        return  "用户不存在";
    }

    public static String deleteUser(String name, String password) {
        ArrayList<String> users = readFile(filename);
        for (String user : users) {
            String[] userInfo = user.split(" ");
            if (Objects.equals(userInfo[0], name)) {
                if (!Objects.equals(userInfo[1], password)) {
                    return "密码错误";
                }
                // 删除操作
                users.remove(user);
                writeFile(filename, users, false);
                return "删除成功";
            }
        }
        return  "用户不存在";
    }

    public static String changePassword(String name, String oldPassword, String newPassword) {
        ArrayList<String> users = readFile(filename);
        for (String user : users) {
            String[] userInfo = user.split(" ");
            if (Objects.equals(userInfo[0], name)) {
                if (!Objects.equals(userInfo[1], oldPassword)) {
                    return "密码错误";
                }
                // 修改操作
                users.remove(user);
                users.add(name + " " + newPassword);
                writeFile(filename, users, false);
                return "修改成功";
            }
        }
        return  "用户不存在";
    }



    public static ArrayList<String> readFile(String filename){
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

    public static void writeFile(String filename, ArrayList<String> lines, boolean append) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, append))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
