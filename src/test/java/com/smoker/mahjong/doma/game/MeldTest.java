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

    /**
     * 在每个测试方法运行之前执行，初始化测试数据
     */
    @BeforeEach
    void setUp() {
        // 创建三张牌 Tile 对象
        tile1 = new Tile(101, true);
        tile2 = new Tile(102, true);
        tile3 = new Tile(103, true);

        // 创建一个包含三张牌的列表
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(tile1);
        tiles.add(tile2);
        tiles.add(tile3);

        // 初始化 Meld 对象，类型为 "Chow"，并且是暗牌
        meld = new Meld(tiles, "Chow", true);
    }

    /**
     * 测试 getMeld 方法，确保返回的牌列表正确
     */
    @Test
    void testGetMeld() {
        // 获取组合中的牌列表
        ArrayList<Tile> tiles = meld.getMeld();

        // 断言组合包含三张牌
        assertEquals(3, tiles.size(), "组合应包含3张牌");

        // 断言组合包含指定的三张牌
        assertTrue(tiles.contains(tile1), "组合应包含tile1");
        assertTrue(tiles.contains(tile2), "组合应包含tile2");
        assertTrue(tiles.contains(tile3), "组合应包含tile3");
    }

    /**
     * 测试 getMeldArray 方法，确保返回的牌数组正确
     */
    @Test
    void testGetMeldArray() {
        // 获取组合中的牌的ID数组
        int[] meldArray = meld.getMeldArray();

        // 断言数组长度为三
        assertEquals(3, meldArray.length, "组合数组应包含3张牌");

        // 断言数组中的牌ID按顺序正确
        assertEquals(101, meldArray[0], "组合数组的第一张牌应为101");
        assertEquals(102, meldArray[1], "组合数组的第二张牌应为102");
        assertEquals(103, meldArray[2], "组合数组的第三张牌应为103");
    }

    /**
     * 测试 open 方法，确保组合从暗牌变为明牌
     */
    @Test
    void testOpen() {
        // 将组合设为明牌
        meld.open();

        // 断言组合现在是明牌
        assertFalse(meld.getHide(), "组合应为明牌");
    }

    /**
     * 测试 getHide 方法，确保返回组合是否为暗牌的状态
     */
    @Test
    void testGetHide() {
        // 断言组合最初为暗牌
        assertTrue(meld.getHide(), "组合应为暗牌");
    }

    /**
     * 测试 getType 方法，确保返回组合的类型
     */
    @Test
    void testGetType() {
        // 断言组合类型为 "Chow"
        assertEquals("Chow", meld.getType(), "组合类型应为Chow");
    }

    /**
     * 测试 sort 方法，确保组合中的牌按ID排序
     */
    @Test
    void testSort() {
        // 对组合中的牌进行排序
        meld.sort();

        // 获取排序后的牌列表
        ArrayList<Tile> tiles = meld.getMeld();

        // 断言牌ID按顺序正确
        assertEquals(101, tiles.get(0).getId(), "排序后，第一张牌应为101");
        assertEquals(102, tiles.get(1).getId(), "排序后，第二张牌应为102");
        assertEquals(103, tiles.get(2).getId(), "排序后，第三张牌应为103");
    }
}
