package com.smoker.mahjong.doma.user;

import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
// F3 P7 T10
    private Player player;
    private Player banker;
    private Player winner;
    private ArrayList<Player> losers;

    @BeforeEach
    void setUp() {
        player = new Player("player1");
        banker = new Player("banker");
        winner = new Player("winner");
        losers = new ArrayList<>();
    }

    @Test
    void testAddInitialHand() {
        Tile tile = new Tile(101, true);
        player.addInitialHand(tile);
        HandTile handTile = player.getHandTile();
        assertTrue(handTile.getHandTile().contains(tile), "手牌应包含刚添加的牌");
    }

    @Test
    void testScoringNormalWin() {
        losers.add(new Player("loser1"));
        player.scoring(banker, player, losers, "normal");
        assertEquals(5800, player.getScore(), "平胡情况下庄家应得分5800");
    }

    @Test
    void testScoringSevenPairsWin() {
        losers.add(new Player("loser1"));
        player.scoring(banker, player, losers, "seven pairs");
        assertEquals(6600, player.getScore(), "七对情况下庄家应得分6600");
    }

    @Test
    void testScoringDraw() {
        player.scoring(banker, null, null, null);
        assertEquals(5000, player.getScore(), "荒牌情况下分数应保持不变");
    }

    @Test
    void testScoringLoser() {
        losers.add(player);
        winner.scoring(banker, winner, losers, "normal");
        assertEquals(4800, player.getScore(), "平胡情况下输家应失分4800");
    }

    @Test
    void testGetName() {
        assertEquals("player1", player.getName(), "玩家的名字应为player1");
    }

    @Test
    void testGetHandTile() {
        HandTile handTile = player.getHandTile();
        assertNotNull(handTile, "应返回玩家的手牌");
    }

    @Test
    void testGetScore() {
        assertEquals(5000, player.getScore(), "初始分数应为5000");
    }

    @Test
    void testIsPrepare() {
        assertFalse(player.isPrepare(), "初始情况下应未准备");
    }

    @Test
    void testSetPrepare() {
        player.setPrepare();
        assertTrue(player.isPrepare(), "设置准备后应为准备状态");
        player.setPrepare();
        assertFalse(player.isPrepare(), "再次设置准备后应为未准备状态");
    }
}
