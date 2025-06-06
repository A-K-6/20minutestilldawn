package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Model.SqliteUserDao; // Use the SQLite DAO
import com.minutestilldawn.game.Model.User;
import com.minutestilldawn.game.Model.UserDao; // Interface
import com.minutestilldawn.game.View.BaseMenuView;
import com.minutestilldawn.game.View.RegistrationMenuView;
import com.minutestilldawn.game.Controller.Tools;

public class RegistrationMenuController extends BaseMenuController {
    private Main gameInstance;
    private RegistrationMenuView view;
    private UserDao userDao; // Use the interface

    private String[] securityQuestions = {
            "What is your best friend's name?",
            "What was your first pet's name?",
            "What city were you born in?",
            "What is your favorite book?",
            "What is your favorite color?"
    };

    private int selectedAvatarId = 0; // Default or no avatar selected

    public RegistrationMenuController(Main gameInstance, UserDao userDao) {
        this.gameInstance = gameInstance;
        this.userDao = userDao;
        this.userDao.initialize(); // Ensure DB table is set up
    }

    @Override
    public void setView(BaseMenuView view) {
        super.setView(view);
        this.view = (RegistrationMenuView) view;
    }

    public String[] getSecurityQuestions() {
        return securityQuestions;
    }

    public void onRandomAvatarSelected(int avatarId) {
        this.selectedAvatarId = avatarId;
        if (view != null) {
            view.setAvatarDisplay(String.valueOf(avatarId));
        }
        Gdx.app.log("RegistrationController", "Random avatar selected: " + avatarId);
    }

    public void onRegisterClicked(String username, String password, String repeatPassword,
            int securityQuestionIndex, String securityAnswer) {

        if (username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || securityAnswer.isEmpty()) {
            if (view != null)
                view.setValidationMessage("All fields are required.", Color.RED);
            return;
        }

        if (username.length() < 3) {
            if (view != null)
                view.setValidationMessage("Username must be at least 3 characters.", Color.RED);
            return;
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            if (view != null)
                view.setValidationMessage("Username can only contain letters, numbers, and underscores.", Color.RED);
            return;
        }
        if (password.length() < 8) {
            if (view != null)
                view.setValidationMessage("Password must be at least 6 characters.", Color.RED);
            return;
        }
        // Check for at least one uppercase character
        if (!password.matches(".*[A-Z].*")) {
            if (view != null)
                view.setValidationMessage("Password must contain at least one uppercase letter.", Color.RED);
            return;
        }
        // Check for at least one special character (non-alphanumeric)
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            if (view != null)
                view.setValidationMessage("Password must contain at least one special character.", Color.RED);
            return;
        }
        
        if (!password.equals(repeatPassword)) {
            if (view != null)
                view.setValidationMessage("Passwords do not match.", Color.RED);
            return;
        }

        if (securityQuestionIndex < 0 || securityQuestionIndex >= securityQuestions.length) {
            if (view != null)
                view.setValidationMessage("Please select a security question.", Color.RED);
            return;
        }

        // --- Check if user already exists ---
        if (userDao.userExists(username)) {
            if (view != null)
                view.setValidationMessage("Username already taken.", Color.RED);
            return;
        }

        // --- Create and Save User ---
        String hashPas = Tools.hashPassword(password);
        User newUser = new User(username, hashPas, securityQuestionIndex, securityAnswer, selectedAvatarId);
        if (userDao.addUser(newUser)) {
            if (view != null)
                view.setValidationMessage("Registration successful!", Color.GREEN);
            Gdx.app.log("RegistrationController", "New user registered: " + username);
            // After successful registration, maybe navigate to login screen
            gameInstance.setLoginScreen();
            if (view != null)
                view.clearInputFields(); // Clear fields after successful registration
        } else {
            if (view != null)
                view.setValidationMessage("Registration failed. Try again.", Color.RED);
            Gdx.app.error("RegistrationController", "Failed to add user to DB: " + username);
        }
    }

    @Override
    public void onButtonClicked(String buttonId) {
        Gdx.app.log("RegistrationController", "Button clicked: " + buttonId);
        switch (buttonId) {
            case "skip_as_guest":
                // Handle guest login
                Gdx.app.log("RegistrationController", "Playing as guest.");
                gameInstance.setMainMenuScreen(); // Go directly to game screen
                break;
            case "back_to_login":
                gameInstance.setLoginScreen(); // Navigate to login screen
                break;
        }
    }

    // Don't forget to dispose the DAO when the game is disposed
    public void dispose() {
        if (userDao != null) {
            userDao.dispose();
        }
    }
}