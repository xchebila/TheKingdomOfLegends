package com.tkol.game.characters.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.characters.MovementManager;
import com.tkol.game.characters.CharactersManager;

public class Zombie extends Monsters {

    public Zombie(float initialX, float initialY, MovementManager movementManager, CharactersManager charactersManager) {
        super(initialX, initialY, 200, 1, 8, movementManager, charactersManager);
    }

    @Override
    protected void initTexture() {
        Texture zombieTexture = new Texture(Gdx.files.internal("monsters/Zombie.png"));
        setCharacterRegion(new TextureRegion(zombieTexture));
    }


}