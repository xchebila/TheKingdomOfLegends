package com.tkol.game.inventory.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Items {
    private final String name;
    private final String description;
    private final TextureRegion texture;
    private final int GOLD_COST;
    private int number;

    public Items(String name, int GOLD_COST, String description, TextureRegion texture) {
        this.name = name;
        this.GOLD_COST = GOLD_COST;
        this.description = description;
        this.texture = texture;
        this.number = 1;
    }

    public int getGoldCost() {
        return GOLD_COST;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public int getNumber() {
        return number;
    }

    public void incrementNumber() {
        number++;
    }
}

