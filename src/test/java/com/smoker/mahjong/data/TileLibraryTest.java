package com.smoker.mahjong.data;

import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TileLibraryTest {
// F5 P3 T8
    private TileLibrary tileLibrary;
    private ArrayList<Player> players;

    @BeforeEach
    public void setUp() {
        // 初始化玩家列表
        players = new ArrayList<>();
        players.add(new Player("player1"));
        players.add(new Player("player2"));
        players.add(new Player("player3"));
        players.add(new Player("player4"));

        // 创建TileLibrary实例
        tileLibrary = new TileLibrary(players);
    }

    @Test
    public void testCreateTiles() {
        // 测试牌的创建
        assertEquals(84, tileLibrary.getTiles().size(), "初始牌库应包含84张牌");
    }

    @Test
    public void testShuffle() {
        // 测试牌库打乱
        ArrayList<Tile> originalOrder = new ArrayList<>(tileLibrary.getTiles());
        tileLibrary.shuffle(tileLibrary.getTiles());
        ArrayList<Tile> shuffledOrder = tileLibrary.getTiles();

        // 两次顺序应该不同
        assertNotEquals(originalOrder, shuffledOrder, "打乱后的牌库顺序应不同");
    }

    @Test
    public void testDealInitialHand() {
        // 测试初始牌的发放
        tileLibrary.dealInitialHand();
        for (Player player : players) {
            assertEquals(13, player.getHandTile().getHandTile().size(), player.getName() + " 应有13张初始牌");
        }
        assertEquals(36, tileLibrary.getTiles().size(), "发牌后牌库应剩余36张牌");
    }

    @Test
    public void testDeal() {
        // 测试发牌给指定玩家
        tileLibrary.dealInitialHand();
        Tile dealtTile = tileLibrary.deal("player1");
        assertNotNull(dealtTile, "应成功发牌给player1");
        assertEquals(14, players.get(0).getHandTile().getHandTile().size(), "player1 应有14张牌");

        // 测试发牌后的牌库数量
        assertEquals(35, tileLibrary.getTiles().size(), "发牌后牌库应剩余35张牌");
    }

    @Test
    public void testFindTile() {
        // 测试根据ID找到指定牌
        Tile foundTile = tileLibrary.findTile(111);
        assertNotNull(foundTile, "应找到ID为111的牌");
        assertEquals(111, foundTile.getId(), "找到的牌ID应为111");
    }

    @Test
    public void testAddTableTile() {
        // 测试添加牌到桌面
        Tile tile = new Tile(111, true);
        tileLibrary.addTableTile(tile);
        ArrayList<Tile> tableTiles = tileLibrary.getTableTile();
        assertEquals(1, tableTiles.size(), "桌面应有1张牌");
        assertEquals(111, tableTiles.get(0).getId(), "桌面牌ID应为111");
    }

    @Test
    public void testGetTableTile() {
        // 测试获取桌面牌
        Tile tile1 = new Tile(111, true);
        Tile tile2 = new Tile(121, true);
        tileLibrary.addTableTile(tile1);
        tileLibrary.addTableTile(tile2);
        ArrayList<Tile> tableTiles = tileLibrary.getTableTile();
        assertEquals(2, tableTiles.size(), "桌面应有2张牌");
    }

    @Test
    public void testGetTiles() {
        // 测试获取牌库牌
        assertEquals(84, tileLibrary.getTiles().size(), "初始牌库应有84张牌");
        tileLibrary.dealInitialHand();
        assertEquals(36, tileLibrary.getTiles().size(), "发牌后牌库应剩余36张牌");
    }
}
