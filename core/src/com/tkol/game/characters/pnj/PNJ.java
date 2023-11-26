package com.tkol.game.characters.pnj;

import com.tkol.game.inventory.Inventory;
import com.tkol.game.characters.Characters;
import com.tkol.game.characters.CharactersManager;
import com.tkol.game.characters.heroes.Heroes;

public abstract class PNJ extends Characters<PNJ> {
    public PNJ(float initialX, float initialY, CharactersManager charactersManager) {
        super(initialX, initialY, 100, 0, charactersManager);
    }

    public void update(float deltaTime) {
        currentFrame = 2;
    }

    public void interact(Heroes hero, Inventory inventory) {
    }


}