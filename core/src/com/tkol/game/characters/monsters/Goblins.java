package com.tkol.game.characters.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.characters.MovementManager;
import com.tkol.game.characters.CharactersManager;

public class Goblins extends Monsters {

    public Goblins(float initialX, float initialY, MovementManager movementManager, CharactersManager charactersManager) {
        super(initialX, initialY, 60, 2, 5, movementManager, charactersManager);
    }

    @Override
    protected void initTexture() {
        Texture goblinsTexture = new Texture(Gdx.files.internal("monsters/Goblins.png"));
        setCharacterRegion(new TextureRegion(goblinsTexture));
    }


}