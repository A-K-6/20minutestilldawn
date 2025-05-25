package com.minutestilldawn.game.Model;

import com.badlogic.gdx.Gdx;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteUserDao implements UserDao {
    private static final String DB_NAME = "users.db";
    private static final String TABLE_NAME = "users";
    private Connection connection;

    @Override
    public void initialize() {
        try {
            Class.forName("org.sqlite.JDBC"); // Load the JDBC driver
            String dbPath = Gdx.files.external(DB_NAME).path(); // Use external storage for flexibility
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Gdx.app.log("SqliteUserDao", "Database connection established to: " + dbPath);

            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                                    "username TEXT PRIMARY KEY," +
                                    "password TEXT NOT NULL," +
                                    "security_question_index INTEGER NOT NULL," +
                                    "security_answer TEXT NOT NULL," +
                                    "avatar_id INTEGER NOT NULL" +
                                    ");";
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableSQL);
                Gdx.app.log("SqliteUserDao", "Table '" + TABLE_NAME + "' ensured to exist.");
            }
        } catch (ClassNotFoundException e) {
            Gdx.app.error("SqliteUserDao", "SQLite JDBC driver not found.", e);
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error initializing database.", e);
        }
    }

    @Override
    public boolean addUser(User user) {
        if (connection == null) {
            Gdx.app.error("SqliteUserDao", "Database not initialized.");
            return false;
        }
        if (userExists(user.getUsername())) {
            Gdx.app.log("SqliteUserDao", "User already exists: " + user.getUsername());
            return false;
        }

        String insertSQL = "INSERT INTO " + TABLE_NAME + " (username, password, security_question_index, security_answer, avatar_id) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Remember: hash in production!
            pstmt.setInt(3, user.getSecurityQuestionIndex());
            pstmt.setString(4, user.getSecurityAnswer());
            pstmt.setInt(5, user.getAvatarId());
            pstmt.executeUpdate();
            Gdx.app.log("SqliteUserDao", "User added: " + user.getUsername());
            return true;
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error adding user: " + user.getUsername(), e);
            return false;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        if (connection == null) {
            Gdx.app.error("SqliteUserDao", "Database not initialized.");
            return null;
        }

        String selectSQL = "SELECT username, password, security_question_index, security_answer, avatar_id FROM " + TABLE_NAME + " WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String fetchedUsername = rs.getString("username");
                String fetchedPassword = rs.getString("password");
                int fetchedQuestionIndex = rs.getInt("security_question_index");
                String fetchedAnswer = rs.getString("security_answer");
                int fetchedAvatarId = rs.getInt("avatar_id");
                return new User(fetchedUsername, fetchedPassword, fetchedQuestionIndex, fetchedAnswer, fetchedAvatarId);
            }
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error getting user: " + username, e);
        }
        return null;
    }

    @Override
    public boolean validateUser(String username, String password) {
        if (connection == null) {
            Gdx.app.error("SqliteUserDao", "Database not initialized.");
            return false;
        }

        String selectSQL = "SELECT password FROM " + TABLE_NAME + " WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                // In production, use a secure password verification library (e.g., BCrypt.checkpw)
                return storedPassword.equals(password); // Simple comparison for this example
            }
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error validating user: " + username, e);
        }
        return false;
    }

    @Override
    public boolean userExists(String username) {
        if (connection == null) {
            Gdx.app.error("SqliteUserDao", "Database not initialized.");
            return false;
        }

        String selectSQL = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE username = ?;";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            Gdx.app.error("SqliteUserDao", "Error checking if user exists: " + username, e);
        }
        return false;
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