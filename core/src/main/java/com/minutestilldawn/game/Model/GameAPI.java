package com.minutestilldawn.game.Model;

public class GameAPI {
    private static GameAPI instance;

    private GameAPI() {
    }

    public static GameAPI getInstance() {
        if (instance == null) {
            instance = new GameAPI();
        }
        return instance;
    }
    public void startNewGame(User user) {
        GameState.getInstance().setCurrentUser(user);
        GameState.getInstance().setCurrentLevel(1);
        GameState.getInstance().setScore(0);
        // additional initialization here
    }
    
}
