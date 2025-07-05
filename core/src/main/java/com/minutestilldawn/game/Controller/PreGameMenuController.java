package com.minutestilldawn.game.Controller;

import com.badlogic.gdx.Gdx;
import com.minutestilldawn.game.Main;
import com.minutestilldawn.game.Model.CharacterType;
import com.minutestilldawn.game.View.BaseMenuView;
import com.minutestilldawn.game.View.PreGameMenuView;

public class PreGameMenuController extends BaseMenuController {

    private Main gameInstance;
    private PreGameMenuView view;

    // Default selections
    private CharacterType selectedCharacter = CharacterType.SHANA;
    private String selectedWeapon = "Revolver"; // Must match names in GameState.createWeaponFromName
    private float selectedDurationMinutes = 2f; // Default to 2 minutes

    public PreGameMenuController(Main gameInstance) {
        this.gameInstance = gameInstance;
    }

    @Override
    public void setView(BaseMenuView view) {
        super.setView(view);
        this.view = (PreGameMenuView) view;
        // Initialize view with current selections if needed, or provide data for
        // SelectBoxes
        if (this.view != null) {
            this.view.updateSelectedCharacterLabel(selectedCharacter.name());
            this.view.updateSelectedWeaponLabel(selectedWeapon);
            this.view.updateSelectedDurationLabel(selectedDurationMinutes + " min");
        }
    }

    @Override
    public void onButtonClicked(String buttonId) {
        Gdx.app.log("PreGameMenuController", "Button clicked: " + buttonId);
        switch (buttonId) {
            case "start_game":
                // Pass the selections to Main to start the game
                gameInstance.startNewGameSession(selectedCharacter, selectedWeapon, selectedDurationMinutes);
                break;
            case "back_to_main_menu":
                gameInstance.setMainMenuScreen();
                break;
            // Add cases for character/weapon/duration selection if using separate buttons
            // If using SelectBox, their ChangeListeners will call methods below
        }
    }

    // Methods to be called by View when selections change
    public void characterSelected(CharacterType character) {
        this.selectedCharacter = character;
        if (view != null)
            view.updateSelectedCharacterLabel(character.name());
        Gdx.app.log("PreGameMenuController", "Character selected: " + character.name());
    }

    public void weaponSelected(String weapon) {
        this.selectedWeapon = weapon;
        if (view != null)
            view.updateSelectedWeaponLabel(weapon);
        Gdx.app.log("PreGameMenuController", "Weapon selected: " + weapon);
    }

    public void durationSelected(float durationMinutes) {
        this.selectedDurationMinutes = durationMinutes;
        if (view != null)
            view.updateSelectedDurationLabel(durationMinutes + " min");
        Gdx.app.log("PreGameMenuController", "Duration selected: " + durationMinutes + " min");
    }

    // --- Getters for the view to populate selection UI (e.g., SelectBox items) ---
    public CharacterType[] getAvailableCharacters() {
        return CharacterType.values();
    }

    public String[] getAvailableWeapons() {
        // Names must match those handled in GameState.createWeaponFromName
        return new String[] { "Revolver", "Shotgun", "Dual SMGs" };
    }

    public Float[] getAvailableDurations() {
        // Durations in minutes as per PDF
        return new Float[] { 2f, 5f, 10f, 20f };
    }

    public CharacterType getSelectedCharacter() {
        return selectedCharacter;
    }

    public String getSelectedWeapon() {
        return selectedWeapon;
    }

    public float getSelectedDurationMinutes() {
        return selectedDurationMinutes;
    }
}
