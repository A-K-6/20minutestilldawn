package com.minutestilldawn.game.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array; // For getTopUsers

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteUserDao implements UserDao {
    private static final String DB_NAME = "user_data.db"; // Changed name slightly for clarity
    private static final String TABLE_USERS = "users";

    // Security questions array - should match RegistrationMenuController
    private static final String[] SECURITY_QUESTIONS = {
            "What is your best friend's name?",
            "What was your first pet's name?",
            "What city were you born in?",
            "What is your favorite book?",
            "What is your favorite color?"
    };

    private Connection connection;

    public SqliteUserDao() {
        // Constructor can be empty, initialize does the work
    }

    @Override
    public void initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
            // Store DB in user's application directory for better platform compatibility
            String dbPath = Gdx.files.local(DB_NAME).path();
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Gdx.app.log("SqliteUserDao", "Database connection established: " + dbPath);

            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                    "username TEXT PRIMARY KEY NOT NULL," +
                    "password TEXT NOT NULL," + // Hashed password
                    "security_question_index INTEGER NOT NULL," +
                    "security_answer TEXT NOT NULL," + // Should also be hashed ideally
                    "avatar_id INTEGER NOT NULL DEFAULT 0," +
                    "total_score INTEGER NOT NULL DEFAULT 0," +
                    "highest_score INTEGER NOT NULL DEFAULT 0," +
                    "total_kills INTEGER NOT NULL DEFAULT 0," +
                    "longest_survival_seconds REAL NOT NULL DEFAULT 0.0" +
                    ");";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createUserTableSQL);
                Gdx.app.log("SqliteUserDao", "Table '" + TABLE_USERS + "' ensured to exist.");
            }
        } catch (ClassNotFoundException e) {
            Gdx.app.error("SqliteUserDao", "SQLite JDBC driver not found.", e);
            throw new RuntimeException("Failed to load SQLite JDBC driver", e);
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error initializing database.", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    @Override
    public boolean addUser(User user) {
        if (userExists(user.getUsername())) {
            Gdx.app.log("SqliteUserDao", "User already exists: " + user.getUsername());
            return false;
        }
        String sql = "INSERT INTO " + TABLE_USERS +
                "(username, password, security_question_index, security_answer, avatar_id, " +
                "total_score, highest_score, total_kills, longest_survival_seconds) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Expecting hashed password
            pstmt.setInt(3, user.getSecurityQuestionIndex());
            pstmt.setString(4, user.getSecurityAnswer()); // Consider hashing this too
            pstmt.setInt(5, user.getAvatarId());
            pstmt.setInt(6, user.getTotalScore());
            pstmt.setInt(7, user.getHighestScore());
            pstmt.setInt(8, user.getTotalKills());
            pstmt.setFloat(9, user.getLongestSurvivalTimeSeconds());
            pstmt.executeUpdate();
            Gdx.app.log("SqliteUserDao", "User added: " + user.getUsername());
            return true;
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error adding user " + user.getUsername(), e);
            return false;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM " + TABLE_USERS + " WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("security_question_index"),
                        rs.getString("security_answer"),
                        rs.getInt("avatar_id"),
                        rs.getInt("total_score"),
                        rs.getInt("highest_score"),
                        rs.getInt("total_kills"),
                        rs.getFloat("longest_survival_seconds")
                );
            }
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error fetching user " + username, e);
        }
        return null;
    }

    @Override
    public boolean validateUser(String username, String hashedPassword) {
        User user = getUserByUsername(username);
        if (user != null) {
            // IMPORTANT: This assumes password in DB is already hashed with the same method.
            return user.getPassword().equals(hashedPassword);
        }
        return false;
    }

    @Override
    public boolean userExists(String username) {
        String sql = "SELECT 1 FROM " + TABLE_USERS + " WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error checking if user exists: " + username, e);
            return false; // Or throw runtime exception
        }
    }

    @Override
    public boolean updatePassword(String username, String newHashedPassword) {
        String sql = "UPDATE " + TABLE_USERS + " SET password = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newHashedPassword);
            pstmt.setString(2, username);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error updating password for " + username, e);
            return false;
        }
    }
    
    @Override
    public boolean updateUsername(String oldUsername, String newUsername) {
        if (userExists(newUsername)) {
            Gdx.app.log("SqliteUserDao", "Cannot update username. New username '" + newUsername + "' already exists.");
            return false;
        }
        String sql = "UPDATE " + TABLE_USERS + " SET username = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, oldUsername);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error updating username from " + oldUsername + " to " + newUsername, e);
            return false;
        }
    }

    @Override
    public boolean updateAvatar(String username, int newAvatarId) {
        String sql = "UPDATE " + TABLE_USERS + " SET avatar_id = ? WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newAvatarId);
            pstmt.setString(2, username);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error updating avatar for " + username, e);
            return false;
        }
    }
    
    @Override
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM " + TABLE_USERS + " WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error deleting user " + username, e);
            return false;
        }
    }


    @Override
    public String getSecurityQuestionPhrase(String username) {
        User user = getUserByUsername(username);
        if (user != null) {
            int index = user.getSecurityQuestionIndex();
            if (index >= 0 && index < SECURITY_QUESTIONS.length) {
                return SECURITY_QUESTIONS[index];
            }
        }
        return null; // Or throw an exception / return a default "Question not found"
    }

    @Override
    public boolean verifySecurityAnswer(String username, String answer) {
        User user = getUserByUsername(username);
        if (user != null) {
            // Consider hashing stored security answers for better security
            return user.getSecurityAnswer().equalsIgnoreCase(answer); // Case-insensitive comparison for now
        }
        return false;
    }

    @Override
    public boolean updateUserStats(String username, int gameScore, int gameKills, float gameSurvivalTime) {
        User user = getUserByUsername(username);
        if (user == null) {
            Gdx.app.error("SqliteUserDao", "Cannot update stats. User not found: " + username);
            return false;
        }

        int newTotalScore = user.getTotalScore() + gameScore;
        int newHighestScore = Math.max(user.getHighestScore(), gameScore);
        int newTotalKills = user.getTotalKills() + gameKills;
        float newLongestSurvival = Math.max(user.getLongestSurvivalTimeSeconds(), gameSurvivalTime);

        String sql = "UPDATE " + TABLE_USERS + " SET " +
                "total_score = ?, highest_score = ?, total_kills = ?, longest_survival_seconds = ? " +
                "WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newTotalScore);
            pstmt.setInt(2, newHighestScore);
            pstmt.setInt(3, newTotalKills);
            pstmt.setFloat(4, newLongestSurvival);
            pstmt.setString(5, username);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                Gdx.app.log("SqliteUserDao", "Stats updated for user: " + username);
                return true;
            }
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error updating stats for " + username, e);
        }
        return false;
    }

    @Override
    public Array<User> getTopUsers(int limit) {
        Array<User> topUsers = new Array<>();
        // Order by highest_score DESC, then by total_score DESC as a tie-breaker
        String sql = "SELECT * FROM " + TABLE_USERS + " ORDER BY highest_score DESC, total_score DESC LIMIT ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                topUsers.add(new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("security_question_index"),
                        rs.getString("security_answer"),
                        rs.getInt("avatar_id"),
                        rs.getInt("total_score"),
                        rs.getInt("highest_score"),
                        rs.getInt("total_kills"),
                        rs.getFloat("longest_survival_seconds")
                ));
            }
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error fetching top users", e);
        }
        return topUsers;
    }
    
    @Override
    public int getUserTotalScore(String username) {
        User user = getUserByUsername(username);
        return (user != null) ? user.getTotalScore() : 0;
    }


    @Override
    public void dispose() {
        if (connection != null) {
            try {
                connection.close();
                Gdx.app.log("SqliteUserDao", "Database connection closed.");
            } catch (SQLException e) {
                Gdx.app.error("SqliteUserDao", "Error closing database connection.", e);
            }
        }
    }
}
