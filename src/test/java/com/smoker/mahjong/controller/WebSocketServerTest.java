package com.smoker.mahjong.controller;

import com.smoker.mahjong.service.GameService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.websocket.Session;
import jakarta.websocket.RemoteEndpoint.Basic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebSocketServerTest {
// F6 P4 T10

    @InjectMocks
    private WebSocketServer webSocketServer;

    @Mock
    private GameService gameService;

    @Mock
    private Session session1, session2, session3, session4;

    @Mock
    private Basic basicRemote1, basicRemote2, basicRemote3, basicRemote4;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        // 为每个会话设置基本的远程端点
        lenient().when(session1.getBasicRemote()).thenReturn(basicRemote1);
        lenient().when(session2.getBasicRemote()).thenReturn(basicRemote2);
        lenient().when(session3.getBasicRemote()).thenReturn(basicRemote3);
        lenient().when(session4.getBasicRemote()).thenReturn(basicRemote4);

        // 初始化 WebSocketServer 的静态变量
        WebSocketServer.setGameService(gameService);
        WebSocketServer.setSessionMap(new ConcurrentHashMap<>());
        WebSocketServer.setRoomMap(new ConcurrentHashMap<>());
        WebSocketServer.setPlayerMap(new ConcurrentHashMap<>());
        WebSocketServer.setRoomSession(new ConcurrentHashMap<>());
        WebSocketServer.setSessionList(new ArrayList<>());
        WebSocketServer.setNoAffairNum(0);
        WebSocketServer.setDiscardPlayer(null);
    }

    @Test
    public void testOnOpen() {
        // 测试 onOpen 方法，确保 session 被添加到 sessionList 中
        webSocketServer.onOpen(session1);
        Assertions.assertTrue(WebSocketServer.getSessionList().contains(session1));
    }

    @Test
    public void testOnClose() {
        // 测试 onClose 方法，确保 session 从 sessionList 中移除
        webSocketServer.onOpen(session1);
        webSocketServer.onClose(session1);
        Assertions.assertFalse(WebSocketServer.getSessionList().contains(session1));
    }

    @Test
    public void testOnMessage_Login() throws IOException {
        // 测试登录操作
        String message = "{\"operation\":\"login\", \"playerName\":\"testPlayer\"}";
        lenient().when(gameService.getGameRooms("testPlayer")).thenReturn("Test Rooms");

        webSocketServer.onMessage(message, session1);

        // 验证 gameService.getGameRooms 方法被调用了一次
        verify(gameService, times(1)).getGameRooms("testPlayer");
        // 验证 basicRemote.sendText 方法被调用了一次，并发送了 "Test Rooms" 消息
        verify(basicRemote1, times(1)).sendText("Test Rooms");
    }

    @Test
    public void testOnMessage_CreateRoom() throws IOException {
        // 测试创建房间操作
        String message = "{\"operation\":\"createRoom\", \"roomID\":\"testRoom\"}";
        WebSocketServer.getPlayerMap().put(session1, "testPlayer");

        // 设置 lenient 来避免严格匹配
        lenient().when(gameService.newRoom("testRoom")).thenReturn("success");
        lenient().when(gameService.getGameRooms("testPlayer")).thenReturn("Test Rooms");
        lenient().when(gameService.getRoomPlayerMessage("testPlayer", "testRoom")).thenReturn("Player Message");

        webSocketServer.onMessage(message, session1);

        // 验证调用了 GameService 的 newRoom 和 addPlayer 方法
        verify(gameService, times(1)).newRoom("testRoom");
        verify(gameService, times(1)).addPlayer("testPlayer", "testRoom");
        // 验证发送了三次消息（一次给所有人，两次给特定用户）
        verify(basicRemote1, times(3)).sendText(anyString());
    }

    @Test
    public void testOnMessage_IntoRoom() throws IOException {
        // 测试加入房间操作
        String message = "{\"operation\":\"intoRoom\", \"roomID\":\"testRoom\"}";
        WebSocketServer.getPlayerMap().put(session1, "testPlayer");
        WebSocketServer.getRoomSession().put("testRoom", new ArrayList<>());

        lenient().when(gameService.getGameRooms("testPlayer")).thenReturn("Test Rooms");
        lenient().when(gameService.getRoomPlayerMessage("testPlayer", "testRoom")).thenReturn("Player Message");

        webSocketServer.onMessage(message, session1);

        // 验证调用了 GameService 的 addPlayer 方法
        verify(gameService, times(1)).addPlayer("testPlayer", "testRoom");
        // 验证发送了三次消息
        verify(basicRemote1, times(3)).sendText(anyString());
    }

    @Test
    public void testOnMessage_EscRoom() throws IOException {
        // 测试离开房间操作
        String message = "{\"operation\":\"escRoom\"}";
        WebSocketServer.getPlayerMap().put(session1, "testPlayer");
        WebSocketServer.getRoomMap().put(session1, "testRoom");
        WebSocketServer.getRoomSession().put("testRoom", new ArrayList<>());
        WebSocketServer.getRoomSession().get("testRoom").add(session1);

        webSocketServer.onMessage(message, session1);

        // 验证调用了 GameService 的 removePlayer 方法
        verify(gameService, times(1)).removePlayer("testPlayer", "testRoom");
        // 验证发送了一次消息
        verify(basicRemote1, times(1)).sendText(anyString());
    }

    @Test
    public void testOnMessage_Prepare() throws IOException {
        // 测试准备操作
        String message = "{\"operation\":\"prepare\"}";
        WebSocketServer.getPlayerMap().put(session1, "testPlayer");
        WebSocketServer.getRoomMap().put(session1, "testRoom");
        WebSocketServer.getRoomSession().put("testRoom", new ArrayList<>());
        WebSocketServer.getRoomSession().get("testRoom").add(session1);

        lenient().when(gameService.startGame("testRoom")).thenReturn(true);
        lenient().when(gameService.getHandTile("testPlayer", "testRoom")).thenReturn("Hand Tile");
        lenient().when(gameService.getMeld("testPlayer", "testRoom")).thenReturn("Meld");

        webSocketServer.onMessage(message, session1);

        // 验证调用了 GameService 的 prepare 方法
        verify(gameService, times(1)).prepare("testPlayer", "testRoom");
        // 验证调用了 GameService 的 getHandTile 和 getMeld 方法
        verify(gameService, times(1)).getHandTile("testPlayer", "testRoom");
        verify(gameService, times(1)).getMeld("testPlayer", "testRoom");
        // 验证发送了四次消息
        verify(basicRemote1, times(4)).sendText(anyString());
    }

    @Test
    public void testOnMessage_Discard() throws IOException {
        // 测试打牌操作
        String message = "{\"operation\":\"discard\", \"tileID\":1}";
        WebSocketServer.getPlayerMap().put(session1, "testPlayer");
        WebSocketServer.getRoomMap().put(session1, "testRoom");
        WebSocketServer.getRoomSession().put("testRoom", new ArrayList<>());
        WebSocketServer.getRoomSession().get("testRoom").add(session1);

        // 设置 lenient 来避免严格匹配
        lenient().when(gameService.canHu(anyString(), anyString())).thenReturn("{\"msg\":\"{\\\"playerName\\\":\\\"testPlayer\\\"}\"}");
        lenient().when(gameService.getHandTile("testPlayer", "testRoom")).thenReturn("Hand Tile");
        lenient().when(gameService.getMeld("testPlayer", "testRoom")).thenReturn("Meld");
        doNothing().when(gameService).discard(anyString(), anyString(), anyInt());

        webSocketServer.onMessage(message, session1);

        // 验证调用了 GameService 的 discard 方法
        verify(gameService, times(1)).discard("testRoom", "testPlayer", 1);
        // 验证调用了 GameService 的 getHandTile 和 getMeld 方法
        verify(gameService, times(1)).getHandTile("testPlayer", "testRoom");
        verify(gameService, times(1)).getMeld("testPlayer", "testRoom");
        // 验证发送了四次消息
        verify(basicRemote1, times(4)).sendText(anyString());
    }

    @Test
    public void testSendMessageToAll() throws IOException {
        // 测试发送消息给所有用户
        WebSocketServer.getSessionMap().put("player1", session1);
        WebSocketServer.getSessionMap().put("player2", session2);
        WebSocketServer.getSessionMap().put("player3", session3);
        WebSocketServer.getSessionMap().put("player4", session4);
        webSocketServer.sendMessageToAll("Test Message");

        // 验证每个会话发送了一次消息
        verify(basicRemote1, times(1)).sendText("Test Message");
        verify(basicRemote2, times(1)).sendText("Test Message");
        verify(basicRemote3, times(1)).sendText("Test Message");
        verify(basicRemote4, times(1)).sendText("Test Message");
    }

    @Test
    public void testSendMessageToUser() throws IOException {
        // 测试发送消息给特定用户
        webSocketServer.sendMessageToUser("Test Message", session1);
        verify(basicRemote1, times(1)).sendText("Test Message");
    }
}
