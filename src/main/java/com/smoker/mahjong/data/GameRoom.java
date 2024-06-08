package com.smoker.mahjong.data;

import com.smoker.mahjong.doma.User.Player;
import java.util.ArrayList;

/**
 * Represents a game room that stores game information and manages players.
 */
public class GameRoom {
    // Stores the list of players in the game room
    private ArrayList<Player> playerList;

    // Stores the tile library for the game room
    private TileLibrary tileLibrary;

    /**
     * Constructor for GameRoom.
     * Initializes an empty player list.
     */
    public GameRoom() {
        playerList = new ArrayList<Player>();
    }

    /**
     * Adds a player to the game room.
     * @param player - The name of the player to add.
     * @return A message indicating whether the player was successfully added or if the room is full.
     */
    public String addPlayer(String player) {
        if (playerList.size() < 4) { // Check if the room has less than 4 players
            playerList.add(new Player(player)); // Add the player to the list
            return "添加成功"; // Translation: "Added successfully"
        }
        return "房间已满"; // Translation: "Room is full"
    }

    /**
     * Removes a player from the game room.
     * @param player - The player to remove.
     * @return A message indicating whether the player was successfully removed or if the player does not exist.
     */
    public String removePlayer(Player player) {
        for (Player p : playerList) {
            if (p.getName().equals(player.getName())) { // Check if the player is in the list by name
                playerList.remove(p); // Remove the player from the list
                return "移除成功"; // Translation: "Removed successfully"
            }
        }
        return "玩家不存在"; // Translation: "Player does not exist"
    }

    /**
     * Gets the number of players in the game room.
     * @return The number of players.
     */
    public int getPlayerNum() {
        return playerList.size(); // Return the size of the player list
    }

    /**
     * Gets the list of players in the game room.
     * @return The list of players.
     */
    public ArrayList<Player> getPlayerList() {
        return playerList; // Return the player list
    }
}
