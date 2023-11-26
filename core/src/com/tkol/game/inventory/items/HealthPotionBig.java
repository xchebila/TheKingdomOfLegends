package com.tkol.game.inventory.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.characters.heroes.Heroes;

public class HealthPotionBig extends Items implements Usable, Sellable {
    private static final int HEALING_AMOUNT = 50;
    private static final int GOLD_COST = 1;

    public HealthPotionBig() {
        super("Grosse potion de vie", 100, "Restaure 50pv", loadTexture());
    }

    private static TextureRegion loadTexture() {
        Texture texture = new Texture(Gdx.files.internal("images/HealthPotionBig.png"));
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

