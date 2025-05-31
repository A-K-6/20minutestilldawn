package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Model.SqliteUserDao; // Use the SQLite DAO
import com.minutestilldawn.game.Model.UserDao;
import com.minutestilldawn.game.View.BaseMenuView;
import com.minutestilldawn.game.View.LoginMenuView;

public class LoginMenuController extends BaseMenuController {
    private Main gameInstance;
    private LoginMenuView view;
    private UserDao userDao; // Use the interface

    public LoginMenuController(Main gameInstance, UserDao userDao) {
        this.gameInstance = gameInstance;
        this.userDao = userDao; 
        this.userDao.initialize(); // Ensure DB table is set up
    }

    @Override
    public void setView(BaseMenuView view) {
        super.setView(view);
        this.view = (LoginMenuView) view;
    }

    public void onLoginClicked(String username, String password) {
        // --- Input Validation ---
        if (username.isEmpty() || password.isEmpty()) {
            if (view != null)
                view.setValidationMessage("Username and password are required.", Color.RED);
            return;
        }
        String hashPass = Tools.hashPassword(password);
        // --- Validate User against Database ---
        if (userDao.validateUser(username, hashPass)) {
            if (view != null)
                view.setValidationMessage("Login successful!", Color.GREEN);
            Gdx.app.log("LoginController", "User logged in: " + username);
            // TODO: Store logged-in user information (e.g., in a session manager)
            // TODO: Set Current user and Player. 
    
            gameInstance.setMainMenuScreen();
            if (view != null)
                view.clearInputFields(); // Clear fields after successful login
        } else {
            if (view != null)
                view.setValidationMessage("Invalid username or password.", Color.RED);
            Gdx.app.error("LoginController", "Login failed for user: " + username);
        }
    }

    @Override
    public void onButtonClicked(String buttonId) {
        Gdx.app.log("LoginController", "Button clicked: " + buttonId);
        switch (buttonId) {
            case "register":
                gameInstance.setRegistrationScreen(); // Navigate to registration screen
                break;
        }
    }

    // Don't forget to dispose the DAO when the game is disposed
    public void dispose() {
        if (userDao != null) {
            userDao.dispose();
        }
    }

public void onForgotPasswordClicked(String username) {
    if (username.isEmpty()) {
        if (view != null)
            view.setValidationMessage("Please enter your username for password recovery.", Color.RED);
        return;
    }

    String securityQuestionPhrase = userDao.getSecurityQuestionPhrase(username);
    if (securityQuestionPhrase.isEmpty() || securityQuestionPhrase == null) { // Index not found
        if (view != null)
            view.setValidationMessage("Username not found.", Color.RED);
        return;
    }
    
    // Now prompt the user for an answer and new password (for example, using a dialog).
    Gdx.app.log("LoginController", "Security question for " + username + ": " + securityQuestionPhrase);
    if (view != null)
        view.setValidationMessage("Security question: " + securityQuestionPhrase, Color.BLUE);
    }
}