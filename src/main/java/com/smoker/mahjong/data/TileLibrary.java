package com.smoker.mahjong.data;

import com.smoker.mahjong.doma.Game.Tile;
import com.smoker.mahjong.doma.User.Player;

import java.util.ArrayList;
import java.util.Collections;

public class TileLibrary {
    // 储存牌库信息
    private ArrayList<Player> playerList;
    private ArrayList<Tile> tiles;

    private ArrayList<Tile> totalTiles;

    private ArrayList<Tile> tableTiles;




    public TileLibrary(ArrayList<Player> playerList){
        this.playerList = playerList;
        tiles = new ArrayList<>();
        createTiles();
        shuffle(tiles);
        dealInitialHand();
    }

    /**
     * 创建初始牌库
     *
     * 牌的 id 可以精准找到牌的 类型 大小 第几张牌
     * id 一定是一个三位数
     *
     * 百位是牌的类型  1 对应 筒， 2 对应 条， 3 对应 万， 4 对应 风牌（东南西北）， 5 对应 字牌 （中发白）
     * 十位是牌的大小  其中 筒、条、万 数字就是其对应的大小   风牌中 1，2，3，4 对应东南西北， 字牌中 1，2，3 对应中发白
     * 个位代表这是第几张牌  因为每种类型的牌有 4个 ，所以个位就用来记录 这是4张一样的牌中的第几个 大小范围是 1 ~ 4
     */
    public void createTiles(){
        for(int suit = 1; suit <= 5; suit++){
            int figure = 9;
            if (suit == 4) figure = 4;
            if (suit == 5) figure = 3;
            for(int num = 1; num <= figure; num++){
                for(int index = 1; index <= 4; index++){
                    tiles.add(new Tile(suit * 100 + num * 10 + index, true));
                }
            }
        }
        totalTiles = new ArrayList<>(tiles);
    }

    public void shuffle(ArrayList<Tile> tiles){
        Collections.shuffle(tiles);
    }


    public void dealInitialHand(){
        // 发牌算法
        for(Player player : playerList){
            for(int i = 0; i < 13; i++){
                player.addInitialHand(tiles.remove(0));
            }
        }
    }

    public Tile deal(String name){
        for(Player player : playerList){
            if(player.getName().equals(name)){
                return player.getHandTile().addTile(tiles.remove(0));
            }
        }
        return null;
    }

    public Tile findTile(int id){
        for(Tile tile : totalTiles){
            if(tile.getId() == id){
                return tile;
            }
        }
        return null;
    }

    public void addTableTile(Tile tile){
        tableTiles.add(tile);
    }

    public ArrayList<Tile> getTableTile(){
        return tableTiles;
    }

    public ArrayList<Tile> getTiles(){
        return tiles;
    }
}
