package com.tkol.game.characters.boss;

import com.tkol.game.game.BossProgressManager;
import com.tkol.game.characters.MovementManager;
import com.tkol.game.characters.CharactersManager;
import com.tkol.game.characters.monsters.Monsters;

public abstract class Boss extends Monsters {
    private int defeatCount;

    public Boss(float initialX, float initialY, int maxHP, int damage, int gold, MovementManager movementManager, CharactersManager charactersManager) {
        super(initialX, initialY, maxHP, damage, gold, movementManager, charactersManager);
        this.defeatCount = 0;
    }

    @Override
    protected void initTexture() {
    }

    @Override
    public int getMaxHP() {
        float multiplier = 1.5f + 0.2f * defeatCount; // Par exemple, ajustez de 1.5 + 0.2 pour chaque défaite
        return (int) (super.getMaxHP() * multiplier);
    }

    public int getDefeatCount() {
        return defeatCount;
    }

    public void incrementDefeatCount() {
        defeatCount++;
    }

    @Override
    protected void handleDeath() {
        super.handleDeath();
        BossProgressManager.incrementDefeatCount();
        System.out.println("Boss defeated. Defeat count: " + BossProgressManager.getDefeatCount());
    }

    public void resetBoss() {
        int initialMaxHP = getMaxHP();
        int defeatCount = BossProgressManager.getDefeatCount();
        System.out.println("Resetting boss. Defeat count: " + defeatCount);
        float multiplier = 1.5f + 0.2f * defeatCount;

        int newMaxHP = (int) (initialMaxHP * multiplier);

        setMaxHP(newMaxHP);
        setDamage((int) (getDamage() * multiplier));
        setCurrentHP(newMaxHP);
    }




    public boolean isDefeated() {
        return getCurrentHP() <= 0;
    }



}

