package com.smoker.mahjong.service;

import com.alibaba.fastjson2.JSONObject;
import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.Player;
import com.smoker.mahjong.impl.GameStarter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service // Annotation indicating this is a service class and should be injected into the Spring container
public class GameService {
    private final Map<String, GameStarter> games = new HashMap<>(); // Stores the game instances with room IDs as keys

    /**
     * Creates a new room if the room ID does not already exist
     * @return {"operation" : "Duplicate room number"} if the room ID exists, otherwise "success"
     */
    public String newRoom(String roomID) {
        if (games.containsKey(roomID)) {
            Map<String, Object> result = new HashMap<>();
            result.put("operation", "Duplicate room number");
            return JSONObject.toJSONString(result);
        }
        games.put(roomID, new GameStarter(roomID)); // Create and add a new GameStarter instance for the room
        return "success";
    }

    /**
     * Removes a room by its ID
     */
    public void removeRoom(String roomID) {
        games.remove(roomID);
    }

    /**
     * Adds a player to a room
     */
    public void addPlayer(String playName, String roomID) {
        games.get(roomID).addPlayer(playName);
    }

    /**
     * Removes a player from a room
     */
    public void removePlayer(String playName, String roomID) {
        games.get(roomID).removePlayer(playName);
    }

    /**
     * Gets the list of players in a room
     */
    public ArrayList<Player> getPlayers(String roomID) {
        return games.get(roomID).getPlayerList();
    }

    /**
     * Sets a player as prepared in a room
     */
    public void prepare(String playName, String roomID) {
        games.get(roomID).findPlayer(playName).setPrepare();
    }

    /**
     * Starts a game in a room if possible
     */
    public boolean startGame(String roomID) {
        return games.get(roomID).startGame();
    }

    /**
     * Gets the name of the player who is currently dealing in a room
     */
    public String getDealPlayer(String roomID) {
        return games.get(roomID).getDealPlayer().getName();
    }

    /**
     * Retrieves information about all game rooms
     * @return JSON string containing the operation and room messages
     */
    public String getGameRooms(String playerName) {
        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getGameRooms");

        Map<String, Object> message = new HashMap<>();
        result.put("name", playerName);
        message.put("room number", games.size());

        Map<String, Object> roomMessage = new HashMap<>();

        if (games.size() != 0) {
            for (String key : games.keySet()) {
                roomMessage.put(key, games.get(key).getPlayerNum()); // Add player count for each room
            }
        }

        message.put("room message", roomMessage);

        result.put("msg", message);
        return JSONObject.toJSONString(result);
    }

    /**
     * Retrieves player information for a specific room
     * @return JSON string containing the operation and player messages
     */
    public String getRoomPlayerMessage(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);
        Player[] players = gs.getSequence(playerName);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getRoomPlayerMessage");

        Map<String, Object> message = new HashMap<>();
        String[] positions = {"self", "nextPlayer", "oppositePlayer", "prevPlayer"};

        for (int i = 0; i < 4; i++) {
            Map<String, Object> playerMessage = new HashMap<>();
            if (players[i] == null) {
                playerMessage.put("name", "");
                playerMessage.put("prepare", false);
                playerMessage.put("score", 0);
                message.put(positions[i], playerMessage);
                continue;
            }
            playerMessage.put("name", players[i].getName());
            playerMessage.put("prepare", players[i].isPrepare());
            playerMessage.put("score", players[i].getScore());
            message.put(positions[i], playerMessage);
        }

