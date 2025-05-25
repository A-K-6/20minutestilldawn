package com.minutestilldawn.game.Model;

// Note: For a real application, you'd hash the password (e.g., with BCrypt)
// and store the hashed value, not the plain password. This example uses plain
// for simplicity, but it's INSECURE for production.
public class User {
    private String username;

    private String password; // Store hashed password in production
    private int securityQuestionIndex;
    private String securityAnswer;
    private int avatarId; // For random avatar, e.g., an index to pre-defined avatars

    // Constructor for new registration
    public User(String username, String password, int securityQuestionIndex, String securityAnswer, int avatarId) {
        this.username = username;
        this.password = password;
        this.securityQuestionIndex = securityQuestionIndex;
        this.securityAnswer = securityAnswer;
        this.avatarId = avatarId;
    }

    // Constructor for retrieving from DB (e.g., if you had an ID column)
    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    public void setSecurityQuestionIndex(int securityQuestionIndex) {
        this.securityQuestionIndex = securityQuestionIndex;
    }

    // You might add setters if user profile can be updated later
}