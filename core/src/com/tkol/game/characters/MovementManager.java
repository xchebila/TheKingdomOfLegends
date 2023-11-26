package com.tkol.game.characters;

import com.tkol.game.map.MapManager;
import com.tkol.game.characters.monsters.Monsters;
import com.tkol.game.characters.heroes.Heroes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MovementManager {

    private final float horizontalSpeed = 0.5f;
    private final float verticalSpeed = 0.66f;
    private final MapManager mapManager;
    private Characters characters;
    private final Random random = new Random();
    private final CharactersManager charactersManager;

    public MovementManager(MapManager mapManager, Characters characters, CharactersManager charactersManager) {
        this.mapManager = mapManager;
        this.characters = characters;
        this.charactersManager = charactersManager;
    }

    public void updateHeroes(Characters newHeroes) {
        this.characters = newHeroes;
    }

    public float getHorizontalSpeed() {
        return horizontalSpeed;
    }

    public float getVerticalSpeed() {
        return verticalSpeed;
    }

    public void moveLeft(Characters character) {
        float newX = character.getX() - horizontalSpeed;
        if (mapManager.collidesWithMapBounds(newX, character.getY()) && !collidesLeft(newX) && collidesWithOtherCharacters(newX, character.getY())) {
            character.setX(newX);
            character.setCurrentDirection(CharacterController.MovementDirection.SIDE_LEFT);
            character.startWalk();
        }
    }

    public void moveRight(Characters character) {
        float newX = character.getX() + horizontalSpeed;
        if (mapManager.collidesWithMapBounds(newX, character.getY()) && !collidesRight(newX) && collidesWithOtherCharacters(newX, character.getY())) {
            character.setX(newX);
            character.setCurrentDirection(CharacterController.MovementDirection.SIDE_RIGHT);
            character.startWalk();
        }
    }

    public void moveUp(Characters character) {
        float newY = character.getY() + verticalSpeed;
        if (mapManager.collidesWithMapBounds(character.getX(), newY) && !collidesTop(newY) && collidesWithOtherCharacters(character.getX(), newY)) {
            character.setY(newY);
            character.setCurrentDirection(CharacterController.MovementDirection.BACK);
            character.startWalk();
        }
    }

    public void moveDown(Characters character) {
        float newY = character.getY() - verticalSpeed;
        if (mapManager.collidesWithMapBounds(character.getX(), newY) && !collidesBottom(newY) && collidesWithOtherCharacters(character.getX(), newY)) {
            character.setY(newY);
            character.setCurrentDirection(CharacterController.MovementDirection.FACE);
            character.startWalk();
        }
    }


    public boolean collidesRight(float newX) {
        for (float step = 0; step < characters.getFrameHeight(); step += mapManager.getTileHeight() / 2f) {
            if (mapManager.isCellBlocked(newX + characters.getFrameWidth(), characters.getY() + step)) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesLeft(float newX) {
        for (float step = 0; step < characters.getFrameHeight(); step += mapManager.getTileHeight() / 2f) {
            if (mapManager.isCellBlocked(newX, characters.getY() + step)) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesTop(float newY) {
        for (float step = 0; step < characters.getFrameWidth(); step += mapManager.getTileWidth() / 2f) {
            if (mapManager.isCellBlocked(characters.getX() + step, newY + characters.getFrameHeight())) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesBottom(float newY) {
        for (float step = 0; step < characters.getFrameWidth(); step += mapManager.getTileWidth() / 2f) {
            if (mapManager.isCellBlocked(characters.getX() + step, newY)) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesWithOtherCharacters(float x, float y) {
        for (Characters otherCharacter : charactersManager.getCharactersList()) {
            if (otherCharacter != characters && collidesWithCharacter(otherCharacter, x, y)) {
                return false;
            }
        }
        return true;
    }

    public boolean collidesWithCharacter(Characters otherCharacter, float x, float y) {
        return (x < otherCharacter.getX() + otherCharacter.getFrameWidth() &&
                x + characters.getFrameWidth() > otherCharacter.getX() &&
                y < otherCharacter.getY() + otherCharacter.getFrameHeight() &&
                y + characters.getFrameHeight() > otherCharacter.getY());
    }

    public void moveRandomly(Monsters monster) {
        float maxMovement = 20f;

        boolean moveHorizontally = random.nextBoolean();
        float deltaX = moveHorizontally ? (random.nextFloat() * 2 - 1) * maxMovement : 0;
        float deltaY = moveHorizontally ? 0 : (random.nextFloat() * 2 - 1) * maxMovement;

        float newX = monster.getX();
        float newY = monster.getY();

        int numFramesToMove = 10;

        float stepX = deltaX / numFramesToMove;
        float stepY = deltaY / numFramesToMove;

        for (int i = 0; i < numFramesToMove; i++) {
            newX += stepX;
            newY += stepY;

            if (!collidesWithOtherCharacters(newX, newY) && !mapManager.isCellBlocked(newX, newY) && mapManager.collidesWithMapBounds(newX, newY)) {
                if (!collidesWithCharacter(characters, newX, newY)) {
                    monster.setX(newX);
                    monster.setY(newY);
                    monster.updateAnimation(stepX, stepY);
                    monster.startWalk();
                } else {
                    break;
                }
            } else {
                break;
            }
            monster.setMoving(true);
        }
    }

    public void attackHeroes() {
        Heroes currentHero = charactersManager.getCurrentHeroes();

        List<Monsters> monstersCopy = new ArrayList<>(charactersManager.getMonstersList());

        for (Monsters monster : monstersCopy) {
            float distanceToHero = currentHero.calculateDistance(monster.getX(), monster.getY());
            if (distanceToHero < Characters.getAttackRange() && monster.isCooldownOver()) {
                System.out.println("Le monstre attaque le héros !");
                System.out.println(currentHero.getCurrentHP());
                monster.attack(currentHero);
                monster.resetCooldown();
            }
        }
    }


    public void update(float deltaTime) {
        List<Characters> charactersListCopy = new ArrayList<>(charactersManager.getCharactersList());

        for (Characters character : charactersListCopy) {
            character.update(deltaTime);

            if (character instanceof Monsters) {
                Monsters monster = (Monsters) character;
                monster.updateCooldown(deltaTime);

                if (monster.isCooldownOver()) {
                    attackHeroes();

                    if (monster.isCooldownOver()) {
                        moveRandomly(monster);
                        monster.resetCooldown();
                    }
                }
            }
        }
    }


}



