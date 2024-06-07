package com.smoker.mahjong.doma.game;

import com.smoker.mahjong.doma.Game.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
// F2 P3 T5
    @Test
    void testTileCreation() {
        Tile tile = new Tile(101, true);
        assertNotNull(tile, "Tile 对象创建失败");
        assertEquals(101, tile.getId(), "Tile ID 初始化不正确");
    }

    @Test
    void testTileId() {
        Tile tile = new Tile(202, false);
        assertEquals(202, tile.getId(), "Tile ID 返回不正确");
    }

    @Test
    void testTileVisibility() {
        Tile hiddenTile = new Tile(303, true);
        Tile visibleTile = new Tile(404, false);

        assertTrue(hiddenTile.isHidden(), "Tile 应该是隐藏的");
        assertFalse(visibleTile.isHidden(), "Tile 应该是可见的");
    }

    @Test
    void testTileSuit() {
        Tile tile = new Tile(101, false);
        assertEquals("筒", tile.getSuit(), "Tile 花色返回不正确");

        tile = new Tile(203, false);
        assertEquals("条", tile.getSuit(), "Tile 花色返回不正确");

        tile = new Tile(305, false);
        assertEquals("万", tile.getSuit(), "Tile 花色返回不正确");

        tile = new Tile(401, false);
        assertEquals("风", tile.getSuit(), "Tile 花色返回不正确");

        tile = new Tile(503, false);
        assertEquals("字", tile.getSuit(), "Tile 花色返回不正确");
    }

    @Test
    void testTileNumber() {
        Tile tile = new Tile(101, false);
        assertEquals("1", tile.getNum(), "Tile 数字返回不正确");

        tile = new Tile(214, false);
        assertEquals("4", tile.getNum(), "Tile 数字返回不正确");

        tile = new Tile(331, false);
        assertEquals("1", tile.getNum(), "Tile 数字返回不正确");

        tile = new Tile(402, false);
        assertEquals("2", tile.getNum(), "Tile 数字返回不正确");

        tile = new Tile(503, false);
        assertEquals("3", tile.getNum(), "Tile 数字返回不正确");
    }

}
