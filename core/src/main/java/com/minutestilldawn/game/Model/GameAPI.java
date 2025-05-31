package com.minutestilldawn.game.Model;

import java.util.List;

public class GameAPI {
    private static GameAPI instance;
    private static GameState gameState; 
    private static User currentUser; 
    private static List<User> users; 

    private GameAPI() {
    }

    public static GameAPI getInstance() {
        if (instance == null) {
            instance = new GameAPI();
        }
        return instance;
    }
    public void startNewGame(GameState gameState) {

    }
    
}
