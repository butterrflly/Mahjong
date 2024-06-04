package com.smoker.mahjong.service;

import com.alibaba.fastjson2.JSONObject;
import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.Player;
import com.smoker.mahjong.impl.GameStarter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service // 注解：标记当前是个service类，并注入到spring容器中
public class GameService {
    private final Map<String, GameStarter> games = new HashMap<>();


    /**
     * @return {"operation" : "Duplicate room number"}
     */
    public String newRoom(String roomID){
        if (games.containsKey(roomID)) {
            Map<String, Object> result = new HashMap<>();
            result.put("operation", "Duplicate room number");
            return JSONObject.toJSONString(result);
        }
        games.put(roomID, new GameStarter(roomID));
        return "success";
    }

    public void removeRoom(String roomID){
        games.remove(roomID);
    }

    public void addPlayer(String playName, String roomID) {
        games.get(roomID).addPlayer(playName);
    }

    public void removePlayer(String playName, String roomID) {
        games.get(roomID).removePlayer(playName);
    }

    public ArrayList<Player> getPlayers(String roomID) {
        return games.get(roomID).getPlayerList();
    }

    public void prepare(String playName, String roomID) {
        games.get(roomID).findPlayer(playName).setPrepare();
    }

    public boolean startGame(String roomID){
        return games.get(roomID).startGame();
    }

    public String getDealPlayer(String roomID){
        return games.get(roomID).getDealPlayer().getName();
    }


