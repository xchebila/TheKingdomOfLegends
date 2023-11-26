package com.tkol.game.characters;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tkol.game.*;
import com.tkol.game.characters.boss.BossVillage;
import com.tkol.game.characters.heroes.Archer;
import com.tkol.game.characters.heroes.Barbarian;
import com.tkol.game.characters.heroes.Heroes;
import com.tkol.game.characters.heroes.Mage;
import com.tkol.game.characters.monsters.Goblins;
import com.tkol.game.characters.monsters.Monsters;
import com.tkol.game.characters.monsters.Spider;
import com.tkol.game.characters.monsters.Zombie;
import com.tkol.game.characters.pnj.PNJ;
import com.tkol.game.characters.pnj.Trader;
import com.tkol.game.game.BossProgressManager;
import com.tkol.game.inventory.Inventory;
import com.tkol.game.map.MapManager;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class CharactersManager {
    private final List<Characters> charactersList;
    private final List<Heroes> heroesList;
    private final List<Monsters> monstersList;
    private final List<Class<? extends Monsters>> monsterTypes;
    private final List<PNJ> pnjList;
    private MovementManager movementManager;
    private MapManager mapManager;
    private Heroes currentHeroes;
    private final TheKingdomOfLegends game;
    private final Inventory inventory;
    private BossVillage bossVillage;
    private int totalKillsForWin = 50;

    public CharactersManager(TheKingdomOfLegends game, Inventory inventory) {
        this.game = game;
        this.inventory = inventory;
        charactersList = new ArrayList<>();
        heroesList = new ArrayList<>();
        monstersList = new ArrayList<>();
        monsterTypes = new ArrayList<>();
        monsterTypes.add(Spider.class);
        monsterTypes.add(Zombie.class);
        monsterTypes.add(Goblins.class);
        pnjList = new ArrayList<>();
    }

    public TheKingdomOfLegends getGame() {
        return game;
    }

    public List<Characters> getCharactersList() {
        return charactersList;
    }

    public List<Monsters> getMonstersList() {
        return monstersList;
    }

    public List<PNJ> getPnjList() {
        return pnjList;
    }

    public Heroes getCurrentHeroes() {
        return currentHeroes;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getKillCount() {
        return currentHeroes.getKillCount();
    }

    public void interactWithNearbyPNJ() {
        for (PNJ pnj : getPnjList()) {
            pnj.interact(currentHeroes, inventory);
        }
    }

    public void initializeHeroes(MapManager mapManager, int heroIndex) {
        this.mapManager = mapManager;
        switch (heroIndex) {
            case 0:
                currentHeroes = new Archer(48, 130, this);
                currentHeroes.setCurrentDirection(CharacterController.MovementDirection.SIDE_LEFT);
                break;
            case 1:
                currentHeroes = new Barbarian(48, 130, this);
                currentHeroes.setCurrentDirection(CharacterController.MovementDirection.SIDE_LEFT);
                break;
            case 2:
                currentHeroes = new Mage(48, 130, this);
                currentHeroes.setCurrentDirection(CharacterController.MovementDirection.SIDE_LEFT);

                break;
            default:
                break;
        }

        addCharacter(currentHeroes);
    }
    private void spawnBossVillage() {
        Random random = new Random();
        float bossX, bossY;
        float margin = 5f;

        do {
            bossX = random.nextFloat() * (mapManager.getMapWidthInPixels() - 2 * margin) + margin;
            bossY = random.nextFloat() * (mapManager.getMapHeightInPixels() - 2 * margin) + margin;
        } while (mapManager.isCellBlocked(bossX, bossY) || !mapManager.collidesWithMapBounds(bossX, bossY) || currentHeroes.isTooCloseToHero(bossX, bossY));

        bossVillage = new BossVillage(bossX, bossY, movementManager, this);
        addCharacter(bossVillage);
    }



    public void initializeMonsters() {
        Random random = new Random();

        for (Monsters monster : monstersList) {
            monster.dispose();
            monster.markForChangeMap();
        }
        removeMarkedCharacters();
        removeForChangeMap();

        monstersList.clear();

        if (mapManager.isFightingZone()) {
            if (mapManager.getCurrentMapName().equals("MonsterZone4")) {
                spawnBossVillage();
            } else {
                int numberOfMonsters = random.nextInt(14) + 7;

                for (int i = 0; i < numberOfMonsters; i++) {
                    float monsterX, monsterY;
                    float margin = 5f;

                    do {
                        monsterX = random.nextFloat() * (mapManager.getMapWidthInPixels() - 2 * margin) + margin;
                        monsterY = random.nextFloat() * (mapManager.getMapHeightInPixels() - 2 * margin) + margin;
                    } while (mapManager.isCellBlocked(monsterX, monsterY) || !mapManager.collidesWithMapBounds(monsterX, monsterY) || currentHeroes.isTooCloseToHero(monsterX, monsterY));

                    Class<? extends Monsters> monsterType = getRandomMonsterType();
                    Monsters monster = createMonsterInstance(monsterType, monsterX, monsterY);
                    addCharacter(monster);
                }
            }
        }
    }


    private Class<? extends Monsters> getRandomMonsterType() {
        Random random = new Random();
        return monsterTypes.get(random.nextInt(monsterTypes.size()));
    }

    private Monsters createMonsterInstance(Class<? extends Monsters> monsterType, float x, float y) {
        try {
            Constructor<? extends Monsters> constructor = monsterType.getConstructor(float.class, float.class, MovementManager.class, CharactersManager.class);
            return constructor.newInstance(x, y, movementManager, this);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création d'une instance de monstre");
        }
    }


    public void initializePNJ() {
        for (PNJ pnj : pnjList) {
            pnj.dispose();
            pnj.markForChangeMap();
        }
        removeForChangeMap();

        if (mapManager.getCurrentMapName().equals("TraderRoom")) {
            initializeGenericPNJ(new Trader(230, 90, this));
        }

    }


    private void initializeGenericPNJ(PNJ pnj) {
        pnjList.add(pnj);
        addCharacter(pnj);
    }


    public void removeMarkedCharacters() {
        Iterator<Characters> iterator = charactersList.iterator();
        while (iterator.hasNext()) {
            Characters character = iterator.next();
            if (character.isMarkedForRemoval()) {
                iterator.remove();
                currentHeroes.incrementKillCount();
            }
        }
    }

    public void removeForChangeMap() {
        Iterator<Characters> iterator = charactersList.iterator();
        while (iterator.hasNext()) {
            Characters character = iterator.next();
            if (character.isMarkedForChangeMap()) {
                iterator.remove();
            }
        }
    }

    public void addCharacter(Characters character) {
        charactersList.add(character);

        if (character instanceof Heroes) {
            heroesList.add((Heroes) character);
        }

        if (character instanceof Monsters) {
            monstersList.add((Monsters) character);
        }
        if (character instanceof PNJ) {
            charactersList.add((PNJ) character);
        }
    }

    public void resetPosition(float x, float y) {
        mapManager.changeMap("Bar");
        currentHeroes.setX(x);
        currentHeroes.setY(y);
        currentHeroes.resetStats();
        BossProgressManager.resetDefeatCount();
        if (bossVillage != null) {
            bossVillage.resetBoss();
        }
    }

    private boolean isBossVillageDefeated() {
        return bossVillage != null && bossVillage.isDefeated();
    }

    public boolean isWin() {
        return currentHeroes.getKillCount() >= totalKillsForWin || isBossVillageDefeated();
    }

    public void updateTotalKillsForWin() {
        totalKillsForWin *= 2;
    }

    public void newGame(float x, float y) {
        currentHeroes.setCurrentHP(currentHeroes.getMaxHP());
        mapManager.changeMap("Bar");
        currentHeroes.setX(x);
        currentHeroes.setY(y);
        updateTotalKillsForWin();
        if (bossVillage != null) {
            bossVillage.resetBoss();
        }
    }

    public void restoreHealthInHealingZone() {
        if (mapManager.isHealingZone()) {
            currentHeroes.setCurrentHP(currentHeroes.getMaxHP());;
        }
    }

    public void update(float deltaTime) {
        for (Heroes heroes : heroesList) {
            heroes.update(deltaTime);
            restoreHealthInHealingZone();
        }

        removeMarkedCharacters();
    }

    public void render(SpriteBatch batch) {
        for (Characters characters : charactersList) {
            characters.render(batch);

            if (characters.isMarkedForRemoval()) {
                characters.dispose();
            }
        }
    }


    public void dispose() {
    }

}