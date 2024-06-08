package com.smoker.mahjong.data;

import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TileLibrary class.
 */
public class TileLibraryTest {

    private TileLibrary tileLibrary;
    private ArrayList<Player> playerList;

    @BeforeEach
    public void setUp() {
        // Initialize player list with 4 players
        playerList = new ArrayList<>();
        playerList.add(new Player("Player1"));
        playerList.add(new Player("Player2"));
        playerList.add(new Player("Player3"));
        playerList.add(new Player("Player4"));
        // Initialize tile library with player list
        tileLibrary = new TileLibrary(playerList);
    }

    @Test
    public void testCreateTiles() {
        // Ensure createTiles method is called correctly and creates 136 tiles
        TileLibrary tileLibrary = new TileLibrary(playerList); // 初始化一个新的 tileLibrary 确保只调用 createTiles 一次
        assertEquals(136, tileLibrary.getTotalTiles().size());
    }

    @Test
    public void testDealInitialHand() {
        // Ensure initial hand dealing is correct
        tileLibrary.dealInitialHand();
        for (Player player : playerList) {
            assertEquals(13, player.getHandTile().getHandTile().size());
        }
        // Check that the number of remaining tiles is correct after dealing initial hands
        assertEquals(136 - 2 * 13 * playerList.size(), tileLibrary.getTiles().size());
    }

    @Test
    public void testFindTile() {
        // Ensure tiles can be found correctly by ID
        Tile tile = tileLibrary.findTile(111);
        assertNotNull(tile);
        assertEquals(111, tile.getId());
    }

    @Test
    public void testShuffle() {
        // Ensure shuffle method randomizes tile order
        ArrayList<Tile> originalTiles = new ArrayList<>(tileLibrary.getTiles());
        tileLibrary.shuffle(originalTiles);
        assertNotEquals(tileLibrary.getTiles(), originalTiles);
    }

    @Test
    public void testDeal() {
        // Ensure a tile is dealt correctly to the player
        Tile tile = tileLibrary.deal("Player1");
        assertNotNull(tile);
        assertEquals(14, playerList.get(0).getHandTile().getHandTile().size());
    }

    @Test
    public void testAddTableTile() {
        // Ensure a tile is added to the table tiles
        Tile tile = new Tile(111, true);
        tileLibrary.addTableTile(tile);
        assertEquals(1, tileLibrary.getTableTile().size());
        assertEquals(111, tileLibrary.getTableTile().get(0).getId());
    }

    @Test
    public void testGetTableTile() {
        // Ensure table tiles can be retrieved correctly
        Tile tile = new Tile(111, true);
        tileLibrary.addTableTile(tile);
        ArrayList<Tile> tableTiles = tileLibrary.getTableTile();
        assertEquals(1, tableTiles.size());
        assertEquals(111, tableTiles.get(0).getId());
    }

    @Test
    public void testGetTiles() {
        // Ensure remaining tiles can be retrieved correctly
        TileLibrary tileLibrary = new TileLibrary(playerList);
        assertEquals(136 - 13 * playerList.size(), tileLibrary.getTiles().size());
    }
}
