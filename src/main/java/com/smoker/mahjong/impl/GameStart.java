package com.smoker.mahjong.impl;

import com.smoker.mahjong.data.*;
import com.smoker.mahjong.doma.User.*;


public class GameStart {

    private GameRoom gameRoom;

    public GameStart() {
        gameRoom = new GameRoom();
    }

    public String startGame() {
        return gameRoom.startGame();
    }

    public String addPlayer(String name) {
        return gameRoom.addPlayer(name);
    }

    public String removePlayer(String name) {
        return gameRoom.removePlayer(name);
    }

}
