package com.minutestilldawn.game.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Controller.LoginMenuController;
import com.minutestilldawn.game.Model.GameAssetManager;

public class LoginMenuView extends BaseMenuView {
    private final LoginMenuController controller;

    private TextField usernameField;
    private TextField passwordField;
    private Label validationMessageLabel; // To display validation errors

    public LoginMenuView(LoginMenuController controller, Skin skin) {
        super(skin);
        this.controller = controller;
        this.controller.setView(this);
    }

    @Override
    protected void setupUI() {
        Texture backgroundTexture = Main.getInstance().getAssetManager()
                .get(GameAssetManager.LOGIN_REGISTER_MENU_BACKGROUND, Texture.class);
        Drawable background = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        table.setFillParent(true); // Ensures the table takes up the full screen/parent actor size

        // Set the background of the table to the login menu background
        table.setBackground(background);

        // We can remove table.center() here, or use it sparingly with specific cells.
        // table.center(); // Removed or adjusted to allow specific elements to go to
        // corners

        table.defaults().pad(10);

        // --- Title ---
        Label titleLabel = new Label("Login", skin, "title");
        if (!skin.has("title", Label.LabelStyle.class)) {
            titleLabel.setStyle(skin.get("default", Label.LabelStyle.class));
            titleLabel.setFontScale(1.5f);
        }
        table.add(titleLabel)
                .expandX().top().right() // Expand horizontally, align to top and right
                .padTop(20).padRight(20) // Add some padding from the actual corner
                .row(); // Move to the next row for subsequent elements
        // --- Empty space to push subsequent elements down and center them ---
        // Add an empty cell that expands vertically. This pushes everything added
        // after it downwards within the table, allowing the input fields/buttons
        // to be visually centered while the title stays top-right.
        table.add().expandY().row();

        Table inputTable = new Table(skin);
        inputTable.defaults().pad(5);

        // --- Username ---
        inputTable.add(new Label("Username:", skin)).right();
        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username");
        inputTable.add(usernameField).width(300).row();

        // --- Password ---
        inputTable.add(new Label("Password:", skin)).right();
        passwordField = new TextField("", skin);
        passwordField.setMessageText("Enter password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        inputTable.add(passwordField).width(300).row();

        // Add the inputTable to the main table, centered
        table.add(inputTable).colspan(2).center().row(); // Spans 2 columns if needed, centers the whole input table

        // --- Validation Message Label ---
        validationMessageLabel = new Label("", skin);
        validationMessageLabel.setColor(Color.RED);
        table.add(validationMessageLabel).colspan(2).padTop(10).center().row();

        // --- Buttons ---
        TextButton loginButton = new TextButton("Login", skin);
        TextButton registerButton = new TextButton("Register", skin); // To go to registration screen

        table.add(loginButton).colspan(2).width(200).height(50).padTop(20).row();
        table.add(registerButton).colspan(2).width(200).height(50).padTop(10).row();

        // --- Listeners ---
        loginButton.addListener(event -> {
            if (loginButton.isPressed()) {
                controller.onLoginClicked(usernameField.getText(), passwordField.getText());
            }
            return false;
        });

        registerButton.addListener(event -> {
            if (registerButton.isPressed()) {
                controller.onButtonClicked("register");
            }
            return false;
        });
    }

    // Methods to update UI elements based on controller actions
    public void setValidationMessage(String message, Color color) {
        validationMessageLabel.setText(message);
        validationMessageLabel.setColor(color);
    }

    public void clearInputFields() {
        usernameField.setText("");
        passwordField.setText("");
        validationMessageLabel.setText("");
    }
}