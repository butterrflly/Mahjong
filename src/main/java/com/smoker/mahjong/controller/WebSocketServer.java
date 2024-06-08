package com.smoker.mahjong.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.smoker.mahjong.service.GameService;
import com.smoker.mahjong.data.UserRegistration;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WebSocketServer class handles WebSocket connections and communications for the Mahjong game.
 * This includes managing user connections, handling messages, and interacting with the GameService.
 */
@Slf4j
@EnableScheduling
@ServerEndpoint("/ws")
@Component
public class WebSocketServer {

    private static GameService gameService;

    @Autowired
    private ApplicationContext context;

    /**
     * Initializes the GameService bean after the construction of the WebSocketServer.
     */
    @PostConstruct
    public void init() {
        gameService = context.getBean(GameService.class);
    }

    // Records the current number of connections
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    private static final Map<Session, String> roomMap = new ConcurrentHashMap<>();
    private static final Map<Session, String> playerMap = new ConcurrentHashMap<>();
    private static final Map<String, ArrayList<Session>> roomSession = new ConcurrentHashMap<>();
    private static final ArrayList<Session> sessionList = new ArrayList<>();

    private static Session discardPlayer;
    private static AtomicInteger noPangOrKong = new AtomicInteger(0);

    /**
     * Method called when a connection is established successfully.
     *
     * @param session the session of the connected user
     */
    @OnOpen
    public void onOpen(Session session) {
        sessionList.add(session);
        log.info("A new user has joined, sessionId = {}, current number of online users {}", session.getId(), sessionList.size());
        System.out.println("WebSocket opened: " + session.getId());
    }

