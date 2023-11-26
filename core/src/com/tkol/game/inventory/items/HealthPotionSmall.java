package com.tkol.game.inventory.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.characters.heroes.Heroes;

public class HealthPotionSmall extends Items implements Usable, Sellable {
    private static final int HEALING_AMOUNT = 10;
    private static final int GOLD_COST = 5;

    public HealthPotionSmall() {
        super("Petite potion de vie", 20, "Restaure 10pv", loadTexture());
    }

    private static TextureRegion loadTexture() {
        Texture texture = new Texture(Gdx.files.internal("images/HealthPotionSmall.png"));
        return new TextureRegion(texture);
    }

    @Override
    public void use(Heroes heroes) {
        heroes.increaseHP(HEALING_AMOUNT);
    }

    @Override
    public int getGoldCost() {
        return GOLD_COST;
    }

}

