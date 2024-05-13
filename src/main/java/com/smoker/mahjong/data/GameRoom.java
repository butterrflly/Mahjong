package com.smoker.mahjong.data;


import com.smoker.mahjong.doma.User.Player;
import java.util.ArrayList;

public class GameRoom {
    // 储存游戏信息
    private ArrayList<Player> playerList;
    private TileLibrary tileLibrary;

    public GameRoom() {
        playerList = new ArrayList<Player>();
    }

    public String startGame() {
        if (playerList.size() < 4) {
            return "人数不足";
        }
        tileLibrary = new TileLibrary(playerList);
        return "游戏开始";
    }

    public String addPlayer(String player) {
        if  (playerList.contains(player)) {
            return "玩家已存在";
        }
        if (playerList.size() < 4){
            playerList.add(new  Player(player));
            return "添加成功";
        }
        return "房间已满";
    }


    public String removePlayer(String player) {
        if (playerList.contains(player)) {
            playerList.remove(player);
            return "移除成功";
        }
        return "玩家不存在";
    }

    public int playNum() {
        return playerList.size();
    }

    public TileLibrary getTileLibrary() {
        return tileLibrary;
    }
}
