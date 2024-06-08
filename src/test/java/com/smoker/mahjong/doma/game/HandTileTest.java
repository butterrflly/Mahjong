package com.smoker.mahjong.doma.game;

import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the HandTile class.
 */
public class HandTileTest {

    private HandTile handTile;

    @BeforeEach
    public void setUp() {
        handTile = new HandTile();
    }

    @Test
    public void testAddTile() {
        // Test adding a tile to the hand
        Tile tile = new Tile(111, true); // 1Wan 1
        handTile.addTile(tile);
        assertEquals(1, handTile.getHandTile().size());
        assertEquals(111, handTile.getHandTile().get(0).getId());
    }

    @Test
    public void testRemoveTile() {
        // Test removing a tile from the hand
        Tile tile = new Tile(111, true); // 1Wan 1
        handTile.addTile(tile);
        handTile.removeTile(tile);
        assertEquals(0, handTile.getHandTile().size());
    }

    @Test
    public void testCleanTile() {
        // Test clearing all tiles from the hand
        Tile tile1 = new Tile(111, true); // 1Wan 1
        Tile tile2 = new Tile(211, true); // 2Tiao 1
        handTile.addTile(tile1);
        handTile.addTile(tile2);
        handTile.cleanTile();
        assertEquals(0, handTile.getHandTile().size());
    }

    @Test
    public void testRemoveMultipleTiles() {
        // Test removing multiple tiles from the hand
        Tile tile1 = new Tile(111, true); // 1Wan 1
        Tile tile2 = new Tile(112, true); // 1Wan 2
        ArrayList<Tile> tilesToRemove = new ArrayList<>();
        tilesToRemove.add(tile1);
        tilesToRemove.add(tile2);
        handTile.addTile(tile1);
        handTile.addTile(tile2);
        handTile.removeTile(tilesToRemove);
        assertEquals(0, handTile.getHandTile().size());
    }

    @Test
    public void testCanPang() {
        // Ensure canPang method works correctly
        handTile.addTile(new Tile(111, true)); // 1Wan 1
        handTile.addTile(new Tile(112, true)); // 1Wan 2
        handTile.addTile(new Tile(113, true)); // 1Wan 3
        assertTrue(handTile.canPang(111));
        assertFalse(handTile.canPang(211));
    }

    @Test
    public void testCanKong() {
        // Ensure canKong method works correctly
        handTile.addTile(new Tile(111, true)); // 1Wan 1
        handTile.addTile(new Tile(112, true)); // 1Wan 2
        handTile.addTile(new Tile(113, true)); // 1Wan 3
        handTile.addTile(new Tile(114, true)); // 1Wan 4
        assertTrue(handTile.canKong(111));
        assertFalse(handTile.canKong(211));
    }

    @Test
    public void testCanHu() {
        // Ensure canHu method works correctly
        handTile.addTile(new Tile(111, true)); // 1Wan 1
        handTile.addTile(new Tile(112, true)); // 1Wan 2
        handTile.addTile(new Tile(113, true)); // 1Wan 3
        handTile.addTile(new Tile(121, true)); // 2Wan 1
        handTile.addTile(new Tile(122, true)); // 2Wan 2
        handTile.addTile(new Tile(123, true)); // 2Wan 3
        Tile huTile = new Tile(111, true); // 1Wan 1
        assertEquals(2, handTile.canHu(huTile)); // Expected normal Hu
    }

    @Test
    public void testPang() {
        // Ensure Pang method works correctly
        handTile.addTile(new Tile(111, true)); // 1Wan 1
        handTile.addTile(new Tile(112, true)); // 1Wan 2
        handTile.addTile(new Tile(113, true)); // 1Wan 3
        handTile.Pang(new Tile(111, true)); // Pang 1Wan
        assertEquals(3, handTile.getMelds().get(0).getMeld().size()); // Should have 3 tiles in the meld
    }

    @Test
    public void testKong() {
        // Ensure Kong method works correctly
        handTile.addTile(new Tile(111, true)); // 1Wan 1
        handTile.addTile(new Tile(112, true)); // 1Wan 2
        handTile.addTile(new Tile(113, true)); // 1Wan 3
        handTile.addTile(new Tile(114, true)); // 1Wan 4
        handTile.Kong(new Tile(111, true)); // Kong 1Wan
        assertEquals(4, handTile.getMelds().get(0).getMeld().size()); // Should have 4 tiles in the meld
    }

    @Test
    public void testChow() {
        // Ensure Chow method works correctly
        handTile.addTile(new Tile(111, true)); // 1Wan 1
        handTile.addTile(new Tile(112, true)); // 1Wan 2
        handTile.addTile(new Tile(113, true)); // 1Wan 3
        ArrayList<Tile> chowTiles = new ArrayList<>();
        chowTiles.add(new Tile(111, true)); // 1Wan 1
        chowTiles.add(new Tile(113, true)); // 1Wan 3
        handTile.Chow(chowTiles, new Tile(112, true)); // Chow 1Wan 2
        assertEquals(3, handTile.getMelds().get(0).getMeld().size()); // Should have 3 tiles in the meld
    }
}
