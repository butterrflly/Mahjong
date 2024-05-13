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

    public Player(String name) {
        this.id = id;
        this.name = name;
        this.handTile = new HandTile();
        this.melds = new ArrayList<Meld>();
        this.score = 0;
    }


    public void addInitialHand(Tile tile) {
        //Distribute the initial hand
        handTile.addTile(tile);
    }

}
