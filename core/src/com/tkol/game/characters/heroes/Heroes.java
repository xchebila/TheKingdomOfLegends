package com.tkol.game.characters.heroes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.hud.LevelUpText;
import com.tkol.game.characters.AnimationManager;
import com.tkol.game.characters.Characters;
import com.tkol.game.characters.CharactersManager;

public class Heroes extends Characters<Heroes> {
    private int killCount;
    private String currentZone;
    private int xp;
    private int level;
    private LevelUpText levelUpText;

    public Heroes(float initialX, float initialY, int pv, int damage, CharactersManager charactersManager) {
        super(initialX, initialY, pv, damage, charactersManager);
        this.killCount = 0;
        this.animationManager = new AnimationManager();
        this.xp = 0;
        this.level = 1;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void increaseXp(int amount) {
        xp += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int xpThreshold = level * 100;
        float hpMultiplier = 1.2f;

        if (xp >= xpThreshold) {
            level++;
            xp = 0;
            damage += 2 * level;
            maxHP = (int) (maxHP * hpMultiplier);
            levelUpText = new LevelUpText(getX(), getY());
        }
    }

    public void resetStats(){
        xp = 0;
        level = 1;
        killCount = 0;
        gold = 0;
        currentHP = maxHP;
    }



    public String getCurrentZone() {
        return currentZone;
    }


    public void setCurrentZone(String currentZone) {
        this.currentZone = currentZone;
    }

    public int getKillCount() {
        return killCount;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    public void incrementKillCount() {
        killCount++;
    }


    @Override
    protected void initTexture() {
    }

    @Override
    public void takeDamage(int damage, boolean isHeroDamage) {
        super.takeDamage(damage, isHeroDamage);
    }

    public void renderLevelUpText(SpriteBatch batch) {
        if (levelUpText != null && !levelUpText.isExpired()) {
            levelUpText.draw(batch);
        }
    }

    public void updateLevelUpText(float deltaTime) {
        if (levelUpText != null) {
            levelUpText.update(deltaTime);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        int regionX = (currentFrame - 1) * frameWidth;
        int regionY = currentDirection.ordinal() * frameHeight;

        TextureRegion currentFrameRegion = new TextureRegion(characterRegion, regionX, regionY, frameWidth, frameHeight);

        batch.draw(currentFrameRegion, x, y);
        renderDamageTexts(batch);
        renderLevelUpText(batch);
        updateDamageTexts(Gdx.graphics.getDeltaTime());
        updateLevelUpText(Gdx.graphics.getDeltaTime());
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentFrame = animationManager.getCurrentFrame(true);

        if (animationManager.isWalking()) {
            animationManager.update(deltaTime);

            if (animationManager.isAttacking() && currentFrame == 5) {
                animationManager.endAttack();
            }
        }
    }


}