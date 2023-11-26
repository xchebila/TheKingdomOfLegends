package com.tkol.game.characters.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.game.BossProgressManager;
import com.tkol.game.characters.MovementManager;
import com.tkol.game.characters.CharactersManager;

public class BossVillage extends Boss {
    public BossVillage(float initialX, float initialY, MovementManager movementManager, CharactersManager charactersManager) {
        super(initialX, initialY, calculateInitialMaxHP(), calculateInitialDamage(),500, movementManager, charactersManager);
    }

    private static int calculateInitialMaxHP() {
        int initialMaxHP = 1000;
        int defeatCount = BossProgressManager.getDefeatCount();
        float multiplier = 1.5f + 0.2f * defeatCount;
        return (int) (initialMaxHP * multiplier);
    }

    private static int calculateInitialDamage() {
        int initialDamage = 50;
        int defeatCount = BossProgressManager.getDefeatCount();
        float multiplier = 1.5f + 0.2f * defeatCount;
        return (int) (initialDamage * multiplier);
    }

    @Override
    protected void initTexture() {
        Texture bossVillageTexture = new Texture(Gdx.files.internal("boss/BossVillage.png"));
        setCharacterRegion(new TextureRegion(bossVillageTexture));
    }

}
