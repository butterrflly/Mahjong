package com.smoker.mahjong.doma.game;

import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HandTileTest {
// F4 P12 T16
    private HandTile handTile;
    private Tile tile1, tile2, tile3, tile4;

    @BeforeEach
    void setUp() {
        handTile = new HandTile();
        tile1 = new Tile(101, true);
        tile2 = new Tile(102, true);
        tile3 = new Tile(103, true);
        tile4 = new Tile(104, true);
    }

    @Test
    void testAddTile() {
        handTile.addTile(tile1);
        assertTrue(handTile.getHandTile().contains(tile1), "手牌应包含刚添加的牌");
    }

    @Test
    void testRemoveTile() {
        handTile.addTile(tile1);
        handTile.removeTile(tile1);
        assertFalse(handTile.getHandTile().contains(tile1), "手牌不应包含已移除的牌");
    }

    @Test
    void testCleanTile() {
        handTile.addTile(tile1);
        handTile.cleanTile();
        assertTrue(handTile.getHandTile().isEmpty(), "清空手牌后，手牌应为空");
    }

    @Test
    void testRemoveTileList() {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(tile1);
        tiles.add(tile2);
        handTile.addTile(tile1);
        handTile.addTile(tile2);
        handTile.removeTile(tiles);
        assertFalse(handTile.getHandTile().contains(tile1), "手牌不应包含已移除的牌");
        assertFalse(handTile.getHandTile().contains(tile2), "手牌不应包含已移除的牌");
    }

    @Test
    void testCanPang() {
        handTile.addTile(tile1);
        handTile.addTile(new Tile(111, true));
        handTile.addTile(tile2);
        assertTrue(handTile.canPang(101), "手牌应可以碰101牌");
    }

    @Test
    void testCanKong() {
        handTile.addTile(tile1);
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(101, true));
        assertTrue(handTile.canKong(101), "手牌应可以杠101牌");
    }

    @Test
    void testCanKongList() {
        handTile.addTile(tile1);
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(101, true));
        ArrayList<Tile> kongTiles = handTile.canKong();
        assertFalse(kongTiles.isEmpty(), "应有可以杠的牌");
        assertEquals(1, kongTiles.size(), "应有一个可以杠的牌");
    }

    @Test
    void testCanChow() {
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(102, true));
        handTile.addTile(new Tile(103, true));
        Tile chowTile = new Tile(104, true);
        ArrayList<ArrayList<Tile>> chowTiles = handTile.canChow(chowTile);
        assertFalse(chowTiles.isEmpty(), "应有可以吃的牌组合");
    }

    @Test
    void testCanHu() {
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(102, true));
        handTile.addTile(new Tile(103, true));
        handTile.addTile(new Tile(104, true));
        Tile huTile = new Tile(105, true);
        assertNotNull(handTile.canHu(huTile), "应可以胡牌");
    }

    @Test
    void testPang() {
        handTile.addTile(tile1);
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(101, true));
        handTile.Pang(tile1);
        assertEquals(1, handTile.getMelds().size(), "应有一个碰的组合");
    }

    @Test
    void testKong() {
        handTile.addTile(tile1);
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(101, true));
        handTile.Kong(tile1);
        assertEquals(1, handTile.getMelds().size(), "应有一个杠的组合");
    }

    @Test
    void testChow() {
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(102, true));
        ArrayList<Tile> chowTiles = new ArrayList<>();
        chowTiles.add(tile1);
        chowTiles.add(tile2);
        handTile.Chow(chowTiles, new Tile(103, true));
        assertEquals(1, handTile.getMelds().size(), "应有一个吃的组合");
    }

    @Test
    void testHu() {
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(102, true));
        handTile.addTile(new Tile(103, true));
        handTile.Hu(new Tile(104, true));
        // 不做进一步断言，因为该方法无具体实现
    }

    @Test
    void testSort() {
        handTile.addTile(new Tile(103, true));
        handTile.addTile(new Tile(102, true));
        handTile.addTile(new Tile(101, true));
        handTile.sort();
        assertEquals(101, handTile.getHandTile().get(0).getId(), "手牌应按顺序排序");
    }

    @Test
    void testGetHandTile() {
        handTile.addTile(tile1);
        assertTrue(handTile.getHandTile().contains(tile1), "应返回手牌列表");
    }

    @Test
    void testGetMelds() {
        handTile.addTile(tile1);
        handTile.addTile(new Tile(101, true));
        handTile.addTile(new Tile(101, true));
        handTile.Pang(tile1);
        assertFalse(handTile.getMelds().isEmpty(), "应返回组合列表");
    }
}
