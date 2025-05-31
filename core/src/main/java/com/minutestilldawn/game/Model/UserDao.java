package com.minutestilldawn.game.Model;

import com.badlogic.gdx.utils.Array;

public interface UserDao {
    void initialize();

    boolean addUser(User user);

    User getUserByUsername(String username);

    boolean validateUser(String username, String hashedPassword); // Password should be hashed

    boolean userExists(String username);

    boolean updatePassword(String username, String newHashedPassword);

    boolean updateUsername(String oldUsername, String newUsername);

    boolean updateAvatar(String username, int newAvatarId);

    boolean deleteUser(String username); // For profile menu

    // For security question / password recovery
    String getSecurityQuestionPhrase(String username); // Returns the question string

    boolean verifySecurityAnswer(String username, String answer);

    // For stats and scoreboard
    boolean updateUserStats(String username, int gameScore, int gameKills, float gameSurvivalTime);

    Array<User> getTopUsers(int limit); // For scoreboard

    int getUserTotalScore(String username); // For display on main menu

    void dispose();
}
