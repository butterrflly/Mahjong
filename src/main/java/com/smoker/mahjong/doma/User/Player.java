package com.smoker.mahjong.doma.User;


import com.smoker.mahjong.doma.Game.HandTile;
import com.smoker.mahjong.doma.Game.Meld;
import com.smoker.mahjong.doma.Game.Tile;
import java.util.ArrayList;

public class Player {
    private int id;
    private String name;
    private HandTile handTile;
    private ArrayList<Meld> melds;
    private int score;
    private boolean prepare;

    public Player(String name) {
        this.name = name;
        this.handTile = new HandTile();
        this.melds = new ArrayList<Meld>();
        this.score = 0; // 分数初始化
        this.prepare = false;
    }


    public void addInitialHand(Tile tile) {
        //Distribute the initial hand
        handTile.addTile(tile);
        handTile.sort();
    }


    /**
     * 用于游戏结束后的算分
     * @param banker 庄家
     * @param winner 赢家，可能是null
     * @param loser 输家，可能是null
     */
    public void scoring(Player banker, Player winner, ArrayList<Player> loser){
        // 计分算法
    }

    public String getName() {
        return name;
    }

    public HandTile getHandTile() {
        return handTile;
    }

    public int getScore() {
        return score;
    }



    public boolean isPrepare() {
        return prepare;
    }

    public void setPrepare() {
        prepare = !prepare;
    }

}
