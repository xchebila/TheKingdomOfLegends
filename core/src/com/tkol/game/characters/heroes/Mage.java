package com.tkol.game.characters.heroes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.characters.CharactersManager;
import com.tkol.game.characters.heroes.Heroes;

public class Mage extends Heroes {

    public Mage(float initialX, float initialY, CharactersManager charactersManager) {
        super(initialX, initialY, 120, 10, charactersManager);
    }

    @Override
    protected void initTexture() {
        Texture mageTexture = new Texture(Gdx.files.internal("characters/Mage.png"));
        setCharacterRegion(new TextureRegion(mageTexture));
    }

}
