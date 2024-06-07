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
    private String roomID;
    private Player banker;
    private Player winner;
    private ArrayList<Player> loser;
    private String dealPlayName;
    private String discardName;
    private int dealTileID;
    private int discardTileID;

    private int HuType = 1;
    private int KongNum = 1;
    private String KongName = "";




    public GameStarter(String roomID) {
        gameRoom = new GameRoom();
        this.roomID = roomID;
    }

    public boolean startGame() {
        if (gameRoom.getPlayerNum() < 4) {
            return false;
        }
        ArrayList<Player> playerList = gameRoom.getPlayerList();
        for (Player player : playerList){
            if (!player.isPrepare()){
                return false;
            }
        }
        tileLibrary = new TileLibrary(gameRoom.getPlayerList());
        gaming = true;
        HuType = 1;
        setBanker();
        return true;
    }

    public void gameOver() {
        gaming = false;
        for(Player player : gameRoom.getPlayerList()){
            player.scoring(banker, winner, loser, HuType);
            player.setPrepare();
        }
    }

    public String addPlayer(String name) {
        return gameRoom.addPlayer(name);
    }

    public String removePlayer(String name) {
        banker = null;
        winner = null;
        loser = null;
        return gameRoom.removePlayer(findPlayer(name));
    }

    public ArrayList<Player> getPlayerList() {
        return gameRoom.getPlayerList();
    }

    public int getPlayerNum() {
        return gameRoom.getPlayerNum();
    }

    public Player getDealPlayer(){
        return findPlayer(dealPlayName);
    }

    public int getDealTileID(){
        return dealTileID;
    }

    public String getDiscardPlayerName(){
        return discardName;
    }

    public int getDiscardTileID(){
        return discardTileID;
    }

    public HandTile getHandTile(String name){
        return findPlayer(name).getHandTile();
    }


    public void setBanker(){
        ArrayList<Player> players = gameRoom.getPlayerList();

        if (banker == null){
            banker = players.get(0);
            return;
        }

        if (winner == banker || winner == null){
            return;
        }

        int i = players.indexOf(banker);
        banker = players.get((i + 1) % 4);

        dealPlayName = null;
    }

    public String getBanker(){
        return banker.getName();
    }


    public int deal() {
        // 抓一张牌
        if (dealPlayName == null) dealPlayName = banker.getName();
        else dealPlayName = getSequence(dealPlayName)[1].getName();

        dealTileID = tileLibrary.deal(dealPlayName).getId();
        discardName = null;
        discardTileID = 0;

        if(!KongName.equals(dealPlayName)){
            KongName = "";
            KongNum = 1;
        }

        return dealTileID;
    }

    public int discard(String name,int tileID) {
        // 打一张牌
        discardName = name;
        discardTileID = tileID;

        return findPlayer(name).getHandTile().removeTile(findTile(tileID));
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
        return findPlayer(name).getHandTile().canHu(findTile(tileID)) != 0;
    }


    public void Pang(String name, int tileID) {
        // 碰牌
        findPlayer(name).getHandTile().Pang(findTile(tileID));
        dealPlayName = name;

        KongName = "";
        KongNum = 1;
    }

    public void Kong(String name, int tileID) {
        // 杠牌
        findPlayer(name).getHandTile().Kong(findTile(tileID));
        dealPlayName = getSequence(name)[3].getName();

        // 杠牌后，如果之前已经杠过，则需要更新杠的数量
        // 如果不是则初始化Kong
        if(!KongName.equals(name)){
            KongName = "";
            KongNum = 1;
        }

        KongNum *= 2;
        KongName = name;
    }

    public void Chow(ArrayList<Tile> chowTiles, String name, int tileID) {
        // 吃牌
        findPlayer(name).getHandTile().Chow(chowTiles, findTile(tileID));
        dealPlayName = name;

        KongName = "";
        KongNum = 1;
    }

    public void Hu(String winnerName, int tileID, String loserName) {
        // 胡牌
        winner = findPlayer(winnerName);
        HuType = winner.getHandTile().Hu(findTile(tileID));
        loser = new ArrayList<>();
        if (loserName.equals("allPlayers")) {
            for (Player player : gameRoom.getPlayerList()){
                if (player != winner){
                    loser.add(player);
                }
            }

            // 海底捞月
            if (tileLibrary.getTiles().size() < 16) {
                HuType *= 2;
            }
        }
        else {
            loser.add(findPlayer(loserName));
        }

        // 杠上开花
        HuType *= KongNum;

        System.out.println("KongNum" + "  :  " + KongNum);
        System.out.println(HuType);
        gameOver();
    }


    /**
     * 是否荒牌
     */
    public boolean canDraw(){
        if (tileLibrary.getTiles().size() < 16) {
            winner = null;
            loser = null;

            HuType = 1;
            KongName = "";
            KongNum = 1;

            gameOver();
            return true;
        }
        return false;
    }

    public Player[] getSequence(String name){
        ArrayList<Player> newPlayers = new ArrayList<>();
        ArrayList<Player> temp = gameRoom.getPlayerList();

        ArrayList<Player> players = new ArrayList<>(temp);

        while (players.size() < 4)
            players.add(null);
        int i = players.indexOf(findPlayer(name));
        for (int j = 0; j < 4; j++) {
            newPlayers.add(players.get(i % 4));
            i++;
        }
        return newPlayers.toArray(new Player[0]);
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

    public void addTableTile(){
        tileLibrary.addTableTile(findTile(discardTileID));
    }

    public ArrayList<Tile> getTableTile(){
        return tileLibrary.getTableTile();
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


    public String getRoomID(){
        return roomID;
    }


    public static void main(String[] args) {
        GameStarter gameStarter = new GameStarter("butterfly");
        gameStarter.addPlayer("songhao");
        gameStarter.findPlayer("songhao").setPrepare();
        gameStarter.addPlayer("666");
        gameStarter.findPlayer("666").setPrepare();
        gameStarter.addPlayer("123");
        gameStarter.findPlayer("123").setPrepare();
        gameStarter.addPlayer("butterfly");
        gameStarter.findPlayer("butterfly").setPrepare();


        System.out.println(gameStarter.startGame());
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
                            311,
                            312,
                            313,
                            321,
                            331,
                            341,
                            351,
                            361,
                            371,
                            381,
                            391,
                            122,
                            314,
                    };
                    handTile.cleanTile();
                    for (int i : exTile) {
                        handTile.addTile(gameStarter.findTile(i));
                    }

//                    ArrayList<Tile> tiles = new ArrayList<>();
//                    tiles.add(gameStarter.findTile(321));
//                    tiles.add(gameStarter.findTile(322));
//                    tiles.add(gameStarter.findTile(323));
//                    handTile.getMelds().add(new Meld(tiles, "Pang", false));
//
//                    tiles = new ArrayList<>();
//                    tiles.add(gameStarter.findTile(111));
//                    tiles.add(gameStarter.findTile(121));
//                    tiles.add(gameStarter.findTile(131));
//                    handTile.getMelds().add(new Meld(tiles, "Chow", false));

                    gameStarter.printHand(name);
                    System.out.println("tileID: ");
                    tileID = scanner.nextInt();
                    System.out.println(gameStarter.canHu(name, tileID));
                    gameStarter.Hu("butterfly", tileID, "666");
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
