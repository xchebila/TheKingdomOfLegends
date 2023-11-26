package com.tkol.game.characters.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.characters.MovementManager;
import com.tkol.game.characters.CharactersManager;

public class Spider extends Monsters {

    public Spider(float initialX, float initialY, MovementManager movementManager, CharactersManager charactersManager) {
        super(initialX, initialY, 40, 10, 10, movementManager, charactersManager);
    }

    @Override
    protected void initTexture() {
        Texture spiderTexture = new Texture(Gdx.files.internal("monsters/Spider.png"));
        setCharacterRegion(new TextureRegion(spiderTexture));
    }

}