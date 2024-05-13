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

    public int getPlayerNum() {
        return playerList.size();
    }

    public ArrayList<Player> getPlayerList() {
        return playerList;
    }
}
