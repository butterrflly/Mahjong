package com.smoker.mahjong.doma.Game;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class represents a Meld in Mahjong, which is a set of tiles that can be
 * formed into specific combinations like Pong, Kong, and Chow. It also supports
 * hidden melds.
 */
public class Meld implements hide {
    private ArrayList<Tile> meld; // List of tiles in the meld
    private String type; // Type of meld (e.g., Pong, Kong, Chow)
    private boolean isHide; // Indicates if the meld is hidden

    /**
     * Constructor for Meld.
     * @param meld the list of tiles in the meld
     * @param type the type of meld
     * @param isHide whether the meld is hidden
     */
    public Meld(ArrayList<Tile> meld, String type, boolean isHide) {
        this.meld = meld;
        this.type = type;
        this.isHide = isHide;
    }

    /**
     * Returns the list of tiles in the meld.
     * @return the meld tiles
     */
    public ArrayList<Tile> getMeld() {
        return meld;
    }

    /**
     * Returns the meld tiles as an array of IDs.
     * @return the meld array
     */
    public int[] getMeldArray() {
        int[] meldArray = new int[meld.size()];
        for (int i = 0; i < meld.size(); i++) {
            meldArray[i] = meld.get(i).getId(); // Convert meld tiles to ID array
        }
        return meldArray;
    }

    /**
     * Opens the meld, making it no longer hidden.
     */
    public void open() {
        isHide = false; // Set the meld to be not hidden
    }

    /**
     * Returns whether the meld is hidden.
     * @return true if the meld is hidden, false otherwise
     */
    public boolean getHide() {
        return isHide;
    }

    /**
     * Returns the type of meld.
     * @return the type of meld
     */
    public String getType() {
        return type;
    }

    /**
     * Sorts the tiles in the meld by their IDs.
     */
    public void sort(){
        meld.sort(Comparator.comparing(Tile::getId)); // Sort the meld tiles by ID
    }
}
