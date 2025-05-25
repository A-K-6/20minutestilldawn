package com.minutestilldawn.game.View;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Controller.RegistrationMenuController;
import com.minutestilldawn.game.Model.GameAssetManager;

import java.util.Random; // For random avatar

public class RegistrationMenuView extends BaseMenuView {
    private final RegistrationMenuController controller;

    private TextField usernameField;
    private TextField passwordField;
    private TextField repeatPasswordField;
    private SelectBox<String> securityQuestionSelectBox;
    private TextField securityAnswerField;
    private Label validationMessageLabel; // To display validation errors
    private Label avatarDisplayLabel; // Placeholder for avatar display

    public RegistrationMenuView(RegistrationMenuController controller, Skin skin) {
        super(skin);
        this.controller = controller;
        this.controller.setView(this); // Set the view reference in the controller
    }

    @Override
    protected void setupUI() {
        Texture backgroundTexture = Main.getInstance().getAssetManager()
                .get(GameAssetManager.MENU_BACKGROUND_TEXTURE, Texture.class);
        Drawable background = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        table.setFillParent(true); // Ensures the table takes up the full screen/parent actor size

        // Set the background of the table to the login menu background
        table.setBackground(background);
        table.center();
        table.defaults().pad(5); // Reduced padding for more fields

        // --- Title ---
        Label titleLabel = new Label("Register", skin, "title");
        if (!skin.has("title", Label.LabelStyle.class)) {
            titleLabel.setStyle(skin.get("default", Label.LabelStyle.class));
            titleLabel.setFontScale(1.5f);
        }
        table.add(titleLabel).colspan(2).padBottom(20).row();

        // --- Username ---
        table.add(new Label("Username:", skin)).right();
        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username");
        table.add(usernameField).width(300).row();

        // --- Password ---
        table.add(new Label("Password:", skin)).right();
        passwordField = new TextField("", skin);
        passwordField.setMessageText("Enter password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        table.add(passwordField).width(300).row();

        // --- Repeat Password ---
        table.add(new Label("Repeat Password:", skin)).right();
        repeatPasswordField = new TextField("", skin);
        repeatPasswordField.setMessageText("Repeat password");
        repeatPasswordField.setPasswordMode(true);
        repeatPasswordField.setPasswordCharacter('*');
        table.add(repeatPasswordField).width(300).row();

        // --- Security Question ---
        table.add(new Label("Security Question:", skin)).right();
        securityQuestionSelectBox = new SelectBox<>(skin);
        securityQuestionSelectBox.setItems(controller.getSecurityQuestions()); // Get questions from controller
        table.add(securityQuestionSelectBox).width(300).row();

        table.add(new Label("Your Answer:", skin)).right();
        securityAnswerField = new TextField("", skin);
        securityAnswerField.setMessageText("Enter answer");
        table.add(securityAnswerField).width(300).row();

        // --- Random Avatar ---
        // For simplicity, we'll just show a label with an avatar ID.
        // In a real game, this would load an actual image.
        avatarDisplayLabel = new Label("Avatar: None Selected", skin);
        table.add(avatarDisplayLabel).colspan(2).padTop(10).row();

        TextButton randomAvatarButton = new TextButton("Random Avatar", skin);
        table.add(randomAvatarButton).colspan(2).width(200).height(40).padTop(5).row();

        // --- Validation Message Label ---
        validationMessageLabel = new Label("", skin);
        validationMessageLabel.setColor(Color.RED);
        table.add(validationMessageLabel).colspan(2).padTop(10).row();

        // --- Buttons ---
        TextButton registerButton = new TextButton("Register", skin);
        TextButton skipAsGuestButton = new TextButton("Skip and Play as Guest", skin);
        TextButton backToLoginButton = new TextButton("Back to Login", skin); // Added for navigation

        table.add(registerButton).colspan(2).width(200).height(50).padTop(20).row();
        table.add(skipAsGuestButton).colspan(2).width(300).height(50).padTop(10).row();
        table.add(backToLoginButton).colspan(2).width(200).height(50).padTop(10).row();

        // --- Listeners ---
        randomAvatarButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int randomId = new Random().nextInt(4) + 1; // Example: 4 possible avatars
                controller.onRandomAvatarSelected(randomId);
            }
        });

        registerButton.addListener(event -> {
            if (registerButton.isPressed()) {
                controller.onRegisterClicked(
                        usernameField.getText(),
                        passwordField.getText(),
                        repeatPasswordField.getText(),
                        securityQuestionSelectBox.getSelectedIndex(),
                        securityAnswerField.getText());
            }
            return false;
        });

        skipAsGuestButton.addListener(event -> {
            if (skipAsGuestButton.isPressed()) {
                controller.onButtonClicked("skip_as_guest");
            }
            return false;
        });

        backToLoginButton.addListener(event -> {
            if (backToLoginButton.isPressed()) {
                controller.onButtonClicked("back_to_login");
            }
            return false;
        });
    }

    // Methods to update UI elements based on controller actions
    public void setValidationMessage(String message, Color color) {
        validationMessageLabel.setText(message);
        validationMessageLabel.setColor(color);
    }

    public void setAvatarDisplay(String text) {
        avatarDisplayLabel.setText("Avatar ID: " + text);
    }

    public void clearInputFields() {
        usernameField.setText("");
        passwordField.setText("");
        repeatPasswordField.setText("");
        securityAnswerField.setText("");
        validationMessageLabel.setText("");
        avatarDisplayLabel.setText("Avatar: None Selected");
    }
}