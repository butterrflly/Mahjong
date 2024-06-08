package com.smoker.mahjong.impl;

import com.smoker.mahjong.data.*;
import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.*;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class represents the main game logic for a Mahjong game.
 * It handles game initialization, player actions, and scoring.
 */
public class GameStarter {
    private GameRoom gameRoom; // The game room containing players
    private TileLibrary tileLibrary; // The tile library for the game
    private boolean gaming = false; // Indicates if the game is currently ongoing
    private String roomID; // The ID of the game room
    private Player banker; // The current banker
    private Player winner; // The winner of the current game
    private ArrayList<Player> loser; // The losers of the current game
    private String dealPlayName; // The name of the player dealing the tile
    private String discardName; // The name of the player discarding the tile
    private int dealTileID; // The ID of the dealt tile
    private int discardTileID; // The ID of the discarded tile
    private int HuType = 1; // The type of Hu
    private int KongNum = 1; // The number of Kongs
    private String KongName = ""; // The name of the player who Konged

    /**
     * Constructor for GameStarter.
     * @param roomID the ID of the game room
     */
    public GameStarter(String roomID) {
        gameRoom = new GameRoom();
        this.roomID = roomID;
    }

    /**
     * Starts the game if there are enough prepared players.
     * @return true if the game started, false otherwise
     */
    public boolean startGame() {
        if (gameRoom.getPlayerNum() < 4) { // Check if there are enough players
            return false;
        }
        ArrayList<Player> playerList = gameRoom.getPlayerList();
        for (Player player : playerList){
            if (!player.isPrepare()){ // Check if all players are prepared
                return false;
            }
        }
        tileLibrary = new TileLibrary(gameRoom.getPlayerList());
        gaming = true;
        HuType = 1;
        setBanker(); // Set the banker
        return true;
    }

    /**
     * Ends the game and calculates the score for each player.
     */
    public void gameOver() {
        gaming = false;
        for(Player player : gameRoom.getPlayerList()){
            player.scoring(banker, winner, loser, HuType); // Calculate the score for each player
            player.setPrepare(); // Reset the preparation state
        }
    }

    /**
     * Adds a player to the game room.
     * @param name the name of the player
     * @return a message indicating the result of adding the player
     */
    public String addPlayer(String name) {
        return gameRoom.addPlayer(name);
    }

    /**
     * Removes a player from the game room.
     * @param name the name of the player
     * @return a message indicating the result of removing the player
     */
    public String removePlayer(String name) {
        banker = null;
        winner = null;
        loser = null;
        return gameRoom.removePlayer(findPlayer(name));
    }

    /**
     * Gets the list of players in the game room.
     * @return the list of players
     */
    public ArrayList<Player> getPlayerList() {
        return gameRoom.getPlayerList();
    }

    /**
     * Gets the number of players in the game room.
     * @return the number of players
     */
    public int getPlayerNum() {
        return gameRoom.getPlayerNum();
    }

    /**
     * Gets the player who is currently dealing a tile.
     * @return the player who is dealing
     */
    public Player getDealPlayer(){
        return findPlayer(dealPlayName);
    }

    /**
     * Gets the ID of the tile being dealt.
     * @return the ID of the dealt tile
     */
    public int getDealTileID(){
        return dealTileID;
    }

    /**
     * Gets the name of the player who discarded a tile.
     * @return the name of the player who discarded
     */
    public String getDiscardPlayerName(){
        return discardName;
    }

    /**
     * Gets the ID of the discarded tile.
     * @return the ID of the discarded tile
     */
    public int getDiscardTileID(){
        return discardTileID;
    }

    /**
     * Gets the hand tiles of a player.
     * @param name the name of the player
     * @return the hand tiles of the player
     */
    public HandTile getHandTile(String name){
        return findPlayer(name).getHandTile();
    }

    /**
     * Sets the banker for the game.
     */
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
        banker = players.get((i + 1) % 4); // Set the next player as the banker

