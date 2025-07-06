package com.minutestilldawn.game.View;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.minutestilldawn.game.Controller.HintMenuController;

public class HintMenuView extends BaseMenuView {
    private final HintMenuController controller;

    public HintMenuView(HintMenuController hintMenuController, Skin uiSkin) {
        super(uiSkin);
        this.controller = hintMenuController;
    }

    private String getLocalizedDetails() {
        return "Abilities\n" +
                "    Vitality: Health increment for 1 unit\n" +
                "    Damager: 25% increment of weapon damage for 10 seconds\n" +
                "    Amocrease: Magazine extension of 5 bullets\n" +
                "    Speedy: Player's speed gets doubled for 10 seconds\n\n" +
                "Cheat Codes\n" +
                "    Num_1: Passing the game time for 60 seconds\n" +
                "    Num_2: The player's level increase for 1 unit\n" +
                "    Num_3: Player will face the boss fight!\n" +
                "    Num_4: Enjoy using your 1000 bullets!\n\n" +
                "Characters\n" +
                "    Shana -> Speed: 4 / HP: 4\n" +
                "    Lilith -> Speed: 1 / HP: 7\n" +
                "    Diamond -> Speed: 5 / HP: 3\n" +
                "    Dasher -> Speed: 3 / HP: 5\n" +
                "    Scarlet -> Speed: 10 / HP: 2\n\n" +
                "Game controllers\n" +
                "    W: Going upward\n" +
                "    S: Going downward\n" +
                "    D: Going to the right side\n" +
                "    A: Going to the left side\n" +
                "    R: Reload (when auto reload is off)\n" +
                "    Space: Enable/Disable auto aim\n" +
                "    Left Click: Shoot!";
    }

    @Override
    protected void setupUI() {
        // Use the 'table' field from BaseMenuView
        table.center();

        Label title = new Label("Hint Menu", skin, "title");
        table.add(title).padBottom(20).row();

        Label detailsLabel = new Label(getLocalizedDetails(), skin);
        detailsLabel.setWrap(true);

        // Use a ScrollPane to make the text scrollable if it's too long
        ScrollPane scrollPane = new ScrollPane(detailsLabel, skin);
        scrollPane.setFadeScrollBars(false);
        table.add(scrollPane).expand().fill().pad(20).row();

        TextButton backButton = new TextButton("Back", skin);
        table.add(backButton).width(200).height(50).padTop(20).row();

        backButton.addListener(event -> {
            if (backButton.isPressed()) {
                controller.onButtonClicked("back");
            }
            return false;
        });
    }
}