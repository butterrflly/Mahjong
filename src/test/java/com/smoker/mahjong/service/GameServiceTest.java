package com.smoker.mahjong.service;

import com.alibaba.fastjson2.JSONObject;
import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.Player;
import com.smoker.mahjong.impl.GameStarter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameServiceTest {
// F7 P24 T31
    private GameService gameService;
    private GameStarter gameStarter;
    private Map<String, GameStarter> games;

    @BeforeEach
    public void setUp() {
        gameService = new GameService();
        games = new HashMap<>();
        gameStarter = mock(GameStarter.class);
        games.put("testRoom", gameStarter);

        // 使用反射设置私有字段
        try {
            var gamesField = GameService.class.getDeclaredField("games");
            gamesField.setAccessible(true);
            gamesField.set(gameService, games);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNewRoom_DuplicateRoom() {
        String result = gameService.newRoom("testRoom");
        String expected = "{\"operation\":\"Duplicate room number\"}";
        assertEquals(expected, result, "应返回房间重复操作结果");
    }

    @Test
    public void testNewRoom_Success() {
        String result = gameService.newRoom("newRoom");
        assertEquals("success", result, "应返回成功操作结果");
    }

    @Test
    public void testRemoveRoom() {
        gameService.removeRoom("testRoom");
        assertFalse(games.containsKey("testRoom"), "房间应被移除");
    }

    @Test
    public void testAddPlayer() {
        gameService.addPlayer("player1", "testRoom");
        verify(gameStarter, times(1)).addPlayer("player1");
    }

    @Test
    public void testRemovePlayer() {
        gameService.removePlayer("player1", "testRoom");
        verify(gameStarter, times(1)).removePlayer("player1");
    }

    @Test
    public void testGetPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        when(gameStarter.getPlayerList()).thenReturn(players);
        ArrayList<Player> result = gameService.getPlayers("testRoom");
        assertEquals(players, result, "应返回玩家列表");
    }

    @Test
    public void testPrepare() {
        Player player = mock(Player.class);
        when(gameStarter.findPlayer("player1")).thenReturn(player);
        gameService.prepare("player1", "testRoom");
        verify(player, times(1)).setPrepare();
    }

    @Test
    public void testStartGame() {
        when(gameStarter.startGame()).thenReturn(true);
        boolean result = gameService.startGame("testRoom");
        assertTrue(result, "应返回游戏开始结果");
    }

    @Test
    public void testGetDealPlayer() {
        Player player = new Player("dealPlayer");
        when(gameStarter.getDealPlayer()).thenReturn(player);
        String result = gameService.getDealPlayer("testRoom");
        assertEquals("dealPlayer", result, "应返回发牌玩家名字");
    }

    @Test
    public void testGetGameRooms() {
        when(gameStarter.getPlayerNum()).thenReturn(0);

        String result = gameService.getGameRooms("player1");
        String expected = "{\"operation\":\"getGameRooms\",\"name\":\"player1\",\"msg\":{\"room number\":1,\"room message\":{\"testRoom\":0}}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回游戏房间信息");
    }

    @Test
    public void testGetRoomPlayerMessage() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");
        Player player4 = new Player("player4");

        Player[] players = {player1, player2, player3, player4};
        when(gameStarter.getSequence("player1")).thenReturn(players);

        String result = gameService.getRoomPlayerMessage("player1", "testRoom");

        String expected = "{\"operation\":\"getRoomPlayerMessage\",\"msg\":{\"self\":{\"name\":\"player1\",\"prepare\":false,\"score\":5000},\"nextPlayer\":{\"name\":\"player2\",\"prepare\":false,\"score\":5000},\"oppositePlayer\":{\"name\":\"player3\",\"prepare\":false,\"score\":5000},\"prevPlayer\":{\"name\":\"player4\",\"prepare\":false,\"score\":5000}}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回房间玩家信息");
    }

    @Test
    public void testGetHandTile() {
        Player player = new Player("player1");
        HandTile handTile = new HandTile();
        handTile.addTile(new Tile(101, false));
        player.addInitialHand(new Tile(101, false));
        Player[] players = {player, null, null, null};

        when(gameStarter.getSequence("player1")).thenReturn(players);

        String result = gameService.getHandTile("player1", "testRoom");
        String expected = "{\"operation\":\"getHandTile\",\"msg\":{\"self\":{\"name\":\"player1\",\"handTile number\":14,\"handTile\":[101]},\"nextPlayer\":{\"name\":\"\",\"handTile number\":0,\"handTile\":[]},\"oppositePlayer\":{\"name\":\"\",\"handTile number\":0,\"handTile\":[]},\"prevPlayer\":{\"name\":\"\",\"handTile number\":0,\"handTile\":[]}}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回玩家手牌信息");
    }

    @Test
    public void testGetMeld() {
        Player player = new Player("player1");
        Meld meld = new Meld(new ArrayList<>(Arrays.asList(new Tile(101, false), new Tile(101, false), new Tile(101, false))), "Pang", false);
        player.getHandTile().getMelds().add(meld);
        Player[] players = {player, null, null, null};

        when(gameStarter.getSequence("player1")).thenReturn(players);

        String result = gameService.getMeld("player1", "testRoom");

        String expected = "{\"operation\":\"getMeld\",\"msg\":{\"self\":{\"name\":\"player1\",\"meld number\":1,\"meld number list\":[3],\"isHide list\":[false],\"melds\":[[101,101,101]]},\"nextPlayer\":{\"name\":\"\",\"meld number\":0,\"meld number list\":[],\"isHide list\":[],\"melds\":[]},\"oppositePlayer\":{\"name\":\"\",\"meld number\":0,\"meld number list\":[],\"isHide list\":[],\"melds\":[]},\"prevPlayer\":{\"name\":\"\",\"meld number\":0,\"meld number list\":[],\"isHide list\":[],\"melds\":[]}}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回玩家吃碰杠信息");
    }

    @Test
    public void testGetTableTile() {
        ArrayList<Tile> tableTiles = new ArrayList<>();
        tableTiles.add(new Tile(101, false));
        when(gameStarter.getTableTile()).thenReturn(tableTiles);

        String result = gameService.getTableTile("testRoom");
        String expected = "{\"operation\":\"getTableTile\",\"msg\":{\"tableTile\":[101]}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回桌面牌信息");
    }

    @Test
    public void testDeal() {
        gameService.deal("testRoom");
        verify(gameStarter, times(1)).deal();
    }

    @Test
    public void testDealPlayer() {
        Player player = new Player("dealPlayer");
        when(gameStarter.getDealPlayer()).thenReturn(player);
        when(gameStarter.getDealTileID()).thenReturn(101);

        Player[] players = {player, null, null, null};

        when(gameStarter.getSequence("player1")).thenReturn(players);

        String result = gameService.deal("player1", "testRoom");
        String expected = "{\"operation\":\"deal\",\"msg\":{\"position\":\"self\",\"playerName\":\"dealPlayer\",\"tileID\":101}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回发牌信息");
    }

    @Test
    public void testGetSelfAffair() {
        when(gameStarter.canKong("player1")).thenReturn(new ArrayList<>());
        when(gameStarter.canHu("player1", 101)).thenReturn(true);
        when(gameStarter.getDealTileID()).thenReturn(101);

        String result = gameService.getSelfAffair("player1", "testRoom");
        String expected = "{\"operation\":\"getSelfAffair\",\"msg\":{\"playerName\":\"player1\",\"canKong\":true,\"canHu\":true}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回玩家自摸信息");
    }

    @Test
    public void testDiscardRequest() {
        String result = gameService.discardRequest();
        String expected = "{\"operation\":\"discard\",\"msg\":\"你怎么还不出牌啊，我等的花都谢了\"}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回弃牌请求信息");
    }

    @Test
    public void testDiscardPlayer() {
        Player player = new Player("player1");
        Player[] players = {player};

        when(gameStarter.getSequence("player1")).thenReturn(players);
        when(gameStarter.getDiscardTileID()).thenReturn(101);
        when(gameStarter.getDiscardPlayerName()).thenReturn("player1");

        String result = gameService.discard("player1", "testRoom", "player1");
        String expected = "{\"operation\":\"discard\",\"msg\":{\"position\":\"self\",\"playerName\":\"player1\",\"tileID\":101}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回弃牌信息");
    }

    @Test
    public void testCanHu() {
        Player player = new Player("player1");
        Player[] players = {player};

        when(gameStarter.getSequence("player1")).thenReturn(players);
        when(gameStarter.getDiscardTileID()).thenReturn(101);
        when(gameStarter.canHu("player1", 101)).thenReturn(true);

        String result = gameService.canHu("player1", "testRoom");
        String expected = "{\"operation\":\"canHu\",\"msg\":{\"playerName\":\"player1\",\"canHu\":true}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回可胡牌信息");
    }

    @Test
    public void testGetAffair() {
        Player player = new Player("player1");
        Player[] players = {player, player, player, player};

        when(gameStarter.getSequence("player1")).thenReturn(players);
        when(gameStarter.getDiscardTileID()).thenReturn(101);
        when(gameStarter.canPang("player1", 101)).thenReturn(true);
        when(gameStarter.canKong("player1", 101)).thenReturn(true);

        String result = gameService.getAffair("player1", "testRoom");
        String expected = "{\"operation\":\"getAffair\",\"msg\":{\"playerName\":\"player1\",\"canPang\":true,\"canKong\":true,\"canChow\":false}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回玩家吃碰杠信息");
    }

    @Test
    public void testHu() {
        when(gameStarter.getDiscardTileID()).thenReturn(0);
        when(gameStarter.getDealTileID()).thenReturn(101);
        gameService.Hu("player1", "testRoom");
        verify(gameStarter, times(1)).Hu("player1", 101, "allPlayers");
    }

    @Test
    public void testHuPlayer() {
        Player player = new Player("player1");
        Player[] players = {player};

        when(gameStarter.getSequence("player1")).thenReturn(players);
        when(gameStarter.findPlayer("player1")).thenReturn(player);

        String result = gameService.Hu("player1", "testRoom", "player1");
        String expected = "{\"operation\":\"Hu\",\"msg\":{\"position\":\"self\",\"playerName\":\"player1\"}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回胡牌信息");
    }

    @Test
    public void testKong() {
        Player player = new Player("player1");
        ArrayList<Tile> kongTiles = new ArrayList<>();
        kongTiles.add(new Tile(101, false));

        when(gameStarter.canKong("player1")).thenReturn(kongTiles);

        String result = gameService.Kong("player1", "testRoom");
        String expected = "{\"operation\":\"Kong\",\"msg\":{\"playerName\":\"player1\",\"KongNum\":1,\"KongList\":[101]}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回杠牌信息");
    }

    @Test
    public void testSelfKong() {
        gameService.selfKong("player1", "testRoom", 101);
        verify(gameStarter, times(1)).Kong("player1", 101);
    }

    @Test
    public void testChow() {
        Player player = new Player("player1");
        ArrayList<ArrayList<Tile>> chowTiles = new ArrayList<>();
        chowTiles.add(new ArrayList<>(Arrays.asList(new Tile(101, false), new Tile(102, false), new Tile(103, false))));

        when(gameStarter.canChow("player1", 101)).thenReturn(chowTiles);

        String result = gameService.Chow("player1", "testRoom");
        String expected = "{\"operation\":\"Chow\",\"msg\":{\"playerName\":\"player1\",\"ChowNum\":1,\"ChowList\":[[101,102,103]]}}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回吃牌信息");
    }

    @Test
    public void testChowPlayer() {
        gameService.Chow("player1", "testRoom", new int[]{101, 102, 103});
        verify(gameStarter, times(1)).Chow(any(ArrayList.class), eq("player1"), eq(101));
    }

    @Test
    public void testPang() {
        when(gameStarter.getDiscardTileID()).thenReturn(101);
        gameService.Pang("player1", "testRoom");
        verify(gameStarter, times(1)).Pang("player1", 101);
    }

    @Test
    public void testCanDraw() {
        when(gameStarter.canDraw()).thenReturn(true);
        Boolean result = gameService.canDraw("testRoom");
        assertTrue(result, "应返回可以荒牌");
    }

    @Test
    public void testDraw() {
        String result = gameService.Draw();
        String expected = "{\"operation\":\"Draw\",\"msg\":\"Game Over\"}";
        assertEquals(JSONObject.parse(expected), JSONObject.parse(result), "应返回荒牌信息");
    }

    @Test
    public void testAddTableTile() {
        gameService.addTableTile("testRoom");
        verify(gameStarter, times(1)).addTableTile();
    }
}
