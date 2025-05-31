package com.minutestilldawn.game.Model;

public class User {
    private String username;
    private String password; // Store hashed password
    private int securityQuestionIndex;
    private String securityAnswer;
    private int avatarId;

    // New fields for player statistics
    private int totalScore;
    private int highestScore;
    private int totalKills;
    private float longestSurvivalTimeSeconds; // Total time played or longest single session

    // Constructor for new registration
    public User(String username, String password, int securityQuestionIndex, String securityAnswer, int avatarId) {
        this.username = username;
        this.password = password; // Should be hashed before passing here
        this.securityQuestionIndex = securityQuestionIndex;
        this.securityAnswer = securityAnswer;
        this.avatarId = avatarId;
        this.totalScore = 0;
        this.highestScore = 0;
        this.totalKills = 0;
        this.longestSurvivalTimeSeconds = 0f;
    }

    // Constructor for retrieving from DB (includes new stats)
    public User(String username, String password, int securityQuestionIndex, String securityAnswer, int avatarId,
            int totalScore, int highestScore, int totalKills, float longestSurvivalTimeSeconds) {
        this.username = username;
        this.password = password;
        this.securityQuestionIndex = securityQuestionIndex;
        this.securityAnswer = securityAnswer;
        this.avatarId = avatarId;
        this.totalScore = totalScore;
        this.highestScore = highestScore;
        this.totalKills = totalKills;
        this.longestSurvivalTimeSeconds = longestSurvivalTimeSeconds;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getSecurityQuestionIndex() {
        return securityQuestionIndex;
    }

    public String getSecurityAnswer() {
        return securityAnswer;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public float getLongestSurvivalTimeSeconds() {
        return longestSurvivalTimeSeconds;
    }

    // Setters for profile updates (username, password, avatar might be handled by
    // DAO directly)
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    } // Ensure it's hashed

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public void setSecurityQuestionIndex(int securityQuestionIndex) {
        this.securityQuestionIndex = securityQuestionIndex;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    // Setters for stats (usually updated by DAO after a game)
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public void setTotalKills(int totalKills) {
        this.totalKills = totalKills;
    }

    public void setLongestSurvivalTimeSeconds(float longestSurvivalTimeSeconds) {
        this.longestSurvivalTimeSeconds = longestSurvivalTimeSeconds;
    }

    // Method to update stats after a game
    public void updateStatsAfterGame(int gameScore, int gameKills, float gameSurvivalTime) {
        this.totalScore += gameScore;
        if (gameScore > this.highestScore) {
            this.highestScore = gameScore;
        }
        this.totalKills += gameKills;
        if (gameSurvivalTime > this.longestSurvivalTimeSeconds) {
            this.longestSurvivalTimeSeconds = gameSurvivalTime;
        }
    }
}
