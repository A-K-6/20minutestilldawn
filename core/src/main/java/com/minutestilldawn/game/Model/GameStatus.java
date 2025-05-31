package com.minutestilldawn.game.Model;

/**
 * Represents the various states the game can be in.
 */
public enum GameStatus {
    /**
     * The game is in the pre-game menu, settings are being chosen.
     */
    PRE_GAME,

    /**
     * The game is actively being played.
     */
    PLAYING,

    /**
     * The game is paused.
     */
    PAUSED,

    /**
     * The game is paused, and the player is presented with ability choices upon leveling up.
     */
    LEVEL_UP_CHOICE,

    /**
     * The game has ended, and the player has won.
     */
    GAME_OVER_WIN,

    /**
     * The game has ended, and the player has lost (e.g., HP reached 0).
     */
    GAME_OVER_LOSE,

    /**
     * The game has ended because the player chose to give up.
     */
    GAME_OVER_GIVE_UP
}
