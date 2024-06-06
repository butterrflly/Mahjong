package com.smoker.mahjong.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.smoker.mahjong.service.GameService;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@EnableScheduling
@ServerEndpoint("/ws")
@Component
public class WebSocketServer {

    private static GameService gameService;

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init() {
        gameService = context.getBean(GameService.class);
    }



    /**
     *  记录当前连接个数
     */
    private static final Map<String , Session> sessionMap = new ConcurrentHashMap<>();
    private static final Map<Session , String> roomMap = new ConcurrentHashMap<>();
    private static final Map<Session , String> playerMap = new ConcurrentHashMap<>();
    private static final Map<String , ArrayList<Session>> roomSession = new ConcurrentHashMap<>();
    private static final ArrayList<Session> sessionList = new ArrayList<>();


    private static Session discardPlayer;
    private static int noPangOrKong;



    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        sessionList.add(session);
        log.info("有新用户加入， sessionId = {}， 当前在线人数{}", session.getId(), sessionList.size());
        System.out.println("WebSocket opened: " + session.getId());
    }

    /**
     * 接收客户端发送的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Received message from client: " + message);
        System.out.println("Received message from client: " + message);

        if(!message.isEmpty()){
            JSONObject jsonObject = JSON.parseObject(message);
            String operation = (String) jsonObject.get("operation");
            // switch based on key 'operation'
            switch (operation) {
                case "login" -> {
                    String playerName = (String) jsonObject.get("playerName");
                    sessionMap.put(playerName, session);
                    playerMap.put(session, playerName);

                    sendMessageToUser(gameService.getGameRooms(playerName), session);

                    log.info("有一用户登录，player name = {}", playerName);
                }
                case "createRoom" -> {
                    String playerName = playerMap.get(session);
                    String roomID = (String) jsonObject.get("roomID");

                    String result = gameService.newRoom(roomID);
                    if (!result.equals("success")) {
                        sendMessageToUser(result, session);
                        return;
                    }

                    roomMap.put(session, roomID);
                    roomSession.put(roomID, new ArrayList<>());
                    roomSession.get(roomID).add(session);

                    gameService.addPlayer(playerName, roomID);
                    sendMessageToAll(gameService.getGameRooms(playerName));
                    sendMessageToUser(gameService.getRoomPlayerMessage(playerName, roomID), session);

                    log.info("创建房间，player name = {}， 房间 ID = {}", playerName, roomID);
                }
                case "intoRoom" -> {
                    String playerName = playerMap.get(session);
                    String roomID = (String) jsonObject.get("roomID");

                    roomMap.put(session, roomID);
                    gameService.addPlayer(playerName, roomID);
                    roomSession.get(roomID).add(session);

                    for (Session s : roomSession.get(roomID)){
                        sendMessageToUser(gameService.getRoomPlayerMessage(playerMap.get(s), roomID), s);
                    }
                    sendMessageToAll(gameService.getGameRooms(playerName));

                    log.info("加入房间，player name = {}， 房间 ID = {}", playerName, roomID);
                }
                case "escRoom" -> {
                    String escName = playerMap.get(session);
                    String escRoomID = roomMap.get(session);

                    gameService.removePlayer(escName, escRoomID);
                    roomMap.remove(session);
                    roomSession.get(escRoomID).remove(session);

                    if (roomSession.get(escRoomID).size() == 0) {
                        gameService.removeRoom(escRoomID);
                        roomSession.remove(escRoomID);
                        return;
                    }

                    for (Session s : roomSession.get(escRoomID)){
                        sendMessageToUser(gameService.getRoomPlayerMessage(playerMap.get(s), escRoomID), s);
                    }

                    log.info("离开房间，player name = {}， 房间 ID = {}", escName, escRoomID);
                }
                case "prepare" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    gameService.prepare(playerName, roomID);

                    log.info("玩家准备，player name = {}， 房间 ID = {}", playerName, roomID);

                    if (gameService.startGame(roomID)){

                        for (Session s : roomSession.get(roomID)){
                            sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s);
                            sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s);
                        }

                        gameService.deal(roomID);
                        deal(roomID);

                        return;
                    }
                    for (Session s : roomSession.get(roomID)){
                        sendMessageToUser(gameService.getRoomPlayerMessage(playerMap.get(s), roomID), s);
                    }
                }
                case "discard" -> {
                    int tileID = (int) jsonObject.get("tileID");
                    discardPlayer = session;

                    String discardPlayerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    gameService.discard(roomID, discardPlayerName, tileID);

                    for (Session s : roomSession.get(roomID)){
                        sendMessageToUser(gameService.discard(playerMap.get(s), roomID, discardPlayerName), s);

                        // 更新手牌
                        sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s);
                        sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s);
                    }

                    String nextCanHu = gameService.canHu(discardPlayerName, roomID);
                    String nextPlayerName = (String) JSON.parseObject(JSON.parseObject(nextCanHu).get("msg").toString()).get("playerName");
                    sendMessageToUser(nextCanHu, sessionMap.get(nextPlayerName));

                    log.info("打张牌，player name = {}， 房间 ID = {}， 牌 ID = {}", playerMap.get(session), roomMap.get(session), tileID);

                }
                case "noHu" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    String nextCanHu = gameService.canHu(playerName, roomID);
                    String nextPlayerName = (String) JSON.parseObject(JSON.parseObject(nextCanHu).get("msg").toString()).get("playerName");

                    if (nextPlayerName.equals("null")){
                        noPangOrKong = 0;
                        for (Session s : roomSession.get(roomID)){
                            if (s == discardPlayer)
                                continue;
                            sendMessageToUser(gameService.canPangOrKong(playerMap.get(s), roomID), s);
                        }
                        return;
                    }

                    sendMessageToUser(nextCanHu, sessionMap.get(nextPlayerName));
                }
                case "noPangOrKong" -> {
                    String roomID = roomMap.get(session);

                    noPangOrKong++;
                    if (noPangOrKong == 3){
                        String canChow = gameService.canChow(roomID);
                        String playerName = (String) JSON.parseObject(JSON.parseObject(canChow).get("msg").toString()).get("playerName");
                        sendMessageToUser(canChow, sessionMap.get(playerName));
                    }
                }
                case "noChow" -> {
                    String roomID = roomMap.get(session);


                    gameService.addTableTile(roomID);

                    for (Session s : roomSession.get(roomID)){
                        sendMessageToUser(gameService.getTableTile(roomID), s);
                    }

                    gameService.deal(roomID);
                    deal(roomID);
                }
                case "Hu" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    gameService.Hu(playerName, roomID);

                    for (Session s : roomSession.get(roomID)){
                        sendMessageToUser(gameService.Hu(playerMap.get(s), roomID, playerName), s);
                        sendMessageToUser(gameService.getRoomPlayerMessage(playerMap.get(s), roomID), s);
                    }
                }
                case "Kong" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    String result = gameService.Kong(playerName, roomID);
                    if (result.equals("null")){

                        for (Session s : roomSession.get(roomID)){
                            // 更新手牌
                            sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s);
                            sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s);
                        }

                        gameService.deal(roomID);
                        deal(roomID);
                    }
                    else {
                        sendMessageToUser(result, session);
                    }
                }
                case "selectKong" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);
                    int tileID = (int) jsonObject.get("tileID");

                    gameService.selfKong(playerName, roomID, tileID);

                    for (Session s : roomSession.get(roomID)){
                        // 更新手牌
                        sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s);
                        sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s);
                    }

                    deal(roomID);
                }
                case "Chow" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    sendMessageToUser(gameService.Chow(playerName, roomID), session);
                }
                case "selectChow" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);
                    String tileIDListString = (String) jsonObject.get("tileIDList");
                    int[] tileIDList = Arrays.stream(tileIDListString.substring(1, 12).split(",")).mapToInt(Integer :: parseInt).toArray();


                    gameService.Chow(playerName, roomID, tileIDList);

                    for (Session s : roomSession.get(roomID)){
                        // 更新手牌
                        sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s);
                        sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s);
                    }

                    sendMessageToUser(gameService.discardRequest(), session);
                }
                case "Pang" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    gameService.Pang(playerName, roomID);

                    for (Session s : roomSession.get(roomID)){
                        // 更新手牌
                        sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s);
                        sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s);
                    }

                    sendMessageToUser(gameService.discardRequest(), session);
                }
            }
        }
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        String escName;
        String escRoomID;

        if (playerMap.containsKey(session)){
            escName = playerMap.remove(session);
            sessionMap.remove(escName);
        }


        if (roomMap.containsKey(session)) {
            escRoomID = roomMap.remove(session);
            roomSession.get(escRoomID).remove(session);

            if (roomSession.get(escRoomID).size() == 0) {
                gameService.removeRoom(escRoomID);
                roomSession.remove(escRoomID);
            }
        }

        sessionList.remove(session);
        log.info("有一用户离开， sessionId = {}， 当前在线人数{}", session.getId(), sessionMap.size());
        System.out.println("WebSocket closed: " + session.getId());
    }

    public void deal(String roomID){
        if (gameService.canDraw(roomID)){
            for (Session s : roomSession.get(roomID)){
                sendMessageToUser(gameService.Draw(), s);
                sendMessageToUser(gameService.getRoomPlayerMessage(playerMap.get(s), roomID), s);
            }
            return;
        }

        Session dealPlayer = null;
        for (Session s : roomSession.get(roomID)){
            String result = gameService.deal(playerMap.get(s), roomID);
            sendMessageToUser(result, s);

            if (dealPlayer == null) {
                String discardPlayerName = (String) JSON.parseObject(JSON.parseObject(result).get("msg").toString()).get("playerName");
                if (playerMap.get(s).equals(discardPlayerName))
                    dealPlayer = s;
            }
        }

        // 给抓牌的玩家 发送一次自我检测
        sendMessageToUser(gameService.getSelfAffair(playerMap.get(dealPlayer), roomID), dealPlayer);
        sendMessageToUser(gameService.discardRequest(), dealPlayer);

        log.info("摸张牌，player name = {}， 房间 ID = {}", playerMap.get(dealPlayer), roomMap.get(dealPlayer));
    }

    public void sendMessageToAll(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("Sending message to all clients: " +  message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("Error sending message to all clients: " + e);
        }
    }


    public void sendMessageToUser(String message, Session session) {
        try {
            if (session != null) {
                log.info("Sending message to user: " +  message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("Error sending message to user: " + e);
        }
    }
}
