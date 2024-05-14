package com.smoker.mahjong.impl;

import com.smoker.mahjong.data.*;
import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.*;

import java.util.Scanner;


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

    public int deal(String name) {
        // 抓一张牌
        return tileLibrary.deal(name).getId();
    }

    public void discard(String name, int tileID) {
        // 打一张牌
        findPlayer(name).getHandTile().removeTile(findTile(tileID));
    }

    public Player findPlayer(String name){
        for (Player player : gameRoom.getPlayerList()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public Tile findTile(int id){
        return tileLibrary.findTile(id);
    }

    public HandTile getHandTile(String name){
        return findPlayer(name).getHandTile();
    }

    public boolean canPang(String name, int tileID) {
        return findPlayer(name).getHandTile().canPang(tileID);
    }

    public boolean canKong(String name, int tileID) {
        return findPlayer(name).getHandTile().canKong(tileID);
    }

    public boolean canChow(String name, int tileID) {
        return true;
    }

    public boolean canHu(String name, int tileID) {
        return true;
    }

    public void Pang(String name, int tileID) {
        // 碰牌
        findPlayer(name).getHandTile().Pang(findTile(tileID));
    }

    public void Kong(String name, int tileID) {
        // 杠牌
        findPlayer(name).getHandTile().Kong(findTile(tileID));
    }

    public void Chow(String name, int tileID) {
        // 吃牌
    }

    public void Hu(String name, int tileID) {
        // 胡牌
    }

    public void printHand(String name){
        for (Tile tile : findPlayer(name).getHandTile().getHandTile()) {
            System.out.println(tile.getId());
        }
        for (Meld meld : findPlayer(name).getHandTile().getMelds()) {
            for (Tile tile : meld.getMeld()){
                System.out.print(tile.getId() + " ");
            }
            System.out.println();
        }
    }



    public static void main(String[] args) {
        GameStart gameStart = new GameStart();
        gameStart.addPlayer("songhao");
        gameStart.addPlayer("666");
        gameStart.addPlayer("123");
        gameStart.addPlayer("butterfly");


        System.out.println(gameStart.startGame());
        HandTile handTile = gameStart.getHandTile("butterfly");
//        for (Tile tile : handTile.getHandTile()){
//            System.out.println(tile.getId());
//        }
        Scanner  scanner = new Scanner(System.in);
        System.out.println("name: ");
        String name = scanner.next();
        while (true){
            int tileID;
            System.out.println("Option: ");
            String option = scanner.next();


            switch (option) {
                case "print" -> gameStart.printHand(name);
                case "canPang" -> {
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    System.out.println(gameStart.canPang(name, tileID));
                }
                case "canKong" -> {
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    System.out.println(gameStart.canKong(name, tileID));
                }
                case "Pang" -> {
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    gameStart.Pang(name, tileID);
                }
                case "Kong" -> {
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    gameStart.Kong(name, tileID);
                }
                default -> {}
            }
        }
    }

}
