package com.smoker.mahjong.data;

import com.smoker.mahjong.doma.User.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameRoomTest {
// F1 P3 T4
    private GameRoom gameRoom;

    @BeforeEach
    public void setUp() {
        gameRoom = new GameRoom();
    }

    @Test
    public void testAddPlayer() {
        // 测试添加玩家成功的情况
        String result = gameRoom.addPlayer("player1");
        assertEquals("添加成功", result, "应成功添加玩家");
        assertEquals(1, gameRoom.getPlayerNum(), "玩家数量应为1");

        // 测试房间已满的情况
        gameRoom.addPlayer("player2");
        gameRoom.addPlayer("player3");
        gameRoom.addPlayer("player4");
        result = gameRoom.addPlayer("player5");
        assertEquals("房间已满", result, "房间已满时应返回适当消息");
        assertEquals(4, gameRoom.getPlayerNum(), "玩家数量应为4");
    }

    @Test
    public void testRemovePlayer() {
        // 测试移除玩家成功的情况
        Player player1 = new Player("player1");
        gameRoom.addPlayer("player1");
        String result = gameRoom.removePlayer(player1);
        assertEquals("移除成功", result, "应成功移除玩家");
        assertEquals(0, gameRoom.getPlayerNum(), "玩家数量应为0");

        // 测试移除不存在的玩家
        Player player2 = new Player("player2");
        result = gameRoom.removePlayer(player2);
        assertEquals("玩家不存在", result, "玩家不存在时应返回适当消息");
    }

    @Test
    public void testGetPlayerNum() {
        // 测试获取玩家数量
        assertEquals(0, gameRoom.getPlayerNum(), "初始玩家数量应为0");
        gameRoom.addPlayer("player1");
        assertEquals(1, gameRoom.getPlayerNum(), "添加1个玩家后数量应为1");
    }

    @Test
    public void testGetPlayerList() {
        // 测试获取玩家列表
        gameRoom.addPlayer("player1");
        gameRoom.addPlayer("player2");
        ArrayList<Player> playerList = gameRoom.getPlayerList();
        assertEquals(2, playerList.size(), "玩家列表应包含2个玩家");
        assertEquals("player1", playerList.get(0).getName(), "第一个玩家应为player1");
        assertEquals("player2", playerList.get(1).getName(), "第二个玩家应为player2");
    }
}
