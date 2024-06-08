package com.smoker.mahjong.data;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class handles user registration, login, logout, and other user-related operations.
 * It simulates a database by reading and writing user data to a text file.
 */
public class UserRegistration {

    // Filename for storing user information
    private static String filename = "users.txt";

    /**
     * Registers a new user with the given name and password.
     * @param name the name of the user
     * @param password the password of the user
     * @return a message indicating the result of the registration
     */
    public static String signup(String name, String password) {
        ArrayList<String> users = readFile(filename);
        for (String user : users) {
            String[] userInfo = user.split(" ");
            if (Objects.equals(userInfo[0], name)) {
                return "用户已存在"; // Translation: "User already exists"
            }
        }

        ArrayList<String> newUser = new ArrayList<>();
        newUser.add(name + " " + password + " off-line");
        writeFile(filename, newUser, true);

        return "注册成功"; // Translation: "Registration successful"
    }

    /**
     * Logs in a user with the given name and password.
     * @param name the name of the user
     * @param password the password of the user
     * @return a message indicating the result of the login
     */
    public static String login(String name, String password) {
        ArrayList<String> users = readFile(filename);
        for (String user : users) {
            String[] userInfo = user.split(" ");
            if (Objects.equals(userInfo[0], name)) {
                if (!Objects.equals(userInfo[1], password)) {
                    return "密码错误"; // Translation: "Incorrect password"
                }
                if (Objects.equals(userInfo[2], "on-line")){
                    return "用户已登录"; // Translation: "User already logged in"
                }

                users.remove(user);
                users.add(name + " " + password + " on-line");
                writeFile(filename, users, false);

                return "登录成功"; // Translation: "Login successful"
            }
        }
        return "用户不存在"; // Translation: "User does not exist"
    }

    /**
     * Logs out a user with the given name.
     * @param name the name of the user
     * @return a message indicating the result of the logout
     */
    public static String logout(String name) {
        ArrayList<String> users = readFile(filename);
        for (String user : users) {
            String[] userInfo = user.split(" ");
            if (Objects.equals(userInfo[0], name)) {
                if (Objects.equals(userInfo[2], "off-line")){
                    return "用户未登录"; // Translation: "User not logged in"
                }

                users.remove(user);
                users.add(name + " " + userInfo[1] + " off-line");
                writeFile(filename, users, false);
                return "退出成功"; // Translation: "Logout successful"
            }
        }
        return "用户不存在"; // Translation: "User does not exist"
    }

    /**
     * Deletes a user with the given name and password.
     * @param name the name of the user
     * @param password the password of the user
     * @return a message indicating the result of the deletion
     */
    public static String deleteUser(String name, String password) {
        ArrayList<String> users = readFile(filename);
        for (String user : users) {
            String[] userInfo = user.split(" ");
            if (Objects.equals(userInfo[0], name)) {
                if (!Objects.equals(userInfo[1], password)) {
                    return "密码错误"; // Translation: "Incorrect password"
                }
                // Delete the user
                users.remove(user);
                writeFile(filename, users, false);
                return "删除成功"; // Translation: "Deletion successful"
            }
        }
        return "用户不存在"; // Translation: "User does not exist"
    }

    /**
     * Changes the password of a user with the given name.
     * @param name the name of the user
     * @param oldPassword the current password of the user
     * @param newPassword the new password of the user
     * @return a message indicating the result of the password change
     */
    public static String changePassword(String name, String oldPassword, String newPassword) {
        ArrayList<String> users = readFile(filename);
        for (String user : users) {
            String[] userInfo = user.split(" ");
            if (Objects.equals(userInfo[0], name)) {
                if (!Objects.equals(userInfo[1], oldPassword)) {
                    return "密码错误"; // Translation: "Incorrect password"
                }
                // Change the password
                users.remove(user);
                users.add(name + " " + newPassword + " " + userInfo[2]);
                writeFile(filename, users, false);
                return "修改成功"; // Translation: "Password changed successfully"
            }
        }
        return "用户不存在"; // Translation: "User does not exist"
    }

    /**
     * Reads the user data from the file.
     * @param filename the name of the file to read from
     * @return a list of strings representing the user data
     */
    public static ArrayList<String> readFile(String filename){
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Writes the user data to the file.
     * @param filename the name of the file to write to
     * @param lines the list of strings representing the user data
     * @param append whether to append to the file or overwrite it
     */
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

    /**
     * Gets the filename for storing user information.
     * @return the filename
     */
    public static String getFilename() {
        return filename;
    }

    /**
     * Sets the filename for storing user information.
     * @param filename the filename to set
     */
    public static void setFilename(String filename) {
        UserRegistration.filename = filename;
    }
}
