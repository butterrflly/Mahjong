package com.smoker.mahjong.doma.User;

import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import java.util.ArrayList;

/**
 * This class represents a player in the Mahjong game.
 * A player has a name, hand tiles, melds, a score, and a prepare state.
 */
public class Player {
    private int id; // The unique identifier for the player
    private String name; // The name of the player
    private HandTile handTile; // The hand tiles of the player
    private ArrayList<Meld> melds; // The melds of the player
    private int score; // The score of the player
    private boolean prepare; // The prepare state of the player

    /**
     * Constructor for Player.
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.handTile = new HandTile();
        this.melds = new ArrayList<Meld>();
        this.score = 5000; // Initialize score
        this.prepare = false;
    }

    /**
     * Clears the hand tiles and melds of the player.
     */
    public void cleanHandTile() {
        this.handTile = new HandTile();
        this.melds = new ArrayList<Meld>();
    }

    /**
     * Adds an initial hand tile and sorts the hand.
     * @param tile the tile to add
     */
    public void addInitialHand(Tile tile) {
        handTile.addTile(tile); // Add the tile to the hand
        handTile.sort(); // Sort the hand tiles
    }

    /**
     * Calculates the score for the player based on the game outcome.
     *
     * @param banker the banker player
     * @param winner the winner player, can be null
     * @param loser the loser players, can be null
     * @param HuType the type of Hu
     */
    public void scoring(Player banker, Player winner, ArrayList<Player> loser, int HuType) {
        int points = 100 * HuType;
        // If there's no winner, it's a draw
        if (winner == null) {
            return;
        }

        if (winner == this) {
            // If the player is the winner
            if (loser.size() == 1) {
                // If not self-drawn
                if (banker == this) {
                    // Banker small win
                    score += points * 8;
                } else {
                    // Non-banker small win
                    if (loser.get(0) == banker) {
                        // Banker discarded
                        score += points * 6;
                    } else {
                        // Non-banker discarded
                        score += points * 5;
                    }
                }
            } else {
                // Self-drawn win
                if (banker == this) {
                    // Banker self-drawn
                    score += points * 12;
                } else {
                    // Non-banker self-drawn
                    score += points * 8;
                }
            }
        } else {
            // If the player is a loser
            if (loser.contains(this)) {
                // If the player discarded or someone self-drawn
                points *= 2;
            }
            if (banker == this) {
                // If the player is the banker
                score -= points * 2;
            } else {
                // If the player is not the banker
                if (winner == banker) {
                    // Banker wins
                    score -= points * 2;
                } else {
                    // Non-banker wins
                    score -= points;
                }
            }
        }
    }

    /**
     * Gets the name of the player.
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the hand tiles of the player.
     * @return the hand tiles of the player
     */
    public HandTile getHandTile() {
        return handTile;
    }

    /**
     * Gets the score of the player.
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Checks if the player is prepared.
     * @return true if the player is prepared, false otherwise
     */
    public boolean isPrepare() {
        return prepare;
    }

    /**
     * Toggles the prepare state of the player.
     */
    public void setPrepare() {
        prepare = !prepare;
    }
}
