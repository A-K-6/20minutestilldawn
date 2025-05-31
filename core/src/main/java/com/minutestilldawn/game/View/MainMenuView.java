package com.minutestilldawn.game.View;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Controller.MainMenuController;
import com.minutestilldawn.game.Model.GameAssetManager;
import com.minutestilldawn.game.Model.User;

public class MainMenuView extends BaseMenuView {

    private final MainMenuController controller;

    public MainMenuView(MainMenuController controller, Skin skin) {
        super(skin);
        this.controller = controller;
        // The controller needs a reference to this view for specific view interactions
        // if any
        this.controller.setView(this); // BaseMenuController has setView
    }

    @Override
    protected void setupUI() {
        Texture backgroundTexture = Main.getInstance().getAssetManager()
                .get(GameAssetManager.LOGIN_REGISTER_MENU_BACKGROUND, Texture.class);
        Drawable background = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        table.setFillParent(true); // Ensures the table takes up the full screen/parent actor size

        // Set the background of the table to the login menu background
        table.setBackground(background);

        User user = Main.getInstance().getCurrentUser();
        boolean isGuest = Main.getInstance().isGuestSession();
        // Avatar
        Texture avatarTexture;
        if (isGuest || user == null) {
            avatarTexture = Main.getInstance().getAssetManager().get("avatars/default_avatar.png", Texture.class);
        } else {
            // Example: avatars/avatar_1.png, avatars/avatar_2.png, etc.
            String avatarPath = "avatars/avatar_" + user.getAvatarId() + ".png";
            if (Main.getInstance().getAssetManager().isLoaded(avatarPath)) {
                avatarTexture = Main.getInstance().getAssetManager().get(avatarPath, Texture.class);
            } else {
                avatarTexture = Main.getInstance().getAssetManager().get(GameAssetManager.Default_Avatar,
                        Texture.class);
            }
        }
        Image avatarImage = new Image(avatarTexture);
        table.add(avatarImage).size(96, 96).padBottom(10).row();
        // User Name
        String usernameText = isGuest || user == null ? "Guest" : user.getUsername();
        Label usernameLabel = new Label("User: " + usernameText, skin);
        table.add(usernameLabel).padBottom(5).row();
        if (!isGuest && user != null) {
            Label scoreLabel = new Label("Highest Score: " + user.getHighestScore(), skin);
            Label killsLabel = new Label("Total Kills: " + user.getTotalKills(), skin);
            table.add(scoreLabel).padBottom(2).row();
            table.add(killsLabel).padBottom(10).row();
        }

        // --- Menu Buttons ---
        TextButton playButton = new TextButton("Play", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton profileButton = new TextButton("Profile", skin);
        TextButton scoreboardButton = new TextButton("Scoreboard", skin);
        TextButton hintButton = new TextButton("hint", skin);
        TextButton continueSavedGameButton = new TextButton("Continue Saved Game", skin);
        TextButton logoutButton = new TextButton("Logout", skin);
        TextButton exitButton = new TextButton("Exit", skin);
        // TODO‌: Show the avatar pic (5 points) with the pictures they have.
        // TODO:‌‌ show the User‌Scors
        // TODO‌: show the Current user Name.
        // --- Layout using Table ---
        table.center(); // Center the table in the screen
        table.defaults().pad(10).width(200).height(50); // Default padding and size for buttons

        table.add(playButton).row();
        table.add(settingsButton).row();
        table.add(profileButton).row();
        table.add(scoreboardButton).row();
        table.add(hintButton).row();
        table.add(continueSavedGameButton).row();
        table.add(logoutButton).row(); // TODO: do the Logout Works Properly
        table.add(exitButton).row();

        // --- Add Listeners to Buttons ---
        // Play Button
        playButton.addListener(event -> {
            if (playButton.isPressed()) { // Check if the button is actually pressed down
                controller.onButtonClicked("play");
            }
            return false; // Return false to indicate event is not consumed if you want it to propagate
        });

        // Settings Button
        settingsButton.addListener(event -> {
            if (settingsButton.isPressed()) {
                controller.onButtonClicked("settings");
            }
            return false;
        });

        // Profile Button
        profileButton.addListener(event -> {
            if (profileButton.isPressed()) {
                controller.onButtonClicked("profile");
            }
            return false;
        });

        // Scoreboard Button
        scoreboardButton.addListener(event -> {
            if (scoreboardButton.isPressed()) {
                controller.onButtonClicked("scoreboard");
            }
            return false;
        });
        logoutButton.addListener(event -> {
            if (logoutButton.isPressed()) {
                controller.onButtonClicked("logout");
            }
            return false;
        });

        // Exit Button
        exitButton.addListener(event -> {
            if (exitButton.isPressed()) {
                controller.onButtonClicked("exit");
            }
            return false;
        });
    }
}