        result.put("msg", message);
        return JSONObject.toJSONString(result);
    }

    /**
     * Retrieves the hand tiles of players in a specific room
     * @return JSON string containing the operation and hand tile messages
     */
    public String getHandTile(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);
        Player[] players = gs.getSequence(playerName);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getHandTile");

        Map<String, Object> message = new HashMap<>();
        String[] positions = {"self", "nextPlayer", "oppositePlayer", "prevPlayer"};

        for (int i = 0; i < 4; i++) {
            Map<String, Object> playerMessage = new HashMap<>();
            playerMessage.put("name", players[i].getName());
            playerMessage.put("handTile number", players[i].getHandTile().getHandTile().size());
            playerMessage.put("handTile", players[i].getHandTile().getHandTile().stream().mapToInt(Tile::getId).toArray());
            message.put(positions[i], playerMessage);
        }

        result.put("msg", message);
        return JSONObject.toJSONString(result);
    }

    /**
     * Retrieves the melds of players in a specific room
     * @return JSON string containing the operation and meld messages
     */
    public String getMeld(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);
        Player[] players = gs.getSequence(playerName);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getMeld");

        Map<String, Object> message = new HashMap<>();
        String[] positions = {"self", "nextPlayer", "oppositePlayer", "prevPlayer"};

        for (int i = 0; i < 4; i++) {
            Map<String, Object> playerMessage = new HashMap<>();
            playerMessage.put("name", players[i].getName());

            ArrayList<Meld> melds = players[i].getHandTile().getMelds();

            int meldNumber = melds.size();
            playerMessage.put("meld number", meldNumber);

            int[] meldNumberList = new int[meldNumber];
            boolean[] isHideList = new boolean[meldNumber];
            ArrayList<int[]> meldArray = new ArrayList<>();

            for (int j = 0; j < meldNumber; j++) {
                meldNumberList[j] = melds.get(j).getMeld().size();
                isHideList[j] = melds.get(j).getHide();
                meldArray.add(melds.get(j).getMeld().stream().mapToInt(Tile::getId).toArray());
            }

            playerMessage.put("meld number list", meldNumberList);
            playerMessage.put("isHide list", isHideList);

            message.put(positions[i], playerMessage);
            message.put(positions[i] + "Melds", meldArray);
        }

        result.put("msg", message);
        return JSONObject.toJSONString(result);
    }

    /**
     * Retrieves the tiles on the table in a specific room
     * @return JSON string containing the operation and table tile messages
     */
    public String getTableTile(String roomID) {
        GameStarter gs = games.get(roomID);
        ArrayList<Tile> tableTile = gs.getTableTile();

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getTableTile");

        Map<String, Object> mag = new HashMap<>();
        int[] tableTileArray = tableTile.stream().mapToInt(Tile::getId).toArray();
        mag.put("tableTile", tableTileArray);

        result.put("msg", mag);

        return JSONObject.toJSONString(result);
    }

    /**
     * Deals a tile to the current player in a specific room
     */
    public void deal(String roomID) {
        GameStarter gs = games.get(roomID);
        gs.deal();
    }

    /**
     * Deals a tile to the current player and provides information about the deal
     * @return JSON string containing the operation and deal messages
     */
    public String deal(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);
        Player[] players = gs.getSequence(playerName);

        Player dealPlayer = gs.getDealPlayer();
        int dealTileID = gs.getDealTileID();

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "deal");

        Map<String, Object> msg = new HashMap<>();
        String[] positions = {"self", "nextPlayer", "oppositePlayer", "prevPlayer"};
        for (int i = 0; i < 4; i++)
            if (players[i] == dealPlayer)
                msg.put("position", positions[i]);
        msg.put("playerName", dealPlayer.getName());
        msg.put("tileID", dealTileID);

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }

    /**
     * Retrieves self-affair information for a player in a specific room
     * @return JSON string containing the operation and self-affair messages
     */
    public String getSelfAffair(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getSelfAffair");

        Map<String, Object> msg = new HashMap<>();
        msg.put("playerName", playerName);

        msg.put("canKong", gs.canKong(playerName).size() != 0);
        msg.put("canHu", gs.canHu(playerName, gs.getDealTileID()));

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }

    /**
     * Requests the player to discard a tile
     * @return JSON string containing the discard request message
     */
    public String discardRequest() {
        Map<String, Object> result = new HashMap<>();
        result.put("operation", "discardRequest");
        result.put("msg", "你怎么还不出牌啊，我等的花都谢了"); // Translation: "Why haven't you discarded a tile yet? I've been waiting forever."

        return JSONObject.toJSONString(result);
    }

    /**
     * Discards a tile for a player in a specific room
     */
    public void discard(String roomID, String discardPlayerName, int TileID) {
        GameStarter gs = games.get(roomID);
        gs.discard(discardPlayerName, TileID);
    }

    /**
     * Provides information about the discarded tile
     * @return JSON string containing the operation and discard messages
     */
    public String discard(String playerName, String roomID, String discardPlayerName) {
        GameStarter gs = games.get(roomID);
        Player[] players = gs.getSequence(playerName);
        Player discardPlayer = gs.findPlayer(discardPlayerName);
        int discardTileID = gs.getDiscardTileID();

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "discard");

        Map<String, Object> msg = new HashMap<>();
        String[] positions = {"self", "nextPlayer", "oppositePlayer", "prevPlayer"};

        for (int i = 0; i < 4; i++)
            if (players[i] == discardPlayer)
                msg.put("position", positions[i]);
        msg.put("playerName", discardPlayer.getName());
        msg.put("tileID", discardTileID);

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }

    /**
     * Checks if the next player can Hu (win) after a tile is discarded
     * @return JSON string containing the operation and Hu messages
     */
    public String canHu(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);
        int discardTileID = gs.getDiscardTileID();

        Player[] players = gs.getSequence(playerName);
        Player nextPlayer = players[1];

        if (nextPlayer.getName().equals(gs.getDiscardPlayerName())) {
            Map<String, Object> result = new HashMap<>();
            result.put("operation", "canHu");

            Map<String, Object> msg = new HashMap<>();
            msg.put("playerName", "null");
            result.put("msg", msg);
            return JSONObject.toJSONString(result);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "canHu");

        Map<String, Object> msg = new HashMap<>();
        msg.put("playerName", nextPlayer.getName());
        msg.put("canHu", games.get(roomID).canHu(nextPlayer.getName(), discardTileID));

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }

    /**
     * Checks if the current player can Pang or Kong
     * @return JSON string containing the operation and Pang or Kong messages
     */
    public String canPangOrKong(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);

        int discardTileID = gs.getDiscardTileID();

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getPangOrKong");

        Map<String, Object> msg = new HashMap<>();
        msg.put("playerName", playerName);
        msg.put("canPang", gs.canPang(playerName, discardTileID));
        msg.put("canKong", gs.canKong(playerName, discardTileID));

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }

    /**
     * Checks if the next player can Chow after a tile is discarded
     * @return JSON string containing the operation and Chow messages
     */
    public String canChow(String roomID) {
        GameStarter gs = games.get(roomID);

        String playerName = gs.getSequence(gs.getDiscardPlayerName())[1].getName();
        int discardTileID = gs.getDiscardTileID();

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "canChow");

        Map<String, Object> msg = new HashMap<>();

        msg.put("playerName", playerName);
        msg.put("canChow", gs.canChow(playerName, discardTileID).size() != 0);

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }

    /**
     * Hu (win) for a player in a specific room
     */
    public void Hu(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);

        if (gs.getDiscardTileID() == 0)
            gs.Hu(playerName, gs.getDealTileID(), "allPlayers");
        else
            gs.Hu(playerName, gs.getDiscardTileID(), gs.getDiscardPlayerName());
    }

    /**
     * Provides information about the Hu (win) operation
     * @return JSON string containing the operation and Hu messages
     */
    public String Hu(String playerName, String roomID, String winnerPlayerName) {
        GameStarter gs = games.get(roomID);
        Player[] players = gs.getSequence(playerName);
        Player winnerPlayer = gs.findPlayer(winnerPlayerName);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "Hu");

        Map<String, Object> msg = new HashMap<>();
        String[] positions = {"self", "nextPlayer", "oppositePlayer", "prevPlayer"};

        for (int i = 0; i < 4; i++)
            if (players[i] == winnerPlayer)
                msg.put("position", positions[i]);
        msg.put("playerName", winnerPlayer.getName());

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }

    /**
     * Retrieves Kong operation information for a player
     * @return JSON string containing the operation and Kong messages
     */
    public String Kong(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);

        if (gs.getDiscardTileID() == 0) {
            // The player Kong themselves
            ArrayList<Tile> kongTiles = gs.canKong(playerName);
            Map<String, Object> result = new HashMap<>();
            result.put("operation", "Kong");

            Map<String, Object> msg = new HashMap<>();
            msg.put("playerName", playerName);
            msg.put("KongNum", kongTiles.size());

            int[] KongList = kongTiles.stream().mapToInt(Tile::getId).toArray();
            msg.put("KongList", KongList);

            result.put("msg", msg);

            return JSONObject.toJSONString(result);
        } else {
            gs.Kong(playerName, gs.getDiscardTileID());
            return "null";
        }
    }

    /**
     * Handles self Kong operation for a player
     */
    public void selfKong(String playerName, String roomID, int tileID) {
        GameStarter gs = games.get(roomID);
        gs.Kong(playerName, tileID);
    }

    /**
     * Retrieves Chow operation information for a player
     * @return JSON string containing the operation and Chow messages
     */
    public String Chow(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);

        ArrayList<ArrayList<Tile>> chowTiles = gs.canChow(playerName, gs.getDiscardTileID());
        Map<String, Object> result = new HashMap<>();
        result.put("operation", "Chow");

        Map<String, Object> msg = new HashMap<>();
        msg.put("playerName", playerName);
        msg.put("ChowNum", chowTiles.size());

        int[][] KongList = new int[chowTiles.size()][3];
        for (int i = 0; i < chowTiles.size(); i++) {
            for (int j = 0; j < 3; j++)
                KongList[i][j] = chowTiles.get(i).get(j).getId();
        }
        msg.put("ChowList", KongList);

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }

    /**
     * Handles the Chow operation for a player
     */
    public void Chow(String playerName, String roomID, int[] tileIDList) {
        GameStarter gs = games.get(roomID);

        ArrayList<Tile> chowTiles = new ArrayList<>();
        for (int tileID : tileIDList) {
            chowTiles.add(gs.findTile(tileID));
        }

        gs.Chow(chowTiles, playerName, gs.getDiscardTileID());
    }

    /**
     * Handles the Pang operation for a player
     */
    public void Pang(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);
        gs.Pang(playerName, gs.getDiscardTileID());
    }

    /**
     * Checks if a room can draw more tiles
     */
    public Boolean canDraw(String roomID) {
        return games.get(roomID).canDraw();
    }

    /**
     * Sends a draw game message
     * @return JSON string containing the draw message
     */
    public String Draw() {
        Map<String, Object> result = new HashMap<>();
        result.put("operation", "Draw");
        result.put("msg", "Game Over");
        return JSONObject.toJSONString(result);
    }

    /**
     * Adds a table tile to the game
     */
    public void addTableTile(String roomID) {
        GameStarter gs = games.get(roomID);
        gs.addTableTile();
    }
}
