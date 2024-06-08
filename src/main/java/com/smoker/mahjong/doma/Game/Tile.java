package com.smoker.mahjong.doma.Game;

/**
 * This class represents a Tile in Mahjong. A tile has an ID, suit, number, and a hidden state.
 * The ID uniquely identifies the tile, and it can be used to determine the suit and number.
 */
public class Tile {
    private int id; // The unique identifier for the tile
    private String suit; // The suit of the tile (e.g., Wan, Tiao, Bing)
    private String num; // The number or type of the tile
    private boolean isHidden; // Whether the tile is hidden

    /**
     * Constructor for Tile.
     * @param id the unique identifier for the tile
     * @param isHidden whether the tile is hidden
     */
    public Tile(int id, boolean isHidden) {
        this.id = id;
        this.isHidden = isHidden;
    }

    /**
     * Gets the ID of the tile.
     * @return the ID of the tile
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the suit of the tile.
     * @return the suit of the tile
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Gets the number or type of the tile.
     * @return the number or type of the tile
     */
    public String getNum() {
        return num;
    }

    /**
     * Returns whether the tile is hidden.
     * @return true if the tile is hidden, false otherwise
     */
    public boolean isHidden() {
        return isHidden;
    }
}
