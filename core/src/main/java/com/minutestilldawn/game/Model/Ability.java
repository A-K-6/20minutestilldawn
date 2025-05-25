package com.minutestilldawn.game.Model;

public enum Ability {
    VITALITY("Increase max HP by 1 unit"), // Increase max HP by 1 unit [cite: 86]
    DAMAGER("Increase weapon damage by 25% for 10 seconds"), // Increase 25% weapon damage for 10 seconds [cite: 86]
    PROCREASE("Increase weapon projectile by 1 unit"), // Increase weapon projectile by 1 unit [cite: 86]
    AMOCREASE("Increase max weapon ammo by 5 units"), // Increase max weapon ammo by 5 units [cite: 86]
    SPEEDY("Double player movement speed for 10 seconds"); // Double player movement speed for 10 seconds [cite: 86]

    private final String description;

    Ability(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}