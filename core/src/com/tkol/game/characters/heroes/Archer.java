package com.tkol.game.characters.heroes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.characters.CharactersManager;
import com.tkol.game.characters.heroes.Heroes;

public class Archer extends Heroes {
    public Archer(float initialX, float initialY, CharactersManager charactersManager) {
        super(initialX, initialY, 80, 30, charactersManager);
    }

    @Override
    protected void initTexture() {
        Texture archerTexture = new Texture(Gdx.files.internal("characters/Archer.png"));
        setCharacterRegion(new TextureRegion(archerTexture));
    }


}
