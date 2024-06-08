package com.smoker.mahjong.impl;

import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the GameStarter class.
 */
public class GameStarterTest {

    private GameStarter gameStarter;

    @BeforeEach
    public void setUp() {
        gameStarter = new GameStarter("testRoom");
        gameStarter.addPlayer("Player1");
        gameStarter.addPlayer("Player2");
        gameStarter.addPlayer("Player3");
        gameStarter.addPlayer("Player4");
        gameStarter.findPlayer("Player1").setPrepare();
        gameStarter.findPlayer("Player2").setPrepare();
        gameStarter.findPlayer("Player3").setPrepare();
        gameStarter.findPlayer("Player4").setPrepare();
        gameStarter.startGame();
    }

    @Test
    public void testStartGame() {
        // Test starting the game with all players prepared
        assertTrue(gameStarter.startGame());
    }

    @Test
    public void testAddPlayer() {
        // Test adding a player to the game
        gameStarter.removePlayer("Player4"); // Ensure room for one more player
        assertEquals("添加成功", gameStarter.addPlayer("Player5"));
    }

    @Test
    public void testRemovePlayer() {
        // Test removing a player from the game
        assertEquals("移除成功", gameStarter.removePlayer("Player1"));
    }

    @Test
    public void testGetPlayerList() {
        // Test getting the list of players
        assertEquals(4, gameStarter.getPlayerList().size());
    }

    @Test
    public void testGetPlayerNum() {
        // Test getting the number of players
        assertEquals(4, gameStarter.getPlayerNum());
    }

    @Test
    public void testGetDealPlayer() {
        // Test getting the player who is dealing
        gameStarter.deal();
        assertNotNull(gameStarter.getDealPlayer());
    }

    @Test
    public void testGetDealTileID() {
        // Test getting the ID of the dealt tile
        gameStarter.deal();
        assertTrue(gameStarter.getDealTileID() > 0);
    }

    @Test
    public void testGetDiscardPlayerName() {
        // Test getting the name of the player who discarded
        gameStarter.deal();
        int tileID = gameStarter.getDealTileID();
        gameStarter.discard("Player1", tileID);
        assertEquals("Player1", gameStarter.getDiscardPlayerName());
    }

    @Test
    public void testGetDiscardTileID() {
        // Test getting the ID of the discarded tile
        gameStarter.deal();
        int tileID = gameStarter.getDealTileID();
        gameStarter.discard("Player1", tileID);
        assertEquals(tileID, gameStarter.getDiscardTileID());
    }

    @Test
    public void testGetHandTile() {
        // Test getting the hand tiles of a player
        assertNotNull(gameStarter.getHandTile("Player1"));
    }

    @Test
    public void testSetBanker() {
        // Test setting the banker
        gameStarter.setBanker();
        assertNotNull(gameStarter.getBanker());
    }

    @Test
    public void testGetBanker() {
        // Test getting the banker
        assertNotNull(gameStarter.getBanker());
    }

    @Test
    public void testDeal() {
        // Test dealing a tile to a player
        int tileID = gameStarter.deal();
        assertTrue(tileID > 0);
    }

    @Test
    public void testDiscard() {
        // Test discarding a tile by a player
        gameStarter.deal();
        int tileID = gameStarter.getDealTileID();
        assertEquals(tileID, gameStarter.discard("Player1", tileID));
    }

    @Test
    public void testGetSequence() {
        // Test getting player sequence
        Player[] sequence = gameStarter.getSequence("Player1");
        assertEquals(4, sequence.length);
        assertEquals("Player1", sequence[0].getName());
    }

    @Test
    public void testFindPlayer() {
        // Test finding a player by name
        assertNotNull(gameStarter.findPlayer("Player1"));
    }

    @Test
    public void testFindTile() {
        // Test finding a tile by ID
        gameStarter.deal();
        int tileID = gameStarter.getDealTileID();
        assertNotNull(gameStarter.findTile(tileID));
    }

    @Test
    public void testAddTableTile() {
        // Test adding a tile to the table
        gameStarter.deal();
        int tileID = gameStarter.getDealTileID();
        gameStarter.discard("Player1", tileID);
        gameStarter.addTableTile();
        assertTrue(gameStarter.getTableTile().size() > 0);
    }

    @Test
    public void testGetTableTile() {
        // Test getting the tiles on the table
        assertNotNull(gameStarter.getTableTile());
    }

    @Test
    public void testPrintHand() {
        // Test printing the hand tiles and melds of a player
        gameStarter.printHand("Player1");
        assertNotNull(gameStarter.getHandTile("Player1"));
    }

    @Test
    public void testGetRoomID() {
        // Test getting the room ID
        assertEquals("testRoom", gameStarter.getRoomID());
    }
}
