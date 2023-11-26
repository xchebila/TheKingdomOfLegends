package com.tkol.game.characters.heroes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.characters.CharactersManager;
import com.tkol.game.characters.heroes.Heroes;

public class Barbarian extends Heroes {
    public Barbarian(float initialX, float initialY, CharactersManager charactersManager) {
        super(initialX, initialY, 100, 20, charactersManager);
    }

    @Override
    protected void initTexture() {
        Texture barbarianTexture = new Texture(Gdx.files.internal("characters/Barbarian.png"));
        setCharacterRegion(new TextureRegion(barbarianTexture));
    }

}