        dealPlayName = null;
    }

    /**
     * Gets the name of the banker.
     * @return the name of the banker
     */
    public String getBanker(){
        return banker.getName();
    }

    /**
     * Deals a tile to the next player.
     * @return the ID of the dealt tile
     */
    public int deal() {
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

    /**
     * Discards a tile from a player's hand.
     * @param name the name of the player
     * @param tileID the ID of the tile to discard
     * @return the ID of the removed tile
     */
    public int discard(String name, int tileID) {
        discardName = name;
        discardTileID = tileID;

        return findPlayer(name).getHandTile().removeTile(findTile(tileID));
    }

    /**
     * Checks if a player can form a Pong with a tile.
     * @param name the name of the player
     * @param tileID the ID of the tile
     * @return true if a Pong can be formed, false otherwise
     */
    public boolean canPang(String name, int tileID) {
        return findPlayer(name).getHandTile().canPang(tileID);
    }

    /**
     * Checks if a player can form a Kong with a tile.
     * @param name the name of the player
     * @param tileID the ID of the tile
     * @return true if a Kong can be formed, false otherwise
     */
    public boolean canKong(String name, int tileID) {
        return findPlayer(name).getHandTile().canKong(tileID);
    }

    /**
     * Checks if a player can form a Kong with any tile.
     * @param name the name of the player
     * @return a list of tiles that can form a Kong
     */
    public ArrayList<Tile> canKong(String name) {
        return findPlayer(name).getHandTile().canKong();
    }

    /**
     * Checks if a player can form a Chow with a tile.
     * @param name the name of the player
     * @param tileID the ID of the tile
     * @return a list of possible Chow combinations
     */
    public ArrayList<ArrayList<Tile>> canChow(String name, int tileID) {
        return findPlayer(name).getHandTile().canChow(findTile(tileID));
    }

    /**
     * Checks if a player can form a Hu with a tile.
     * @param name the name of the player
     * @param tileID the ID of the tile
     * @return true if a Hu can be formed, false otherwise
     */
    public boolean canHu(String name, int tileID) {
        return findPlayer(name).getHandTile().canHu(findTile(tileID)) != 0;
    }

    /**
     * Forms a Pong with a tile for a player.
     * @param name the name of the player
     * @param tileID the ID of the tile
     */
    public void Pang(String name, int tileID) {
        findPlayer(name).getHandTile().Pang(findTile(tileID));
        dealPlayName = name;

        KongName = "";
        KongNum = 1;
    }

    /**
     * Forms a Kong with a tile for a player.
     * @param name the name of the player
     * @param tileID the ID of the tile
     */
    public void Kong(String name, int tileID) {
        findPlayer(name).getHandTile().Kong(findTile(tileID));
        dealPlayName = getSequence(name)[3].getName();

        if(!KongName.equals(name)){
            KongName = "";
            KongNum = 1;
        }

        KongNum *= 2;
        KongName = name;
    }

    /**
     * Forms a Chow with tiles for a player.
     * @param chowTiles the tiles to form the Chow with
     * @param name the name of the player
     * @param tileID the ID of the tile
     */
    public void Chow(ArrayList<Tile> chowTiles, String name, int tileID) {
        findPlayer(name).getHandTile().Chow(chowTiles, findTile(tileID));
        dealPlayName = name;

        KongName = "";
        KongNum = 1;
    }

    /**
     * Forms a Hu with a tile for a player.
     * @param winnerName the name of the winner
     * @param tileID the ID of the tile
     * @param loserName the name of the loser
     */
    public void Hu(String winnerName, int tileID, String loserName) {
        winner = findPlayer(winnerName);
        HuType = winner.getHandTile().Hu(findTile(tileID));
        loser = new ArrayList<>();
        if (loserName.equals("allPlayers")) {
            for (Player player : gameRoom.getPlayerList()){
                if (player != winner){
                    loser.add(player);
                }
            }

            if (tileLibrary.getTiles().size() < 16) {
                HuType *= 2;
            }
        }
        else {
            loser.add(findPlayer(loserName));
        }

        HuType *= KongNum;

        System.out.println("KongNum" + "  :  " + KongNum);
        System.out.println(HuType);
        gameOver();
    }

    /**
     * Checks if the game is a draw.
     * @return true if the game is a draw, false otherwise
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

    /**
     * Gets the sequence of players starting from a given player.
     * @param name the name of the starting player
     * @return an array of players in sequence
     */
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

    /**
     * Finds a player by name.
     * @param name the name of the player
     * @return the player if found, null otherwise
     */
    public Player findPlayer(String name){
        for (Player player : gameRoom.getPlayerList()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Finds a tile by ID.
     * @param id the ID of the tile
     * @return the tile if found
     */
    public Tile findTile(int id){
        return tileLibrary.findTile(id);
    }

    /**
     * Adds a tile to the table.
     */
    public void addTableTile(){
        tileLibrary.addTableTile(findTile(discardTileID));
    }

    /**
     * Gets the tiles on the table.
     * @return the list of table tiles
     */
    public ArrayList<Tile> getTableTile(){
        return tileLibrary.getTableTile();
    }

    /**
     * Prints the hand tiles and melds of a player.
     * @param name the name of the player
     */
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

    /**
     * Gets the ID of the game room.
     * @return the ID of the game room
     */
    public String getRoomID(){
        return roomID;
    }

//    /**
//     * Main method to run the game from the console.
//     * @param args command line arguments
//     */
//    public static void main(String[] args) {
//        GameStarter gameStarter = new GameStarter("butterfly");
//        gameStarter.addPlayer("songhao");
//        gameStarter.findPlayer("songhao").setPrepare();
//        gameStarter.addPlayer("666");
//        gameStarter.findPlayer("666").setPrepare();
//        gameStarter.addPlayer("123");
//        gameStarter.findPlayer("123").setPrepare();
//        gameStarter.addPlayer("butterfly");
//        gameStarter.findPlayer("butterfly").setPrepare();
//
//        System.out.println(gameStarter.startGame());
//        HandTile handTile = gameStarter.getHandTile("butterfly");
//
//        Scanner scanner = new Scanner(System.in);
//        String name = "butterfly";
//        ArrayList<ArrayList<Tile>> chowTiless;
//        while (true){
//            int tileID;
//            System.out.println("Option: ");
//            String option = scanner.next();
//
//            switch (option) {
//                case "print" -> gameStarter.printHand(name);
//                case "canPang" -> {
//                    System.out.println("tileID: ");
//                    tileID = scanner.nextInt();
//                    System.out.println(gameStarter.canPang(name, tileID));
//                }
//                case "canKong" -> {
//                    System.out.println("tileID: ");
//                    tileID = scanner.nextInt();
//                    System.out.println(gameStarter.canKong(name, tileID));
//                }
//                case "canChow" -> {
//                    System.out.println("tileID: ");
//                    tileID = scanner.nextInt();
//                    chowTiless = gameStarter.canChow(name, tileID);
//                    for (ArrayList<Tile> chowTiles : chowTiless) {
//                        for (Tile tile : chowTiles) {
//                            System.out.print(tile.getId() + " ");
//                        }
//                        System.out.println();
//                    }
//                }
//                case "canHu" -> {
//                    int[] exTile = {
//                            311,
//                            312,
//                            313,
//                            321,
//                            331,
//                            341,
//                            351,
//                            361,
//                            371,
//                            381,
//                            391,
//                            122,
//                            314,
//                    };
//                    handTile.cleanTile();
//                    for (int i : exTile) {
//                        handTile.addTile(gameStarter.findTile(i));
//                    }
//
//                    gameStarter.printHand(name);
//                    System.out.println("tileID: ");
//                    tileID = scanner.nextInt();
//                    System.out.println(gameStarter.canHu(name, tileID));
//                    gameStarter.Hu("butterfly", tileID, "666");
//                }
//                case "Pang" -> {
//                    System.out.println("tileID: ");
//                    tileID = scanner.nextInt();
//                    gameStarter.Pang(name, tileID);
//                    gameStarter.printHand(name);
//                }
//                case "Kong" -> {
//                    System.out.println("tileID: ");
//                    tileID = scanner.nextInt();
//                    gameStarter.Kong(name, tileID);
//                    gameStarter.printHand(name);
//                }
//                case "Chow" -> {
//                    System.out.println("tileID: ");
//                    tileID = scanner.nextInt();
//                    System.out.println("tileID1: ");
//                    int tileID1 = scanner.nextInt();
//                    System.out.println("tileID2: ");
//                    int tileID2 = scanner.nextInt();
//                    System.out.println("tileID3: ");
//                    int tileID3 = scanner.nextInt();
//                    ArrayList<Tile> chowTiles = new ArrayList<>();
//                    chowTiles.add(gameStarter.findTile(tileID1));
//                    chowTiles.add(gameStarter.findTile(tileID2));
//                    chowTiles.add(gameStarter.findTile(tileID3));
//                    gameStarter.Chow(chowTiles, name, tileID);
//                    gameStarter.printHand(name);
//                }
//                default -> {}
//            }
//        }
//    }
}
