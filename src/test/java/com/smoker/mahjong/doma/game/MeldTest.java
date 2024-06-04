package com.smoker.mahjong.doma.game;

import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MeldTest {
// P6 T6
    private Meld meld;
    private Tile tile1, tile2, tile3;

    @BeforeEach
    void setUp() {
        tile1 = new Tile(101, true);
        tile2 = new Tile(102, true);
        tile3 = new Tile(103, true);
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(tile1);
        tiles.add(tile2);
        tiles.add(tile3);
        meld = new Meld(tiles, "Chow", true);
    }

    @Test
    void testGetMeld() {
        ArrayList<Tile> tiles = meld.getMeld();
        assertEquals(3, tiles.size(), "组合应包含3张牌");
        assertTrue(tiles.contains(tile1), "组合应包含tile1");
        assertTrue(tiles.contains(tile2), "组合应包含tile2");
        assertTrue(tiles.contains(tile3), "组合应包含tile3");
    }

    @Test
    void testGetMeldArray() {
        int[] meldArray = meld.getMeldArray();
        assertEquals(3, meldArray.length, "组合数组应包含3张牌");
        assertEquals(101, meldArray[0], "组合数组的第一张牌应为101");
        assertEquals(102, meldArray[1], "组合数组的第二张牌应为102");
        assertEquals(103, meldArray[2], "组合数组的第三张牌应为103");
    }

    @Test
    void testOpen() {
        meld.open();
        assertFalse(meld.getHide(), "组合应为明牌");
    }

    @Test
    void testGetHide() {
        assertTrue(meld.getHide(), "组合应为暗牌");
    }

    @Test
    void testGetType() {
        assertEquals("Chow", meld.getType(), "组合类型应为Chow");
    }

    @Test
    void testSort() {
        meld.sort();
        ArrayList<Tile> tiles = meld.getMeld();
        assertEquals(101, tiles.get(0).getId(), "排序后，第一张牌应为101");
        assertEquals(102, tiles.get(1).getId(), "排序后，第二张牌应为102");
        assertEquals(103, tiles.get(2).getId(), "排序后，第三张牌应为103");
    }
}
