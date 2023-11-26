package com.tkol.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tkol.game.characters.heroes.Heroes;
import com.tkol.game.characters.monsters.Monsters;
import com.tkol.game.map.MapManager;

public class CharacterController {

    private final MovementManager movementManager;
    private final MapManager mapManager;
    private final CharactersManager charactersManager;

    public CharacterController(CharactersManager charactersManager, MapManager mapManager, MovementManager movementManager) {
        this.charactersManager = charactersManager;
        this.mapManager = mapManager;
        this.movementManager = movementManager;
    }

    private void handleHeroInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movementManager.moveLeft(charactersManager.getCurrentHeroes());
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movementManager.moveRight(charactersManager.getCurrentHeroes());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movementManager.moveUp(charactersManager.getCurrentHeroes());
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movementManager.moveDown(charactersManager.getCurrentHeroes());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (!charactersManager.getCurrentHeroes().animationManager.isAttacking()) {
                charactersManager.getCurrentHeroes().attack(charactersManager.getMonstersList());
                charactersManager.getCurrentHeroes().animationManager.setAttackAnimationCompleted(false);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            charactersManager.interactWithNearbyPNJ();
        }
    }

    private void handleMonsterInput(Monsters monster) {
    }

    private void handleCharacterInput(Characters character) {
        if (character instanceof Heroes) {
            handleHeroInput();
        } else if (character instanceof Monsters) {
            handleMonsterInput((Monsters) character);
        }
    }

    public void update(float dt) {
        for (Characters character : charactersManager.getCharactersList()) {
            handleCharacterInput(character);
        }
    }

    public enum MovementDirection {
        BACK,
        SIDE_RIGHT,
        FACE,
        SIDE_LEFT,
    }
}
