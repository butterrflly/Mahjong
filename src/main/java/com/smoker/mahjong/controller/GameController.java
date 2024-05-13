package com.smoker.mahjong.controller;

import com.smoker.mahjong.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @RequestMapping(value = "/startGame", method = RequestMethod.POST)
    public String startGame(@RequestParam("play_A") String name_A,
                            @RequestParam("play_B") String name_B,
                            @RequestParam("play_C") String name_C,
                            @RequestParam("play_D") String name_D)
    {
        return "Result";
    }


    @RequestMapping(value = "/draw", method = RequestMethod.POST)
    public String draw(@RequestParam("player") String name)
    {
        return "Tile";
    }

    @RequestMapping(value = "/discard", method = RequestMethod.POST)
    public String discard(@RequestParam("player") String name,
                          @RequestParam("tileID") int tileID)
    {
        return "HandTile";
    }

    @RequestMapping(value = "/isPang", method = RequestMethod.POST)
    public String isPang(@RequestParam("player") String name,
                         @RequestParam("tileID") int tileID)
    {
        return "Result";
    }

    @RequestMapping(value = "/Pang", method = RequestMethod.POST)
    public String Pang(@RequestParam("player") String name,
                         @RequestParam("tileID") int tileID)
    {
        return "HandTile";
    }

    @RequestMapping(value = "/")
    public String draw()
    {
        return Result.okGetString();
    }

    //  ......

}
