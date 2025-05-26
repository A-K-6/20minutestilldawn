package com.minutestilldawn.game.Model;

public interface UserDao {
    void initialize(); // To create table if it doesn't exist
    boolean addUser(User user);
    User getUserByUsername(String username);
    boolean validateUser(String username, String password);
    boolean userExists(String username);
    int getSecurityQuestion(String username); 
    void dispose(); // For closing connections
}