    /**
     * Method to receive messages sent by the client.
     *
     * @param message the message sent by the client
     * @param session the session of the user who sent the message
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("Received message from client: " + message);
        System.out.println("Received message from client: " + message);

        if (!message.isEmpty()) {
            JSONObject jsonObject = JSON.parseObject(message);
            String operation = (String) jsonObject.get("operation");
            // Switch based on key 'operation'
            switch (operation) {
                case "login" -> {
                    String playerName = (String) jsonObject.get("playerName");
                    sessionMap.put(playerName, session); // Add the player to the session map
                    playerMap.put(session, playerName); // Add the session to the player map

                    sendMessageToUser(gameService.getGameRooms(playerName), session); // Send game rooms info to the player

                    log.info("A user has logged in, player name = {}", playerName);
                }
                case "logout" -> {
                    String playerName = playerMap.get(session);

                    sessionMap.remove(playerName); // Remove the player from the session map
                    playerMap.remove(session); // Remove the session from the player map

                    UserRegistration.logout(playerName); // Perform logout operation
                    log.info("A user has logged out, player name = {}", playerName);
                }
                case "createRoom" -> {
                    String playerName = playerMap.get(session);
                    String roomID = (String) jsonObject.get("roomID");

                    String result = gameService.newRoom(roomID); // Attempt to create a new room
                    if (!result.equals("success")) {
                        sendMessageToUser(result, session);
                        return;
                    }

                    roomMap.put(session, roomID); // Add the session to the room map
                    roomSession.put(roomID, new ArrayList<>()); // Initialize the session list for the room
                    roomSession.get(roomID).add(session); // Add the session to the room's session list

                    gameService.addPlayer(playerName, roomID); // Add the player to the room
                    sendMessageToAll(gameService.getGameRooms(playerName)); // Broadcast updated game rooms info
                    sendMessageToUser(gameService.getRoomPlayerMessage(playerName, roomID), session); // Send room player info to the player

                    log.info("Room created, player name = {}, room ID = {}", playerName, roomID);
                }
                case "intoRoom" -> {
                    String playerName = playerMap.get(session);
                    String roomID = (String) jsonObject.get("roomID");

                    roomMap.put(session, roomID); // Add the session to the room map
                    gameService.addPlayer(playerName, roomID); // Add the player to the room
                    roomSession.get(roomID).add(session); // Add the session to the room's session list

                    for (Session s : roomSession.get(roomID)) {
                        sendMessageToUser(gameService.getRoomPlayerMessage(playerMap.get(s), roomID), s); // Send room player info to all sessions in the room
                    }
                    sendMessageToAll(gameService.getGameRooms(playerName)); // Broadcast updated game rooms info

                    log.info("Entered room, player name = {}, room ID = {}", playerName, roomID);
                }
                case "escRoom" -> {
                    String escName = playerMap.get(session);
                    String escRoomID = roomMap.get(session);

                    gameService.removePlayer(escName, escRoomID); // Remove the player from the room
                    roomMap.remove(session); // Remove the session from the room map
                    roomSession.get(escRoomID).remove(session); // Remove the session from the room's session list

                    if (roomSession.get(escRoomID).size() == 0) {
                        gameService.removeRoom(escRoomID); // Remove the room if empty
                        roomSession.remove(escRoomID); // Remove the room's session list
                        return;
                    }

                    for (Session s : roomSession.get(escRoomID)) {
                        sendMessageToUser(gameService.getRoomPlayerMessage(playerMap.get(s), escRoomID), s); // Send updated room player info to all sessions in the room
                    }

                    log.info("Left room, player name = {}, room ID = {}", escName, escRoomID);
                }
                case "prepare" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    gameService.prepare(playerName, roomID); // Mark the player as prepared

                    log.info("Player ready, player name = {}, room ID = {}", playerName, roomID);

                    if (gameService.startGame(roomID)) {
                        for (Session s : roomSession.get(roomID)) {
                            sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s); // Send hand tile info to all sessions in the room
                            sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s); // Send meld info to all sessions in the room
                        }

                        gameService.deal(roomID); // Deal tiles for the room
                        deal(roomID); // Perform the dealing operation

                        return;
                    }
                    for (Session s : roomSession.get(roomID)) {
                        sendMessageToUser(gameService.getRoomPlayerMessage(playerMap.get(s), roomID), s); // Send updated room player info to all sessions in the room
                    }
                }
                case "discard" -> {
                    int tileID = (int) jsonObject.get("tileID");
                    discardPlayer = session; // Set the discard player

                    String discardPlayerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    gameService.discard(roomID, discardPlayerName, tileID); // Perform the discard operation

                    for (Session s : roomSession.get(roomID)) {
                        sendMessageToUser(gameService.discard(playerMap.get(s), roomID, discardPlayerName), s); // Send discard info to all sessions in the room

                        // Update hand tiles
                        sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s);
                        sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s);
                    }

                    String nextCanHu = gameService.canHu(discardPlayerName, roomID); // Check if the next player can Hu
                    String nextPlayerName = (String) JSON.parseObject(JSON.parseObject(nextCanHu).get("msg").toString()).get("playerName");
                    sendMessageToUser(nextCanHu, sessionMap.get(nextPlayerName)); // Send Hu info to the next player

                    log.info("Discarded a tile, player name = {}, room ID = {}, tile ID = {}", playerMap.get(session), roomMap.get(session), tileID);
                }
                case "noHu" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    String nextCanHu = gameService.canHu(playerName, roomID); // Check if the next player can Hu
                    String nextPlayerName = (String) JSON.parseObject(JSON.parseObject(nextCanHu).get("msg").toString()).get("playerName");

                    if (nextPlayerName.equals("null")) {
                        noPangOrKong.set(0); // Reset noPangOrKong counter

                        System.out.println(noPangOrKong.get());

                        for (Session s : roomSession.get(roomID)) {
                            if (s == discardPlayer)
                                continue;
                            sendMessageToUser(gameService.canPangOrKong(playerMap.get(s), roomID), s); // Check if the player can Pang or Kong
                        }
                        return;
                    }

                    sendMessageToUser(nextCanHu, sessionMap.get(nextPlayerName)); // Send Hu info to the next player
                }
                case "noPangOrKong" -> {
                    String roomID = roomMap.get(session);

                    int count = noPangOrKong.incrementAndGet(); // Increment noPangOrKong counter

                    if (count == 3) {
                        String canChow = gameService.canChow(roomID); // Check if the player can Chow
                        String playerName = (String) JSON.parseObject(JSON.parseObject(canChow).get("msg").toString()).get("playerName");
                        sendMessageToUser(canChow, sessionMap.get(playerName)); // Send Chow info to the player
                    }
                }
                case "noChow" -> {
                    String roomID = roomMap.get(session);

                    gameService.addTableTile(roomID); // Add the tile to the table

                    for (Session s : roomSession.get(roomID)) {
                        sendMessageToUser(gameService.getTableTile(roomID), s); // Send table tile info to all sessions in the room
                    }

                    gameService.deal(roomID); // Deal tiles for the room
                    deal(roomID); // Perform the dealing operation
                }
                case "Hu" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    gameService.Hu(playerName, roomID); // Perform the Hu operation

                    for (Session s : roomSession.get(roomID)) {
                        sendMessageToUser(gameService.Hu(playerMap.get(s), roomID, playerName), s); // Send Hu info to all sessions in the room
                        sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s); // Send hand tile info to all sessions in the room
                        sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s); // Send meld info to all sessions in the room
                        sendMessageToUser(gameService.getRoomPlayerMessage(playerMap.get(s), roomID), s); // Send room player info to all sessions in the room
                    }
                }
                case "Kong" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    String result = gameService.Kong(playerName, roomID); // Perform the Kong operation
                    if (result.equals("null")) {

                        for (Session s : roomSession.get(roomID)) {
                            // Update hand tiles
                            sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s);
                            sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s);
                        }

                        gameService.deal(roomID); // Deal tiles for the room
                        deal(roomID); // Perform the dealing operation
                    } else {
                        sendMessageToUser(result, session); // Send result to the player
                    }
                }
                case "selectKong" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);
                    int tileID = (int) jsonObject.get("tileID");

                    gameService.selfKong(playerName, roomID, tileID); // Perform the self Kong operation

                    for (Session s : roomSession.get(roomID)) {
                        // Update hand tiles
                        sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s);
                        sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s);
                    }

                    gameService.deal(roomID); // Deal tiles for the room
                    deal(roomID); // Perform the dealing operation
                }
                case "Chow" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    sendMessageToUser(gameService.Chow(playerName, roomID), session); // Perform the Chow operation
                }
                case "selectChow" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);
                    String tileIDListString = (String) jsonObject.get("tileIDList");
                    int[] tileIDList = Arrays.stream(tileIDListString.substring(1, 12).split(",")).mapToInt(Integer::parseInt).toArray();

                    gameService.Chow(playerName, roomID, tileIDList); // Perform the select Chow operation

                    for (Session s : roomSession.get(roomID)) {
                        // Update hand tiles
                        sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s);
                        sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s);
                    }

                    sendMessageToUser(gameService.discardRequest(), session); // Request discard
                }
                case "Pang" -> {
                    String playerName = playerMap.get(session);
                    String roomID = roomMap.get(session);

                    gameService.Pang(playerName, roomID); // Perform the Pang operation

                    for (Session s : roomSession.get(roomID)) {
                        // Update hand tiles
                        sendMessageToUser(gameService.getHandTile(playerMap.get(s), roomID), s);
                        sendMessageToUser(gameService.getMeld(playerMap.get(s), roomID), s);
                    }

                    sendMessageToUser(gameService.discardRequest(), session); // Request discard
                }
            }
        }
    }

    /**
     * Method called when a connection is closed.
     *
     * @param session the session of the user who closed the connection
     */
    @OnClose
    public void onClose(Session session) {
        String escName;
        String escRoomID;

        if (playerMap.containsKey(session)) {
            escName = playerMap.remove(session); // Remove the session from the player map
            sessionMap.remove(escName); // Remove the player from the session map
            UserRegistration.logout(escName); // Perform logout operation
        }

        if (roomMap.containsKey(session)) {
            escRoomID = roomMap.remove(session); // Remove the session from the room map
            roomSession.get(escRoomID).remove(session); // Remove the session from the room's session list

            if (roomSession.get(escRoomID).size() == 0) {
                gameService.removeRoom(escRoomID); // Remove the room if empty
                roomSession.remove(escRoomID); // Remove the room's session list
            }
        }

        sessionList.remove(session); // Remove the session from the session list
        log.info("A user has left, sessionId = {}, current number of online users {}", session.getId(), sessionMap.size());
        System.out.println("WebSocket closed: " + session.getId());
    }

