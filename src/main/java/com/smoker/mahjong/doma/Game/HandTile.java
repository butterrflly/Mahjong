package com.smoker.mahjong.doma.Game;


import java.util.*;
import java.util.stream.Collectors;

public class HandTile {

    private ArrayList<Tile> handTile;

    private ArrayList<Meld> melds;



    public HandTile() {
        handTile = new ArrayList<Tile>();
        melds = new ArrayList<Meld>();
    }

    public Tile addTile(Tile tile){
        handTile.add(tile);
        sort();
        return tile;
    }

    public int removeTile(Tile tile){
        handTile.remove(tile);
        sort();
        return tile.getId();
    }

    public void cleanTile(){
        handTile.clear();
    }

    public void removeTile(ArrayList<Tile> tiles){
        for (Tile tile : tiles)
            handTile.remove(tile);
    }

    public boolean canPang(int tileID) {
        int num = 0;
        for (Tile tile : handTile)
            if (tile.getId() / 10 == tileID / 10)
                num ++;

        return num >= 2;
    }

    public boolean canKong(int tileID) {
        int numHand = 0;
        int numMelds = 0;
        boolean contain = false;


        for (Tile tile : handTile) {
            if (tile.getId() == tileID) {
                contain = true;
                continue;
            }
            if (tile.getId() / 10 == tileID / 10) {
                numHand++;
            }
        }

        if (contain){
            return numHand >= 3;
        }

        for (Meld meld : melds) {
            int count = 0;
            for (Tile tile : meld.getMeld()) {
                if (tile.getId() / 10 == tileID / 10) {
                    count++;
                }
            }
            if (count == 3) {
                numMelds++;
            }
        }

        return numHand >= 3 || numMelds > 0;
    }


    /**
     * 每回合检查自己的手牌有没有 4 个一样的牌
     * 检查是否可以在自己回合内杠牌
     * @return 可以杠牌 的 列表 ，可能有多个可以开杠的牌
     */
    public ArrayList<Tile> canKong(){
        ArrayList<Tile> kongTiles = new ArrayList<>();
        int id = 0;
        for (Tile tile : handTile){
            if (id == tile.getId() / 10)
                continue;
            if (canKong(tile.getId())){
                kongTiles.add(tile);
                id = tile.getId() / 10;
            }
        }
        return kongTiles;
    }


    /**
     * 因为可能会有多种吃法，所以返回一个二维数组
     * 二位数组 肯能为空， 也就是没有可以吃的 组合
     */
    public ArrayList<ArrayList<Tile>> canChow(Tile tile) {
        ArrayList<ArrayList<Tile>> chowTiles = new ArrayList<>();
        int num = tile.getId() / 10;

        if (num / 10 > 3) return chowTiles;

        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile value : handTile) {
            if (value.getId() / 100 == num / 10){
                tiles.add(value);
            }
        }

        if (tiles.size() < 2) return chowTiles;

        Tile tile1 = inArrayList(tiles, num - 2);
        Tile tile2 = inArrayList(tiles, num - 1);
        Tile tile3 = inArrayList(tiles, num + 1);
        Tile tile4 = inArrayList(tiles, num + 2);