    /**
     * @return {"operation" : "getGameRooms", "msg" : {"name" : String, "room number" : int, "room message" : {"room id_1" : player number, "room id_2" : player number, ...}}}
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
                roomMessage.put(key, games.get(key).getPlayerNum());
            }
        }

        message.put("room message", roomMessage);

        result.put("msg", message);
        return JSONObject.toJSONString(result);
    }

    /**
     * @return {"operation" : "getRoomPlayerMessage", "msg" : {"self" : {"name" : String, "prepare" : boolean, "score" : int},
     *                                                         "nextPlayer" : {"name" : String, "prepare" : boolean, "score" : int},
     *                                                         "oppositePlayer" : {"name" : String, "prepare" : boolean, "score" : int},
     *                                                         "prevPlayer" : {"name" : String, "prepare" : boolean, "score" : int}}}
     */
    public String getRoomPlayerMessage(String playerName, String roomID){
        GameStarter gs = games.get(roomID);
        Player[] players = gs.getSequence(playerName);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getRoomPlayerMessage");

        Map<String, Object> message = new HashMap<>();
        String[] positions = {"self", "nextPlayer", "oppositePlayer", "prevPlayer"};

        for (int i = 0; i < 4; i++){
            Map<String, Object> playerMessage = new HashMap<>();
            if (players[i] == null){
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
     * @return {"operation" : "getHandTile", "msg" : {"self" : {"name" : String, "handTile number" : int, "handTile" : [int, int, ...]},
     *                                                "nextPlayer" : {"name" : String, "handTile number" : int, "handTile" : [int, int, ...]},
     *                                                "oppositePlayer" : {"name" : String, "handTile number" : int, "handTile" : [int, int, ...]},
     *                                                "prevPlayer" : {"name" : String, "handTile number" : int, "handTile" : [int, int, ...]}}}
     */
    public String getHandTile(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);
        Player[] players = gs.getSequence(playerName);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getHandTile");

        Map<String, Object> message = new HashMap<>();
        String[] positions = {"self", "nextPlayer", "oppositePlayer", "prevPlayer"};

        for (int i = 0; i < 4; i++){
            Map<String, Object> playerMessage = new HashMap<>();
            playerMessage.put("name", players[i].getName());
            playerMessage.put("handTile number", players[i].getHandTile().getHandTile().size());
            playerMessage.put("handTile", players[i].getHandTile().getHandTile().stream().mapToInt(Tile :: getId).toArray());
            message.put(positions[i], playerMessage);
        }

        result.put("msg", message);
        return JSONObject.toJSONString(result);
    }


    /**
     * @return {"operation" : "getMeld", "msg" : {"self" :           {"name" : String, "meld number" : int, "meld number list" : [int, int, ...], "isHide list" : [boolean, boolean, ...], "melds" : [[int, int, ...], [int, int, ...], ...]},
     *                                            "nextPlayer" :     {"name" : String, "meld number" : int, "meld number list" : [int, int, ...], "isHide list" : [boolean, boolean, ...], "melds" : [[int, int, ...], [int, int, ...], ...]},
     *                                            "oppositePlayer" : {"name" : String, "meld number" : int, "meld number list" : [int, int, ...], "isHide list" : [boolean, boolean, ...], "melds" : [[int, int, ...], [int, int, ...], ...]},
     *                                            "prevPlayer" :     {"name" : String, "meld number" : int, "meld number list" : [int, int, ...], "isHide list" : [boolean, boolean, ...], "melds" : [[int, int, ...], [int, int, ...], ...]}}}
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

            for (int j = 0; j < meldNumber; j++){
                meldNumberList[j] = melds.get(j).getMeld().size();
                isHideList[j] = melds.get(j).getHide();
                meldArray.add(melds.get(j).getMeld().stream().mapToInt(Tile :: getId).toArray());
            }

            playerMessage.put("meld number list", meldNumberList);
            playerMessage.put("isHide list", isHideList);
            playerMessage.put("meld", meldArray.toArray());

            message.put(positions[i], playerMessage);
        }

        result.put("msg", message);
        return JSONObject.toJSONString(result);
    }


    /**
     * @return {"operation" : "getTableTile", "msg" : {"tableTile" : [tileID_1, tileID_2, ...]}}
     */
    public String getTableTile(String roomID) {
        GameStarter gs = games.get(roomID);
        ArrayList<Tile> tableTile = gs.getTableTile();

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getTableTile");


        Map<String, Object> mag = new HashMap<>();
        int[] tableTileArray = tableTile.stream().mapToInt(Tile :: getId).toArray();
        mag.put("tableTile", tableTileArray);

        result.put("msg", mag);

        return JSONObject.toJSONString(result);
    }


    public void deal(String roomID) {
        GameStarter gs = games.get(roomID);
        gs.deal();
    }


    /**
     * @return {"operation" : "deal", "msg" : {"position" : String, "playerName" : String, "tileID" : int}}
     * positions = {"self", "nextPlayer", "oppositePlayer", "prevPlayer"}
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
     * @return {"operation" : "getSelfAffair", "msg" : {"playerName" : String, "canKong" : boolean, "canHu" : boolean}}
     */
    public String getSelfAffair(String playerName, String roomID){
        GameStarter gs = games.get(roomID);

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getSelfAffair");

        Map<String, Object> msg = new HashMap<>();
        msg.put("playerName", playerName);

        msg.put("canKong", gs.canKong(playerName).size() == 0);
        msg.put("canHu", gs.canHu(playerName, gs.getDealTileID()));

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }



    /**
     * @return {"operation" : "discardRequest", "msg" : "Until you play"}
     */
    public String discardRequest(){
        Map<String, Object> result = new HashMap<>();
        result.put("operation", "discard");
        result.put("msg", "你怎么还不出牌啊，我等的花都谢了");

        return JSONObject.toJSONString(result);
    }


    public void discard(String roomID, String discardPlayerName, int TileID) {
        GameStarter gs = games.get(roomID);
        gs.discard(discardPlayerName, TileID);
    }



    /**
     * @return {"operation" : "discard", "msg" : {"position" : String, "playerName" : String, "tileID" : int}}
     * positions = {"self", "nextPlayer", "oppositePlayer", "prevPlayer"}
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
     * @return {"operation" : "canHu", "msg" : {"playerName" : String, "canHu" : boolean}}
     */
    public String canHu(String playerName, String roomID){
        GameStarter gs = games.get(roomID);
        int discardTileID = gs.getDiscardTileID();

        Player[] players = gs.getSequence(playerName);
        Player nextPlayer = players[1];

        if (nextPlayer.getName().equals(gs.getDiscardPlayerName())){
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
        msg.put("canPang", games.get(roomID).canHu(nextPlayer.getName(), discardTileID));

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }




    /**
     * @return {"operation" : "getAffair", "msg" : {"playerName" : String, "canPang" : boolean, "canKong" : boolean, "canChow" : boolean}}
     */
    public String getAffair(String playerName, String roomID){
        GameStarter gs = games.get(roomID);
        Player[] players = gs.getSequence(playerName);
        int discardTileID = gs.getDiscardTileID();

        Map<String, Object> result = new HashMap<>();
        result.put("operation", "getAffair");

        Map<String, Object> msg = new HashMap<>();
        msg.put("playerName", playerName);
        msg.put("canPang", gs.canPang(playerName, discardTileID));
        msg.put("canKong", gs.canKong(playerName, discardTileID));

        if (players[3].getName().equals(gs.getDiscardPlayerName()))
            msg.put("canChow", gs.canChow(playerName, discardTileID).size() == 0);
        else
            msg.put("canChow", false);

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }



    public void Hu(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);

        if (gs.getDiscardTileID() == 0)
            gs.Hu(playerName, gs.getDealTileID(), "allPlayers");
        else
            gs.Hu(playerName, gs.getDiscardTileID(), gs.getDiscardPlayerName());
    }


    /**
     * @return {"operation" : "Hu", "msg" : {"position" : String, "playerName" : String}}
     * positions = {"self", "nextPlayer", "oppositePlayer", "prevPlayer"}
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
     * @return {"operation" : "Kong", "msg" : {"playerName" : String, "KongNum" : int, "KongList" : [TileID_1, TileID_2, ...]}}
     */
    public String Kong(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);

        if (gs.getDiscardTileID() == 0) {
            // 自己 kong 自己
            ArrayList<Tile> kongTiles = gs.canKong(playerName);
            Map<String, Object> result = new HashMap<>();
            result.put("operation", "Kong");

            Map<String, Object> msg = new HashMap<>();
            msg.put("playerName", playerName);
            msg.put("KongNum", kongTiles.size());

            int[] KongList = kongTiles.stream().mapToInt(Tile :: getId).toArray();
            msg.put("KongList", KongList);

            result.put("msg", msg);

            return JSONObject.toJSONString(result);
        }
        else {
            gs.Kong(playerName, gs.getDiscardTileID());
            return null;
        }
    }


    public void selfKong(String playerName, String roomID, int tileID){
        GameStarter gs = games.get(roomID);
        Map<String, Object> result = new HashMap<>();

        gs.Kong(playerName, tileID);
    }


    /**
     * @return {"operation" : "Chow", "msg" : {"playerName" : String, "ChowNum" : int, "ChowList" : [[TileID_1, TileID_2, TileID_3], ...]}}
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
        for (int i = 0; i < chowTiles.size(); i++){
            for (int j = 0; j < 3; j++)
                KongList[i][j] = chowTiles.get(i).get(j).getId();
        }
        msg.put("ChowList", KongList);

        result.put("msg", msg);

        return JSONObject.toJSONString(result);
    }


    public void Chow(String playerName, String roomID, int[] tileIDList){
        GameStarter gs = games.get(roomID);

        ArrayList<Tile> chowTiles = new ArrayList<>();
        for (int tileID : tileIDList) {
            chowTiles.add(gs.findTile(tileID));
        }

        gs.Chow(chowTiles, playerName, gs.getDiscardTileID());
    }

    public void Pang(String playerName, String roomID) {
        GameStarter gs = games.get(roomID);
        gs.Pang(playerName, gs.getDiscardTileID());
    }

    public Boolean canDraw(String roomID) {
        return games.get(roomID).canDraw();
    }



    /**
     * @return {"operation" : "Draw", "msg" : "Game Over"}
     */
    public String Draw() {
        Map<String, Object> result = new HashMap<>();
        result.put("operation", "Draw");
        result.put("msg", "Game Over");
        return JSONObject.toJSONString(result);
    }


    public void addTableTile(String roomID) {
        GameStarter gs = games.get(roomID);
        gs.addTableTile();
    }
}
