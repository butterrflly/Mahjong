package com.smoker.mahjong.doma.game;

import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Meld class.
 */
public class MeldTest {

    private Meld meld;
    private ArrayList<Tile> tiles;

    @BeforeEach
    public void setUp() {
        tiles = new ArrayList<>();
        tiles.add(new Tile(111, true)); // 1Wan 1
        tiles.add(new Tile(112, true)); // 1Wan 2
        tiles.add(new Tile(113, true)); // 1Wan 3
        meld = new Meld(tiles, "Pong", true); // Initializing a Pong meld with tiles
    }

    @Test
    public void testGetMeld() {
        // Ensure getMeld method works correctly
        assertEquals(tiles, meld.getMeld());
    }

    @Test
    public void testGetMeldArray() {
        // Ensure getMeldArray method works correctly
        int[] expectedArray = {111, 112, 113};
        assertArrayEquals(expectedArray, meld.getMeldArray());
    }

    @Test
    public void testOpen() {
        // Ensure open method works correctly
        meld.open();
        assertFalse(meld.getHide());
    }

    @Test
    public void testGetHide() {
        // Ensure getHide method works correctly
        assertTrue(meld.getHide());
    }

    @Test
    public void testGetType() {
        // Ensure getType method works correctly
        assertEquals("Pong", meld.getType());
    }

    @Test
    public void testSort() {
        // Ensure sort method works correctly
        tiles.add(new Tile(114, true)); // 1Wan 4
        meld.sort();
        assertEquals(111, meld.getMeld().get(0).getId());
        assertEquals(112, meld.getMeld().get(1).getId());
        assertEquals(113, meld.getMeld().get(2).getId());
        assertEquals(114, meld.getMeld().get(3).getId());
    }
}
