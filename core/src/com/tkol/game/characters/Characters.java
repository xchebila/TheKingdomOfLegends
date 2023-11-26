package com.tkol.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.hud.DamageText;
import com.tkol.game.characters.heroes.Heroes;
import com.tkol.game.characters.monsters.Monsters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Characters<T extends Characters<?>> {
    private static final long ATTACK_COOLDOWN = 200;
    private static final float ATTACK_RANGE = 35;
    private static final float SPAWN_RANGE = 50;

    protected final int frameWidth = 24;
    protected float x;
    protected float y;
    protected int frameHeight = 32;
    protected int currentFrame = 1;
    protected TextureRegion characterRegion;
    protected CharacterController.MovementDirection currentDirection = CharacterController.MovementDirection.FACE;
    protected AnimationManager animationManager;
    protected int maxHP;
    protected int currentHP;
    protected int damage;
    protected int gold;
    private long lastAttackTime;
    private final List<DamageText> damageTexts;
    private boolean markedForRemoval = false;
    private boolean markedForChangeMap = false;
    private final CharactersManager charactersManager;

    public Characters(float initialX, float initialY, int maxHP, int damage, CharactersManager charactersManager) {
        this.x = initialX;
        this.y = initialY;
        this.maxHP = maxHP;
        this.currentHP = maxHP;
        this.damage = damage;
        this.gold = gold;
        this.damageTexts = new ArrayList<>();
        this.animationManager = new AnimationManager();
        this.charactersManager = charactersManager;
        initTexture();
    }

    public static float getAttackRange() {
        return ATTACK_RANGE;
    }

    protected abstract void initTexture();

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }
    public void setCurrentHP(int currentHP) {
        this.currentHP = Math.min(currentHP, maxHP);
        this.currentHP = Math.max(this.currentHP, 0);
    }


    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void increaseGold(int amount) {
        this.gold += amount;
    }

    public void decreaseGold(int amount) {
        this.gold -= amount;
        if (this.gold < 0) {
            this.gold = 0;
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public float getFrameWidth() {
        return frameWidth;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setCharacterRegion(TextureRegion region) {
        this.characterRegion = region;
    }

    public void setCurrentDirection(CharacterController.MovementDirection direction) {
        this.currentDirection = direction;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    protected float calculateDistance(float targetX, float targetY) {
        float deltaX = targetX - getX();
        float deltaY = targetY - getY();
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public void increaseHP(int amount) {
        currentHP += amount;
    }

    public boolean isTooCloseToHero(float x, float y) {
        float distanceToHero = calculateDistance(x, y);
        return distanceToHero < SPAWN_RANGE;
    }

    public void attack(Characters target) {
        List<Characters> targets = new ArrayList<>();
        targets.add(target);
        attack(targets, target instanceof Heroes);
    }

    public void attack(List<? extends Characters> characters) {
        attack(characters, false);
    }

    private void attack(List<? extends Characters> characters, boolean isHeroDamage) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastAttackTime >= ATTACK_COOLDOWN) {
            animationManager.startAttack();

            for (Characters character : characters) {
                float distance = calculateDistance(character.getX(), character.getY());
                if (distance <= ATTACK_RANGE && character.getCurrentHP() > 0) {
                    int damageDealt = calculateDamage();
                    character.takeDamage(damageDealt, isHeroDamage);
                    addDamageText(Integer.toString(damageDealt), character.getX(), character.getY(), isHeroDamage);
                }
            }

            lastAttackTime = currentTime;
        }
    }

    protected int calculateDamage() {
        return damage;
    }

    public void takeDamage(int damage, boolean isHeroDamage) {
        currentHP -= damage;
        addDamageText(Integer.toString(damage), getX(), getY() + getFrameHeight() + 10, isHeroDamage);

        if (currentHP <= 0) {
            handleDeath();
        }

        System.out.println("Current HP: " + currentHP);
    }


    protected void handleDeath() {
        if (this instanceof Monsters) {
            markForRemoval();
            charactersManager.getCurrentHeroes().increaseGold(getGold());
            charactersManager.getCurrentHeroes().increaseXp(10);
        }
    }

    protected void markForRemoval() {
        markedForRemoval = true;
    }

    protected void markForChangeMap() {
        markedForChangeMap = true;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public boolean isMarkedForChangeMap() {
        return markedForChangeMap;
    }

    public void renderDamageTexts(SpriteBatch batch) {
        for (int i = 0; i < damageTexts.size(); i++) {
            DamageText damageText = damageTexts.get(i);
            damageText.setY(getY() + getFrameHeight() + 10 + i * 20);
            damageText.draw(batch);
        }
    }

    public void updateDamageTexts(float deltaTime) {
        Iterator<DamageText> iterator = damageTexts.iterator();
        while (iterator.hasNext()) {
            DamageText damageText = iterator.next();
            damageText.update(deltaTime);
            if (damageText.isExpired()) {
                iterator.remove();
            }
        }
    }

    public void addDamageText(String text, float headX, float headY, boolean isHeroDamage) {
        DamageText damageText = new DamageText(headX, headY, text, isHeroDamage);
        damageTexts.add(damageText);

        if (damageTexts.size() > 3) {
            damageTexts.remove(0);
        }
    }

    public void startWalk() {
        if (!animationManager.isWalking()) {
            animationManager.startWalk();
        }
    }

    public void endWalk() {
        if (animationManager.isWalking()) {
            animationManager.endWalk();
        }
    }

    public void update(float deltaTime) {
        animationManager.update(deltaTime);

    }

    public void render(SpriteBatch batch) {
        int regionX = (currentFrame - 1) * frameWidth;
        int regionY = currentDirection.ordinal() * frameHeight;

        TextureRegion currentFrameRegion = new TextureRegion(characterRegion, regionX, regionY, frameWidth, frameHeight);

        batch.draw(currentFrameRegion, x, y);
        renderDamageTexts(batch);
        updateDamageTexts(Gdx.graphics.getDeltaTime());
    }

    public void dispose() {
        characterRegion.getTexture().dispose();
    }
}

