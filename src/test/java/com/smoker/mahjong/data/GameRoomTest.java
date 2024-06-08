package com.smoker.mahjong.data;

import com.smoker.mahjong.doma.User.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

/**
 * Unit tests for the GameRoom class.
 */
public class GameRoomTest {

    private GameRoom gameRoom;

    @BeforeEach
    public void setUp() {
        gameRoom = new GameRoom();
    }

    @Test
    public void testAddPlayerSuccess() {
        String result = gameRoom.addPlayer("Player1");
        assertEquals("添加成功", result); // Translation: "Added successfully"
        assertEquals(1, gameRoom.getPlayerNum());
        assertEquals("Player1", gameRoom.getPlayerList().get(0).getName());
    }

    @Test
    public void testAddPlayerRoomFull() {
        gameRoom.addPlayer("Player1");
        gameRoom.addPlayer("Player2");
        gameRoom.addPlayer("Player3");
        gameRoom.addPlayer("Player4");

        String result = gameRoom.addPlayer("Player5");
        assertEquals("房间已满", result); // Translation: "Room is full"
        assertEquals(4, gameRoom.getPlayerNum());
    }

    @Test
    public void testRemovePlayerSuccess() {
        gameRoom.addPlayer("Player1");
        Player player = gameRoom.getPlayerList().get(0); // Get the actual Player object added
        String result = gameRoom.removePlayer(player);
        assertEquals("移除成功", result); // Translation: "Removed successfully"
        assertEquals(0, gameRoom.getPlayerNum());
    }

    @Test
    public void testRemovePlayerNotFound() {
        Player player = new Player("Player1");
        String result = gameRoom.removePlayer(player);
        assertEquals("玩家不存在", result); // Translation: "Player does not exist"
    }

    @Test
    public void testGetPlayerNum() {
        gameRoom.addPlayer("Player1");
        gameRoom.addPlayer("Player2");
        assertEquals(2, gameRoom.getPlayerNum());
    }

    @Test
    public void testGetPlayerList() {
        gameRoom.addPlayer("Player1");
        gameRoom.addPlayer("Player2");
        ArrayList<Player> playerList = gameRoom.getPlayerList();
        assertEquals(2, playerList.size());
        assertEquals("Player1", playerList.get(0).getName());
        assertEquals("Player2", playerList.get(1).getName());
    }
}
