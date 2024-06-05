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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WebSocketServerTest {

    @InjectMocks
    private WebSocketServer webSocketServer;

    @Mock
    private GameService gameService;

    @Mock
    private Session session;

    @Mock
    private Basic basicRemote;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(session.getBasicRemote()).thenReturn(basicRemote);
        WebSocketServer.setGameService(gameService);
        WebSocketServer.setSessionMap(new ConcurrentHashMap<>());
        WebSocketServer.setRoomMap(new ConcurrentHashMap<>());
        WebSocketServer.setPlayerMap(new ConcurrentHashMap<>());
        WebSocketServer.setRoomSession(new ConcurrentHashMap<>());
        WebSocketServer.setSessionList(new ArrayList<>());
        WebSocketServer.setNoAffairNum(0);
        WebSocketServer.setDiscardPlayer(null);
    }

    @AfterEach
    public void tearDown() {
        webSocketServer.onClose(session);
    }

    @Test
    public void testOnOpen() {
        webSocketServer.onOpen(session);
        Assertions.assertTrue(WebSocketServer.getSessionList().contains(session));
    }

    @Test
    public void testOnClose() {
        webSocketServer.onOpen(session);
        webSocketServer.onClose(session);

        Assertions.assertFalse(WebSocketServer.getSessionList().contains(session));
    }

    @Test
    public void testOnMessage_Login() throws IOException {
        String message = "{\"operation\":\"login\", \"playerName\":\"testPlayer\"}";
        when(gameService.getGameRooms("testPlayer")).thenReturn("Test Rooms");

        webSocketServer.onMessage(message, session);

        verify(gameService, times(1)).getGameRooms("testPlayer");
        verify(basicRemote, times(1)).sendText("Test Rooms");
    }

    @Test
    public void testOnMessage_CreateRoom() throws IOException {
        String message = "{\"operation\":\"createRoom\", \"roomID\":\"testRoom\"}";
        WebSocketServer.getPlayerMap().put(session, "testPlayer");

        when(gameService.newRoom("testRoom")).thenReturn("success");

        webSocketServer.onMessage(message, session);

        verify(gameService, times(1)).newRoom("testRoom");
        verify(gameService, times(1)).addPlayer("testPlayer", "testRoom");
        verify(basicRemote, times(3)).sendText(anyString());
    }

    @Test
    public void testOnMessage_IntoRoom() throws IOException {
        String message = "{\"operation\":\"intoRoom\", \"roomID\":\"testRoom\"}";
        WebSocketServer.getPlayerMap().put(session, "testPlayer");
        WebSocketServer.getRoomSession().put("testRoom", new ArrayList<>());

        webSocketServer.onMessage(message, session);

        verify(gameService, times(1)).addPlayer("testPlayer", "testRoom");
        verify(basicRemote, times(3)).sendText(anyString());
    }

    @Test
    public void testOnMessage_EscRoom() throws IOException {
        String message = "{\"operation\":\"escRoom\"}";
        WebSocketServer.getPlayerMap().put(session, "testPlayer");
        WebSocketServer.getRoomMap().put(session, "testRoom");
        WebSocketServer.getRoomSession().put("testRoom", new ArrayList<>());
        WebSocketServer.getRoomSession().get("testRoom").add(session);

        webSocketServer.onMessage(message, session);

        verify(gameService, times(1)).removePlayer("testPlayer", "testRoom");
        verify(basicRemote, times(1)).sendText(anyString());
    }

    @Test
    public void testOnMessage_Prepare() throws IOException {
        String message = "{\"operation\":\"prepare\"}";
        WebSocketServer.getPlayerMap().put(session, "testPlayer");
        WebSocketServer.getRoomMap().put(session, "testRoom");
        WebSocketServer.getRoomSession().put("testRoom", new ArrayList<>());
        WebSocketServer.getRoomSession().get("testRoom").add(session);

        when(gameService.startGame("testRoom")).thenReturn(true);

        webSocketServer.onMessage(message, session);

        verify(gameService, times(1)).prepare("testPlayer", "testRoom");
        verify(gameService, times(1)).getHandTile("testPlayer", "testRoom");
        verify(gameService, times(1)).getMeld("testPlayer", "testRoom");
        verify(basicRemote, times(4)).sendText(anyString());
    }

    @Test
    public void testOnMessage_Discard() throws IOException {
        String message = "{\"operation\":\"discard\", \"tileID\":1}";
        WebSocketServer.getPlayerMap().put(session, "testPlayer");
        WebSocketServer.getRoomMap().put(session, "testRoom");
        WebSocketServer.getRoomSession().put("testRoom", new ArrayList<>());
        WebSocketServer.getRoomSession().get("testRoom").add(session);

        when(gameService.canHu(anyString(), anyString())).thenReturn("{\"msg\":\"{\"playerName\":\"testPlayer\"}\"}");

        webSocketServer.onMessage(message, session);

        verify(gameService, times(1)).discard("testRoom", "testPlayer", 1);
        verify(gameService, times(1)).getHandTile("testPlayer", "testRoom");
        verify(gameService, times(1)).getMeld("testPlayer", "testRoom");
        verify(basicRemote, times(4)).sendText(anyString());
    }

    @Test
    public void testSendMessageToAll() throws IOException {
        WebSocketServer.getSessionMap().put("testPlayer", session);
        webSocketServer.sendMessageToAll("Test Message");
        verify(basicRemote, times(1)).sendText("Test Message");
    }

    @Test
    public void testSendMessageToUser() throws IOException {
        webSocketServer.sendMessageToUser("Test Message", session);
        verify(basicRemote, times(1)).sendText("Test Message");
    }
}
