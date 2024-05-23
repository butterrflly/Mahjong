package com.smoker.mahjong.service;

import com.alibaba.fastjson2.JSONObject;
import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.impl.GameStarter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service // 注解：标记当前是个service类，并注入到spring容器中
public class GameService {
    private final Map<String, GameStarter> games = new HashMap<>();

    public void newRoom(String owner){
        games.put(owner, new GameStarter(owner));
    }

    public String startGame(String owner, String banker){
        return handleResult(games.get(owner).startGame(banker));
    }

    public String addPlayer(String owner, String name) {
        return handleResult(games.get(owner).addPlayer(name));

    }

    public String removePlayer(String owner, String name) {
        return handleResult(games.get(owner).removePlayer(name));
    }

    public String deal(String owner, String name) {
        return handleResult(games.get(owner).deal(name));
    }

    public String discard(String owner, String name, int tileID) {
        return handleResult(games.get(owner).discard(name, tileID));
    }


    public String canPang(String owner, String name, int tileID) {
        return handleResult(games.get(owner).canPang(name, tileID));
    }


    public String canKong(String owner, String name, int tileID) {
        return handleResult(games.get(owner).canKong(name, tileID));
    }


    public String canKong(String owner, String name) {
        GameStarter game = games.get(owner);
        ArrayList<Tile> tiles = game.canKong(name);
        if (tiles.size() == 0) {
            return handleResult(false);
        }

        int[] intArray = tiles.stream().mapToInt(Tile :: getId).toArray();
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "ok");
        map.put("data", true);
        map.put("tiles", intArray);
        return JSONObject.toJSONString(map);
    }


    public String canChow(String owner, String name, int tileID) {
        GameStarter game = games.get(owner);
        ArrayList<ArrayList<Tile>> tiless = game.canChow(name, tileID);
        if (tiless.size() == 0) {
            return handleResult(false);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "ok");
        map.put("data", true);
        map.put("num", tiless.size());
        for (ArrayList<Tile> tiles : tiless){
            int[] intArray = tiles.stream().mapToInt(Tile :: getId).toArray();
            map.put("tiles" + (tiless.indexOf(tiles) + 1), intArray);
        }
        return JSONObject.toJSONString(map);
    }


    public String canHu(String owner, String name, int tileID) {
        return handleResult(games.get(owner).canHu(name, tileID));
    }


    public String Pang(String owner, String name, int tileID) {
        games.get(owner).Pang(name, tileID);
        return handleResult();
    }

    public String Kong(String owner, String name, int tileID) {
        games.get(owner).Kong(name, tileID);
        return handleResult();
    }


    public String Chow(String owner, String name, int tileID, int tile1, int tile2, int tile3) {
        GameStarter game = games.get(owner);
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.add(game.findTile(tile1));
        tiles.add(game.findTile(tile2));
        tiles.add(game.findTile(tile3));
        game.Chow(tiles, name, tileID);
        return handleResult();
    }


    public String Hu(String owner, String winnerName, int tileID, String loserName) {
        games.get(owner).Hu(winnerName, tileID, loserName);
        return handleResult();
    }


    public String canDraw(String owner) {
        return handleResult(games.get(owner).canDraw());
    }


    public String getHandTile(String owner, String name) {
        HandTile handTile = games.get(owner).getHandTile(name);
        ArrayList<Tile> tiles = handTile.getHandTile();
        ArrayList<Meld> melds = handTile.getMelds();

        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "ok");
        map.put("手牌数量", tiles.size());

        int[] intArray = tiles.stream().mapToInt(Tile :: getId).toArray();
        map.put("手牌", intArray);

        map.put("组数量", handTile.getMelds().size());

        for (Meld meld : melds){
            int[] meldArray = meld.getMeld().stream().mapToInt(Tile :: getId).toArray();
            map.put("meld" + (melds.indexOf(meld) + 1), intArray);
        }

        map.put("组是明还是暗", true);

        return JSONObject.toJSONString(map);
    }

    public String handleResult() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "ok");
        return JSONObject.toJSONString(map);
    }

    public String handleResult(Object result) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "ok");
        map.put("data", result);
        return JSONObject.toJSONString(map);
    }
}
