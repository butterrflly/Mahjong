package com.smoker.mahjong.doma.game;

import com.smoker.mahjong.doma.Game.MeldUtil;
import com.smoker.mahjong.doma.Game.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MeldUtilTest {
// F3 P7 T10
    private MeldUtil meldUtil;
    private Tile tile1, tile2, tile3, tile4, tile5, tile6, tile7;

    @BeforeEach
    void setUp() {
        meldUtil = new MeldUtil();
        tile1 = new Tile(101, true); // 筒1
        tile2 = new Tile(102, true); // 筒1
        tile3 = new Tile(103, true); // 筒1
        tile4 = new Tile(201, true); // 条1
        tile5 = new Tile(202, true); // 条1
        tile6 = new Tile(203, true); // 条1
        tile7 = new Tile(104, true); // 筒1
    }

    @Test
    void testAddTile() {
        meldUtil.addTile(tile1);
        assertFalse(meldUtil.isHu(), "不能胡牌，仅有一张牌时");
    }

    @Test
    void testIsHu_emptyTiles() {
        assertTrue(meldUtil.isHu(), "空手牌应返回true");
    }

    @Test
    void testIsHu_invalidNumberOfTiles() {
        meldUtil.addTile(tile1);
        meldUtil.addTile(tile2);
        assertFalse(meldUtil.isHu(), "牌数不对，不能胡牌");
    }

    @Test
    void testIsHu_validTriplet() {
        meldUtil.addTile(tile1);
        meldUtil.addTile(tile2);
        meldUtil.addTile(tile3);
        assertTrue(meldUtil.isHu(), "三个一样的牌，可以胡牌");
        assertEquals(1, meldUtil.tripletNum(), "应有一个刻子");
    }

    @Test
    void testIsHu_validSequence() {
        meldUtil.addTile(tile4);
        meldUtil.addTile(tile5);
        meldUtil.addTile(tile6);
        assertTrue(meldUtil.isHu(), "一副顺子，可以胡牌");
        assertEquals(1, meldUtil.sequenceNum(), "应有一个顺子");
    }

    @Test
    void testIsHu_mixedTripletAndSequence() {
        meldUtil.addTile(tile1);
        meldUtil.addTile(tile2);
        meldUtil.addTile(tile3);
        meldUtil.addTile(tile4);
        meldUtil.addTile(tile5);
        meldUtil.addTile(tile6);
        meldUtil.addTile(tile7);
        assertFalse(meldUtil.isHu(), "三个一样加三个顺子，没有对子，不能胡牌");
    }

    @Test
    void testIsSequence_validSequence() {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(tile4);
        tiles.add(tile5);
        tiles.add(tile6);
        assertTrue(meldUtil.isSequence(tiles), "一副顺子，可以胡牌");
    }

    @Test
    void testIsSequence_invalidSequence() {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(tile1);
        tiles.add(tile2);
        tiles.add(tile4);
        assertFalse(meldUtil.isSequence(tiles), "不是顺子，不能胡牌");
    }

    @Test
    void testTripletNum() {
        meldUtil.addTile(tile1);
        meldUtil.addTile(tile2);
        meldUtil.addTile(tile3);
        meldUtil.isHu();
        assertEquals(1, meldUtil.tripletNum(), "应有一个刻子");
    }

    @Test
    void testSequenceNum() {
        meldUtil.addTile(tile4);
        meldUtil.addTile(tile5);
        meldUtil.addTile(tile6);
        meldUtil.isHu();
        assertEquals(1, meldUtil.sequenceNum(), "应有一个顺子");
    }
}
