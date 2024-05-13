package com.smoker.mahjong.impl;

import com.smoker.mahjong.data.*;
import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.*;


public class GameStart {

    private GameRoom gameRoom;
    private TileLibrary tileLibrary;

    public GameStart() {
        gameRoom = new GameRoom();
    }

    public String startGame() {
        if (gameRoom.getPlayerNum() < 4) {
            return "人数不足";
        }
        tileLibrary = new TileLibrary(gameRoom.getPlayerList());
        return "游戏开始";
    }

    public String addPlayer(String name) {
        return gameRoom.addPlayer(name);
    }

    public String removePlayer(String name) {
        return gameRoom.removePlayer(name);
    }

    public Tile deal(String name) {
        // 抓一张牌
        return tileLibrary.deal(name);
    }

    public void discard(String name, Tile tile) {
        // 打一张牌
        findPlayer(name).getHandTile().removeTile(tile);
    }

    public Player findPlayer(String name){
        for (Player player : gameRoom.getPlayerList()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public HandTile getHandTile(String name){
        return findPlayer(name).getHandTile();
    }

    public static void main(String[] args) {
        GameStart gameStart = new GameStart();
        gameStart.addPlayer("songhao");
        gameStart.addPlayer("666");
        gameStart.addPlayer("123");
        gameStart.addPlayer("butterfly");


        System.out.println(gameStart.startGame());
        HandTile handTile = gameStart.getHandTile("butterfly");
        for (Tile tile : handTile.getHandTile()){
            System.out.println(tile.getId());
        }
    }

}
