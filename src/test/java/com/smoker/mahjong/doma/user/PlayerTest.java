package com.smoker.mahjong.doma.user;

import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Player class.
 */
public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        // Initialize a player with name "TestPlayer"
        player = new Player("TestPlayer");
    }

    @Test
    public void testGetName() {
        // Ensure getName method works correctly
        assertEquals("TestPlayer", player.getName());
    }

    @Test
    public void testGetHandTile() {
        // Ensure getHandTile method works correctly
        assertNotNull(player.getHandTile());
    }

    @Test
    public void testGetScore() {
        // Ensure getScore method works correctly
        assertEquals(5000, player.getScore());
    }

    @Test
    public void testIsPrepare() {
        // Ensure isPrepare method works correctly
        assertFalse(player.isPrepare());
    }

    @Test
    public void testSetPrepare() {
        // Ensure setPrepare method works correctly
        player.setPrepare();
        assertTrue(player.isPrepare());
        player.setPrepare();
        assertFalse(player.isPrepare());
    }

    @Test
    public void testCleanHandTile() {
        // Ensure cleanHandTile method works correctly
        player.getHandTile().addTile(new Tile(111, true)); // Add a tile to hand
        player.cleanHandTile();
        assertEquals(0, player.getHandTile().getHandTile().size());
    }

    @Test
    public void testAddInitialHand() {
        // Ensure addInitialHand method works correctly
        Tile tile = new Tile(111, true); // Create a tile
        player.addInitialHand(tile);
        assertEquals(1, player.getHandTile().getHandTile().size());
        assertEquals(111, player.getHandTile().getHandTile().get(0).getId());
    }

    @Test
    public void testScoring() {
        // Initialize banker, winner, and loser players
        Player banker = new Player("Banker");
        Player winner = new Player("Winner");
        ArrayList<Player> losers = new ArrayList<>();
        losers.add(new Player("Loser1"));

        // Store initial score for comparison
        int initialScore = player.getScore();

        // Test case where player is the winner and not self-drawn
        player.scoring(banker, player, losers, 1);
        // Expected score calculation: initialScore + (100 * 1 * 6)
        assertEquals(initialScore + 1 * 100 * 6, player.getScore());

        // Reset score
        player = new Player("TestPlayer");
        initialScore = player.getScore();

        // Test case where player is a loser and the banker wins
        player.scoring(banker, banker, losers, 1);
        // Expected score calculation: initialScore - (100 * 1 * 2)
        assertEquals(initialScore - 1 * 2 * 100, player.getScore());

        // Reset score
        player = new Player("TestPlayer");
        initialScore = player.getScore();

        // Test case where player is the banker and wins by self-draw
        player.scoring(player, player, new ArrayList<>(), 1);
        // Expected score calculation: initialScore + (100 * 1 * 12)
        assertEquals(initialScore + 1 * 100 * 12, player.getScore());

        // Reset score
        player = new Player("TestPlayer");
        initialScore = player.getScore();

        // Test case where player is a loser and another player wins by self-draw
        player.scoring(banker, winner, losers, 1);
        // Expected score calculation: initialScore - (100 * 1 * 2)
        assertEquals(initialScore - 1 * 100 * 2, player.getScore());
    }
}