    /**
     * Deals a tile to the next player in the specified room.
     *
     * @param roomID the ID of the room
     */
    public void deal(String roomID) {
        if (gameService.canDraw(roomID)) {
            for (Session s : roomSession.get(roomID)) {
                sendMessageToUser(gameService.Draw(), s); // Send draw info to all sessions in the room
                sendMessageToUser(gameService.getRoomPlayerMessage(playerMap.get(s), roomID), s); // Send room player info to all sessions in the room
            }
            return;
        }

        Session dealPlayer = null;
        for (Session s : roomSession.get(roomID)) {
            String result = gameService.deal(playerMap.get(s), roomID); // Deal a tile to the player
            sendMessageToUser(result, s); // Send the result to the player

            if (dealPlayer == null) {
                String discardPlayerName = (String) JSON.parseObject(JSON.parseObject(result).get("msg").toString()).get("playerName");
                if (playerMap.get(s).equals(discardPlayerName))
                    dealPlayer = s; // Set the deal player
            }
        }

        // Send a self-check to the player who drew the tile
        sendMessageToUser(gameService.getSelfAffair(playerMap.get(dealPlayer), roomID), dealPlayer);
        sendMessageToUser(gameService.discardRequest(), dealPlayer); // Request discard

        log.info("Drew a tile, player name = {}, room ID = {}", playerMap.get(dealPlayer), roomMap.get(dealPlayer));
    }

    /**
     * Sends a message to all connected clients.
     *
     * @param message the message to send
     */
    public void sendMessageToAll(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("Sending message to all clients: " + message);
                session.getBasicRemote().sendText(message); // Send message to the client
            }
        } catch (Exception e) {
            log.error("Error sending message to all clients: " + e);
        }
    }

    /**
     * Sends a message to a specific user with a timeout.
     *
     * @param message  the message to send
     * @param session  the session of the user
     */
    public void sendMessageToUser(String message, Session session) {
        long timeoutMillis = 1000; // Set the timeout duration
        Future<Void> future = session.getAsyncRemote().sendText(message); // Send message asynchronously
        log.info("Sending message to all clients: " + message);

        try {
            // Wait for the send to complete, with a timeout
            future.get(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            // Handle send timeout or other exceptions
            log.error("Error sending message to user: " + e);
        }
    }
}
