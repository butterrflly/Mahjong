package com.smoker.mahjong.impl;

import com.smoker.mahjong.data.*;
import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.*;

import java.util.ArrayList;
import java.util.Scanner;


public class GameStarter {
    private GameRoom gameRoom;
    private TileLibrary tileLibrary;
    private boolean gaming = false;
    private String owner;
    private Player banker;
    private Player winner;
    private Player loser;





    public GameStarter(String owner) {
        gameRoom = new GameRoom();
        this.owner = owner;
    }

    public String startGame(String name) {
        if (gameRoom.getPlayerNum() < 4) {
            return "人数不足";
        }
        tileLibrary = new TileLibrary(gameRoom.getPlayerList());
        banker = findPlayer(name);
        gaming = true;
        return "游戏开始";
    }

    public void gameOver() {
        gaming = false;
        for(Player player : gameRoom.getPlayerList()){
            player.scoring(banker, winner, loser);
        }
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

    public int discard(String name,int tileID) {
        // 打一张牌
        return findPlayer(name).getHandTile().removeTile(findTile(tileID));
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

    public ArrayList<Tile> canKong(String name) {
        return findPlayer(name).getHandTile().canKong();
    }

    public ArrayList<ArrayList<Tile>> canChow(String name, int tileID) {
        return findPlayer(name).getHandTile().canChow(findTile(tileID));
    }

    public boolean canHu(String name, int tileID) {
        return findPlayer(name).getHandTile().canHu(findTile(tileID));
    }

    public void Pang(String name, int tileID) {
        // 碰牌
        findPlayer(name).getHandTile().Pang(findTile(tileID));
    }

    public void Kong(String name, int tileID) {
        // 杠牌
        findPlayer(name).getHandTile().Kong(findTile(tileID));
    }

    public void Chow(ArrayList<Tile> chowTiles, String name, int tileID) {
        // 吃牌
        findPlayer(name).getHandTile().Chow(chowTiles, findTile(tileID));
    }

    public void Hu(String winnerName, int tileID, String loserName) {
        // 胡牌
        winner = findPlayer(winnerName);
        loser = findPlayer(loserName);
        gameOver();
    }


    /**
     * 是否荒牌
     */
    public boolean canDraw(){
        if (tileLibrary.getTiles().size() < 16) {
            winner = null;
            loser = null;
            gameOver();
            return true;
        }
        return false;
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


    public String getOwner(){
        return owner;
    }


    public static void main(String[] args) {
        GameStarter gameStarter = new GameStarter("butterfly");
        gameStarter.addPlayer("songhao");
        gameStarter.addPlayer("666");
        gameStarter.addPlayer("123");
        gameStarter.addPlayer("butterfly");


        System.out.println(gameStarter.startGame("butterfly"));
        HandTile handTile = gameStarter.getHandTile("butterfly");
//        for (Tile tile : handTile.getHandTile()){
//            System.out.println(tile.getId());
//        }
        Scanner  scanner = new Scanner(System.in);
        //System.out.println("name: ");
        String name = "butterfly";
        ArrayList<ArrayList<Tile>> chowTiless;
        while (true){
            int tileID;
            System.out.println("Option: ");
            String option = scanner.next();
//            String option = "canHu";



            switch (option) {
                case "print" -> gameStarter.printHand(name);
                case "canPang" -> {
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    System.out.println(gameStarter.canPang(name, tileID));
                }
                case "canKong" -> {
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    System.out.println(gameStarter.canKong(name, tileID));
                }
                case "canChow" -> {
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    chowTiless = gameStarter.canChow(name, tileID);
                    for (ArrayList<Tile> chowTiles : chowTiless) {
                        for (Tile tile : chowTiles) {
                            System.out.print(tile.getId() + " ");
                        }
                        System.out.println();
                    }
                }
                case "canHu" -> {
                    int[] exTile = {
                            111,
                            122,
                            123,
                            124,
                            131,
                            132,
                            133,
                            141,
                            142,
                            143,
                            151,
                            152,
                            153
                    };
                    handTile.cleanTile();
                    for (int i : exTile) {
                        handTile.addTile(gameStarter.findTile(i));
                    }
                    gameStarter.printHand(name);
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    System.out.println(gameStarter.canHu(name, tileID));
                }
                case "Pang" -> {
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    gameStarter.Pang(name, tileID);
                    gameStarter.printHand(name);
                }
                case "Kong" -> {
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    gameStarter.Kong(name, tileID);
                    gameStarter.printHand(name);
                }
                case "Chow" -> {
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    System.out.println("tileID1: ");
                    int tileID1 = scanner.nextInt();
                    System.out.println("tileID2: ");
                    int tileID2 = scanner.nextInt();
                    System.out.println("tileID3: ");
                    int tileID3 = scanner.nextInt();
                    ArrayList<Tile> chowTiles = new ArrayList<>();
                    chowTiles.add(gameStarter.findTile(tileID1));
                    chowTiles.add(gameStarter.findTile(tileID2));
                    chowTiles.add(gameStarter.findTile(tileID3));
                    gameStarter.Chow(chowTiles, name, tileID);
                    gameStarter.printHand(name);
                }
                default -> {}
            }
        }
    }

}
