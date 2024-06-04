package com.smoker.mahjong.impl;

import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameStarterTest {
// F13 P19 T32
    private GameStarter gameStarter;

    @BeforeEach
    void setUp() {
        gameStarter = new GameStarter("testRoom");
        gameStarter.addPlayer("player1");
        gameStarter.addPlayer("player2");
        gameStarter.addPlayer("player3");
        gameStarter.addPlayer("player4");
    }

    @Test
    void testStartGame() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        assertTrue(gameStarter.startGame(), "游戏应该成功开始");
    }

    @Test
    void testGameOver() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        gameStarter.gameOver();
        assertFalse(gameStarter.startGame(), "游戏应该结束");
    }

    @Test
    void testAddPlayer() {
        GameStarter newGameStarter = new GameStarter("newRoom");
        assertEquals("添加成功", newGameStarter.addPlayer("newPlayer"), "新玩家应该成功添加");
    }

    @Test
    void testRemovePlayer() {
        assertEquals("移除成功", gameStarter.removePlayer("player1"), "玩家应该成功移除");
    }

    @Test
    void testGetPlayerList() {
        ArrayList<Player> playerList = gameStarter.getPlayerList();
        assertEquals(4, playerList.size(), "玩家列表的长度应该是4");
    }

    @Test
    void testGetPlayerNum() {
        int playerNum = gameStarter.getPlayerNum();
        assertEquals(4, playerNum, "玩家数量应该是4");
    }

    @Test
    void testGetDealPlayer() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        assertNotNull(gameStarter.getDealPlayer(), "应该有一个发牌的玩家");
    }

    @Test
    void testGetDealTileID() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        int dealTileID = gameStarter.deal();
        assertTrue(dealTileID > 0, "发牌的ID应该大于0");
    }

    @Test
    void testGetDiscardPlayerName() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        gameStarter.deal();
        gameStarter.discard("player1", 101);
        assertEquals("player1", gameStarter.getDiscardPlayerName(), "弃牌玩家应该是player1");
    }

    @Test
    void testGetDiscardTileID() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        gameStarter.deal();
        gameStarter.discard("player1", 101);
        assertEquals(101, gameStarter.getDiscardTileID(), "弃牌的ID应该是101");
    }

    @Test
    void testGetHandTile() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        HandTile handTile = gameStarter.getHandTile("player1");
        assertNotNull(handTile, "应该返回player1的手牌");
    }

    @Test
    void testSetBanker() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        gameStarter.setBanker();
        assertNotNull(gameStarter.getBanker(), "庄家应该被设置");
    }

    @Test
    void testGetBanker() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        assertNotNull(gameStarter.getBanker(), "应该返回庄家的名字");
    }

    @Test
    void testDeal() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        int dealTileID = gameStarter.deal();
        assertTrue(dealTileID > 0, "发牌的ID应该大于0");
    }

    @Test
    void testDiscard() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        gameStarter.deal();
        int removedTileID = gameStarter.discard("player1", 101);
        assertEquals(101, removedTileID, "移除的牌ID应该是101");
    }

    @Test
    void testCanPang() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        assertFalse(gameStarter.canPang("player1", 101), "在没有相同牌的情况下，不能碰");
    }

    @Test
    void testCanKong() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        assertFalse(gameStarter.canKong("player1", 101), "在没有相同牌的情况下，不能杠");
    }

    @Test
    void testCanKongMultiple() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        ArrayList<Tile> kongTiles = gameStarter.canKong("player1");
        assertTrue(kongTiles.isEmpty(), "在没有相同牌的情况下，不能杠");
    }

    @Test
    void testCanChow() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        ArrayList<ArrayList<Tile>> chowTiles = gameStarter.canChow("player1", 101);
        assertTrue(chowTiles.isEmpty(), "在没有相应顺子的情况下，不能吃牌");
    }

    @Test
    void testCanHu() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        assertFalse(gameStarter.canHu("player1", 101), "在不满足胡牌条件的情况下，不能胡牌");
    }

    @Test
    void testPang() throws NoSuchFieldException, IllegalAccessException {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        gameStarter.Pang("player1", 101);

        Player player = gameStarter.findPlayer("player1");
        HandTile handTile = player.getHandTile();

        Field meldsField = HandTile.class.getDeclaredField("melds");
        meldsField.setAccessible(true);
        ArrayList melds = (ArrayList) meldsField.get(handTile);

        assertFalse(melds.isEmpty(), "玩家应该有一个碰牌");
    }

    @Test
    void testKong() throws NoSuchFieldException, IllegalAccessException {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        gameStarter.Kong("player1", 101);

        Player player = gameStarter.findPlayer("player1");
        HandTile handTile = player.getHandTile();

        Field meldsField = HandTile.class.getDeclaredField("melds");
        meldsField.setAccessible(true);
        ArrayList melds = (ArrayList) meldsField.get(handTile);

        assertFalse(melds.isEmpty(), "玩家应该有一个杠牌");
    }

    @Test
    void testChow() throws NoSuchFieldException, IllegalAccessException {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        ArrayList<Tile> chowTiles = new ArrayList<>();
        gameStarter.Chow(chowTiles, "player1", 101);

        Player player = gameStarter.findPlayer("player1");
        HandTile handTile = player.getHandTile();

        Field meldsField = HandTile.class.getDeclaredField("melds");
        meldsField.setAccessible(true);
        ArrayList melds = (ArrayList) meldsField.get(handTile);

        assertFalse(melds.isEmpty(), "玩家应该有一个吃牌");
    }

    @Test
    void testHu() throws NoSuchFieldException, IllegalAccessException {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        gameStarter.Hu("player1", 101, "player2");

        Field winnerField = GameStarter.class.getDeclaredField("winner");
        winnerField.setAccessible(true);
        Player winner = (Player) winnerField.get(gameStarter);

        assertEquals("player1", winner.getName(), "玩家1应该是赢家");
    }

    @Test
    void testCanDraw() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        assertFalse(gameStarter.canDraw(), "在牌堆未空的情况下，不能荒牌");
    }

    @Test
    void testGetSequence() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        Player[] sequence = gameStarter.getSequence("player1");
        assertEquals(4, sequence.length, "应该返回4个玩家的顺序");
    }

    @Test
    void testFindPlayer() {
        Player player = gameStarter.findPlayer("player1");
        assertNotNull(player, "应该能找到player1");
    }

    @Test
    void testFindTile() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        Tile tile = gameStarter.findTile(101);
        assertNotNull(tile, "应该能找到指定ID的牌");
    }

    @Test
    void testAddTableTile() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        gameStarter.deal();
        gameStarter.addTableTile();
        assertFalse(gameStarter.getTableTile().isEmpty(), "牌桌上应该有牌");
    }

    @Test
    void testGetTableTile() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        gameStarter.deal();
        gameStarter.addTableTile();
        assertFalse(gameStarter.getTableTile().isEmpty(), "牌桌上应该有牌");
    }

    @Test
    void testPrintHand() {
        gameStarter.getPlayerList().forEach(player -> player.setPrepare());
        gameStarter.startGame();
        gameStarter.printHand("player1");
        // 不做进一步断言，因为输出到控制台
    }

    @Test
    void testGetRoomID() {
        assertEquals("testRoom", gameStarter.getRoomID(), "房间ID应该是testRoom");
    }
}
