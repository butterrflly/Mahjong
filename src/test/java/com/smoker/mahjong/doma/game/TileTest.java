package com.smoker.mahjong.doma.game;

import com.smoker.mahjong.doma.Game.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Tile class.
 */
public class TileTest {

    private Tile tile;

    @BeforeEach
    public void setUp() {
        // Initializing a tile with ID 111 (1Wan 1) and hidden state true
        tile = new Tile(111, true);
    }

    @Test
    public void testGetId() {
        // Ensure getId method works correctly
        assertEquals(111, tile.getId());
    }

    @Test
    public void testIsHidden() {
        // Ensure isHidden method works correctly
        assertTrue(tile.isHidden());
    }
}
