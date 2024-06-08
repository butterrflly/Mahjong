package com.smoker.mahjong.data;

import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.Player;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents the tile library for the Mahjong game, managing the creation, shuffling, and dealing of tiles.
 */
public class TileLibrary {
    // Stores the list of players
    private ArrayList<Player> playerList;
    // Stores the tiles to be dealt
    private ArrayList<Tile> tiles;
    // Stores all the tiles initially created
    private ArrayList<Tile> totalTiles;
    // Stores the tiles on the table
    private ArrayList<Tile> tableTiles;

    /**
     * Constructor for TileLibrary.
     * Initializes the tile library with the given player list, creates tiles, shuffles them, and deals the initial hands.
     * @param playerList - The list of players.
     */
    public TileLibrary(ArrayList<Player> playerList) {
        this.tableTiles = new ArrayList<>();
        this.playerList = playerList;
        tiles = new ArrayList<>();
        createTiles(); // Create initial set of tiles
        shuffle(tiles); // Shuffle the tiles
        dealInitialHand(); // Deal the initial hand to players
    }

    /**
     * Creates the initial set of tiles.
     * Each tile is identified by a unique three-digit ID.
     * Hundreds place indicates tile type: 1-Wan, 2-Tiao, 3-Bing, 4-Wind tiles, 5-Character tiles.
     * Tens place indicates tile value.
     * Units place indicates which of the four identical tiles it is.
     */
    public void createTiles() {
        for (int suit = 1; suit <= 5; suit++) {
            int figure = 9;
            if (suit == 4) figure = 4; // Wind tiles
            if (suit == 5) figure = 3; // Character tiles
            for (int num = 1; num <= figure; num++) {
                for (int index = 1; index <= 4; index++) {
                    tiles.add(new Tile(suit * 100 + num * 10 + index, true)); // Add each tile to the list
                }
            }
        }
        totalTiles = new ArrayList<>(tiles); // Copy all tiles to totalTiles
    }

    /**
     * Shuffles the tiles.
     * @param tiles - The list of tiles to shuffle.
     */
    public void shuffle(ArrayList<Tile> tiles) {
        Collections.shuffle(tiles); // Shuffle the tiles
    }

    /**
     * Deals the initial hand to each player.
     * Each player receives 13 tiles.
     */
    public void dealInitialHand() {
        for (Player player : playerList) {
            player.cleanHandTile(); // Clean player's hand
            for (int i = 0; i < 13; i++) {
                player.addInitialHand(tiles.remove(0)); // Deal 13 tiles to each player
            }
        }
    }

    /**
     * Deals one tile to the specified player.
     * @param name - The name of the player.
     * @return The dealt tile.
     */
    public Tile deal(String name) {
        for (Player player : playerList) {
            if (player.getName().equals(name)) {
                return player.getHandTile().addTile(tiles.remove(0)); // Deal one tile to the player
            }
        }
        return null; // If player not found, return null
    }

    /**
     * Finds a tile by its ID.
     * @param id - The ID of the tile.
     * @return The tile if found, else null.
     */
    public Tile findTile(int id) {
        for (Tile tile : totalTiles) {
            if (tile.getId() == id) {
                return tile; // Return the tile if found
            }
        }
        return null; // If tile not found, return null
    }

    /**
     * Adds a tile to the table tiles.
     * @param tile - The tile to add.
     */
    public void addTableTile(Tile tile) {
        tableTiles.add(tile); // Add the tile to the table tiles
    }

    /**
     * Gets the tiles on the table.
     * @return The list of table tiles.
     */
    public ArrayList<Tile> getTableTile() {
        return tableTiles; // Return the list of table tiles
    }

    /**
     * Gets the remaining tiles.
     * @return The list of remaining tiles.
     */
    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    /**
     * Gets the initial tiles.
     * @return The list of initial tiles.
     */
    public ArrayList<Tile> getTotalTiles() {
        return totalTiles;
    }
}
