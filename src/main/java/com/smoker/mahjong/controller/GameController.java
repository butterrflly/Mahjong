package com.smoker.mahjong.controller;

import com.smoker.mahjong.service.GameService;
import com.smoker.mahjong.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
@CrossOrigin(origins = "http://172.20.10.2:5173")
public class GameController {

    @Autowired // 引用容器中的service
    private GameService gameService;

    @RequestMapping(value = "/newRoom", method = RequestMethod.POST)
    public String newRoom(@RequestParam("owner") String owner)
    {
        gameService.newRoom(owner);
        return Result.okGetString();
    }

    @RequestMapping(value = "/findRoom", method = RequestMethod.POST)
    public String findRoom(@RequestParam("owner") String owner)
    {
        return gameService.findRoom(owner);
    }

    @RequestMapping(value = "/startGame", method = RequestMethod.POST)
    public String startGame(@RequestParam("owner") String owner)
    {
        return gameService.startGame(owner);
    }


    @RequestMapping(value = "/addPlayer", method = RequestMethod.POST)
    public String addPlayer(@RequestParam("owner") String owner,
                            @RequestParam("name") String name)
    {
        return gameService.addPlayer(owner, name);
    }


    @RequestMapping(value = "/removePlayer", method = RequestMethod.POST)
    public String removePlayer(@RequestParam("owner") String owner,
                               @RequestParam("name") String name)
    {
        return gameService.removePlayer(owner, name);
    }

    @RequestMapping(value = "/getBanker", method = RequestMethod.POST)
    public String getBanker(@RequestParam("owner") String owner)
    {
        return gameService.getBanker(owner);
    }

    @RequestMapping(value = "/getSequence", method = RequestMethod.POST)
    public String getSequence(@RequestParam("owner") String owner,
                               @RequestParam("name") String name)
    {
        return gameService.getSequence(owner, name);
    }
    
    @RequestMapping(value = "/deal", method = RequestMethod.POST)
    public String deal(@RequestParam("owner") String owner,
                       @RequestParam("name") String name)
    {
        return gameService.deal(owner, name);
    }


    @RequestMapping(value = "/discard", method = RequestMethod.POST)
    public String discard(@RequestParam("owner") String owner,
                          @RequestParam("name") String name,
                          @RequestParam("tileID") int tileID)
    {
        return gameService.discard(owner, name, tileID);
    }


    @RequestMapping(value = "/canPang", method = RequestMethod.POST)
    public String canPang(@RequestParam("owner") String owner,
                          @RequestParam("name") String name,
                          @RequestParam("tileID") int tileID)
    {
        return gameService.canPang(owner, name, tileID);
    }


    @RequestMapping(value = "/canKong", method = RequestMethod.POST)
    public String canKong(@RequestParam("owner") String owner,
                          @RequestParam("name") String name,
                          @RequestParam("tileID") int tileID)
    {
        return gameService.canKong(owner, name, tileID);
    }


    @RequestMapping(value = "/canKongSelf", method = RequestMethod.POST)
    public String canKongSelf(@RequestParam("owner") String owner,
                          @RequestParam("name") String name)
    {
        return gameService.canKong(owner, name);
    }


    @RequestMapping(value = "/canChow", method = RequestMethod.POST)
    public String canChow(@RequestParam("owner") String owner,
                          @RequestParam("name") String name,
                          @RequestParam("tileID") int tileID)
    {
        return gameService.canChow(owner, name, tileID);
    }


    @RequestMapping(value = "/canHu", method = RequestMethod.POST)
    public String canHu(@RequestParam("owner") String owner,
                        @RequestParam("name") String name,
                        @RequestParam("tileID") int tileID)
    {
        return gameService.canHu(owner, name, tileID);
    }


    @RequestMapping(value = "/Pang", method = RequestMethod.POST)
    public String Pang(@RequestParam("owner") String owner,
                       @RequestParam("name") String name,
                       @RequestParam("tileID") int tileID)
    {
        return gameService.Pang(owner, name, tileID);
    }


    @RequestMapping(value = "/Kong", method = RequestMethod.POST)
    public String Kong(@RequestParam("owner") String owner,
                       @RequestParam("name") String name,
                       @RequestParam("tileID") int tileID)
    {
        return gameService.Kong(owner, name, tileID);
    }


    @RequestMapping(value = "/Chow", method = RequestMethod.POST)
    public String Chow(@RequestParam("owner") String owner,
                       @RequestParam("name") String name,
                       @RequestParam("tileID") int tileID,
                       @RequestParam("tile1") int tile1,
                       @RequestParam("tile2") int tile2,
                       @RequestParam("tile3") int tile3)
    {
        return gameService.Chow(owner, name, tileID, tile1, tile2, tile3);
    }


    @RequestMapping(value = "/Hu", method = RequestMethod.POST)
    public String Hu(@RequestParam("owner") String owner,
                     @RequestParam("winnerName") String winnerName,
                     @RequestParam("tileID") int tileID,
                     @RequestParam("loserName") String loserName)
    {
        return gameService.Hu(owner, winnerName, tileID, loserName);
    }


    @RequestMapping(value = "/canDraw", method = RequestMethod.POST)
    public String canDraw(@RequestParam("owner") String owner)
    {
        return gameService.canDraw(owner);
    }


    @RequestMapping(value = "/getHandTile", method = RequestMethod.POST)
    public String t(@RequestParam("owner") String owner,
                              @RequestParam("name") String name)
    {
        return gameService.getHandTile(owner, name);
    }

}