        if (tile1 != null && tile2 != null){
            ArrayList<Tile> c = new ArrayList<>();
            c.add(tile1);
            c.add(tile2);
            c.add(tile);
            chowTiles.add(c);
        }
        if (tile2 != null && tile3 != null){
            ArrayList<Tile> c = new ArrayList<>();
            c.add(tile2);
            c.add(tile);
            c.add(tile3);
            chowTiles.add(c);
        }
        if (tile3 != null && tile4 != null){
            ArrayList<Tile> c = new ArrayList<>();
            c.add(tile);
            c.add(tile3);
            c.add(tile4);
            chowTiles.add(c);
        }
        return chowTiles;
    }


    public int canHu(Tile tile) {
        ArrayList<Tile> tempHandTile = new ArrayList<>(handTile);

        if (!tempHandTile.contains(tile)) {
            tempHandTile.add(tile);
        }

        tempHandTile.sort(Comparator.comparingInt(Tile::getId));

        ArrayList<Tile> pair = new ArrayList<>();

        for (int i = 0; i < tempHandTile.size(); i += 2) {
            if (tempHandTile.get(i).getId() / 10 == tempHandTile.get(i + 1).getId() / 10){
                pair.add(tempHandTile.get(i));
                pair.add(tempHandTile.get(i + 1));
            }
        }


        if (pair.size() == 0) return 0;


        // 7 small pair
        if (pair.size() == 14) {
            Map<Integer, Integer> countMap = new HashMap<>();
            for (Tile tempTile : pair) {
                int currentID = tempTile.getId() / 10;
                countMap.put(currentID, countMap.getOrDefault(currentID, 0) + 1);
            }

            // big 7 pair
            for (int count : countMap.values()) {
                if (count >= 4) {
                    System.out.println("big 7 pair");
                    return 8;
                }
            }
            System.out.println("7 small pair");
            return 4;
        }


        int meldTripletNum = 0;
        for (Meld meld : melds){
            if (meld.getType().equals("Pang") || meld.getType().equals("Kong")){
                meldTripletNum++;
            }
        }

        while (pair.size() != 0) {
            ArrayList <Tile> temp = new ArrayList<>(tempHandTile);
            temp.remove(pair.remove(0));
            temp.remove(pair.remove(0));

            ArrayList<Integer> tileIDs = new ArrayList<>(temp.stream().map(Tile :: getId).map(id -> id / 10).toList());
            int tripletNum = 0;
            boolean canHu = false;

            while (tileIDs.size() >= 3) {
                if (tileIDs.get(0).equals(tileIDs.get(1)) && tileIDs.get(0).equals(tileIDs.get(2))) {
                    tileIDs.subList(0, 3).clear();  // Remove first three elements
                    tripletNum++;
                } else if (tileIDs.contains(tileIDs.get(0) + 1) && tileIDs.contains(tileIDs.get(0) + 2)) {
                    if (tileIDs.get(0) / 10 > 3){
                        continue;
                    }
                    tileIDs.remove(Integer.valueOf(tileIDs.get(0) + 2));
                    tileIDs.remove(Integer.valueOf(tileIDs.get(0) + 1));
                    tileIDs.remove(0);
                } else {
                    canHu = false;
                    break;
                }
                canHu = true;
            }
            if (canHu && tripletNum + meldTripletNum >= 1){
                System.out.println("normal");
                return 2;
            }
        }

        return 0;
    }

    public void Pang(Tile tile) {
        // 碰牌
        ArrayList<Tile> meld = new ArrayList<>();

        for (Tile value : handTile) {
            if (tile.getId() / 10 == value.getId() / 10 && meld.size() < 2) {
                meld.add(value);
            }
        }

        removeTile(meld);
        meld.add(tile);
        melds.add(new Meld(meld, "Pang", false));
    }

    public void Kong(Tile tile) {
        // 杠牌
        ArrayList<Tile> meld = new ArrayList<>();

        for (Tile value : handTile) {
            if (tile.getId() / 10 == value.getId() / 10) {
                meld.add(value);
            }
        }

        if (meld.size() >= 3) {
            removeTile(meld);
            if (meld.size() == 3) meld.add(tile);
            melds.add(new Meld(meld, "Kong", handTile.contains(tile)));
        } else {
            for (int i = 0; i < melds.size(); i++) {
                Meld m = melds.get(i);
//                int num = 0;
//                for (Tile value : m.getMeld()){
//                    if  (tile.getId() / 10 == value.getId() / 10){
//                        num++;
//                    }
//                }
//                if (num == 3){
//                    melds.remove(m);
//                    meld.addAll(m.getMeld());
//                    meld.add(tile);
//                    melds.add(new Meld(meld, "Kong", false));
//                    break;
//                }

                if (m.getType().equals("Pang")){
                    if (tile.getId() / 10 == m.getMeld().get(0).getId() / 10){
                        melds.remove(m);
                        meld.addAll(m.getMeld());
                        meld.add(tile);
                        melds.add(new Meld(meld, "Kong", false));
                        break;
                    }
                }
            }
        }
    }


    /**
     * 因为可能会有多种吃法，所以需要一种吃的组合
     * 所以额外的一个数组参数就是吃的组合
     */
    public void Chow(ArrayList<Tile> chowTiles, Tile tile) {
        // 吃牌
        chowTiles.remove(tile);
        Tile tile1 = chowTiles.remove(0);
        Tile tile2 = chowTiles.remove(0);
        handTile.remove(tile1);
        handTile.remove(tile2);
        chowTiles.add(tile1);
        chowTiles.add(tile);
        chowTiles.add(tile2);
        melds.add(new Meld(chowTiles, "Chow", false));
    }


    public int Hu(Tile tile) {
        ArrayList<Tile> tempHandTile = new ArrayList<>(handTile);

        // 胡牌
        int HuType;

        HuType = canHu(tile);

        if (!tempHandTile.contains(tile)){
            tempHandTile.add(tile);
        }

        // 大 单吊
        if (tempHandTile.size() == 2){
            System.out.println("大 单吊");
            HuType *= 2;
        }



        // 一条龙
        ArrayList<Integer> tileIDs = new ArrayList<>(tempHandTile.stream().map(Tile :: getId).map(id -> id / 10).distinct().toList());

        System.out.println(tileIDs.toString());

        boolean hasAllSuites = true;
        for (int i = 1; i <= 3; i++) {
            hasAllSuites = true;
            for (int j = 1; j <= 9; j++) {
                int tileID = i * 10 + j;
                if (!tileIDs.contains(tileID)) {
                    hasAllSuites = false;
                    break;
                }
            }
            if (hasAllSuites) {
                break;
            }
        }

        if (hasAllSuites) {
            System.out.println("一条龙");
            HuType *= 2;
        }


        // 清一色
        for (Meld meld : melds){
            tempHandTile.addAll(meld.getMeld());
        }

        if (tempHandTile.stream().map(Tile :: getId).map(id -> id / 100).distinct().count() == 1){
            System.out.println("清一色");
            HuType *= 2;
        }

        return HuType;
    }


    private Tile inArrayList(ArrayList<Tile> tiles, int id) {
        for (Tile value : tiles) {
            if (value.getId() / 10 == id) {
                return value;
            }
        }
        return null;
    }

    public void sort(){
        handTile.sort(Comparator.comparing(Tile :: getId));
    }

    public ArrayList<Tile> getHandTile(){
        sort();
        return handTile;
    }

    public ArrayList<Meld> getMelds(){
        return melds;
    }
}

