package com.tkol.game.characters.monsters;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tkol.game.hud.DamageText;
import com.tkol.game.characters.MovementManager;
import com.tkol.game.characters.AnimationManager;
import com.tkol.game.characters.CharacterController;
import com.tkol.game.characters.Characters;
import com.tkol.game.characters.CharactersManager;

import java.util.ArrayList;
import java.util.List;


public class Monsters extends Characters<Monsters> {
    private final MovementManager movementManager;
    private final float cooldownTime = 2.0f;
    private final List<DamageText> damageTexts;
    private float currentCooldown = 0.0f;
    private boolean isMoving;

    public Monsters(float initialX, float initialY, int pv, int damage, int gold, MovementManager movementManager, CharactersManager charactersManager) {
        super(initialX, initialY, pv, damage, charactersManager);
        this.movementManager = movementManager;
        damageTexts = new ArrayList<>();
        this.animationManager = new AnimationManager();
        this.gold = gold;
    }


    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    @Override
    protected void initTexture() {
    }

    public void renderDamageTexts(SpriteBatch batch) {
        for (DamageText damageText : damageTexts) {
            damageText.draw(batch);
        }
    }

    public void updateCooldown(float deltaTime) {
        currentCooldown += deltaTime;
    }

    public boolean isCooldownOver() {
        return currentCooldown >= cooldownTime;
    }

    public void resetCooldown() {
        currentCooldown = 0.0f;
    }

    public void updateAnimation(float stepX, float stepY) {
        if (Math.abs(stepX) > Math.abs(stepY)) {
            if (stepX > 0.2) {
                setCurrentDirection(CharacterController.MovementDirection.SIDE_RIGHT);
            } else if (stepX < -0.2) {
                setCurrentDirection(CharacterController.MovementDirection.SIDE_LEFT);
            }
        } else {
            if (stepY > 0.2) {
                setCurrentDirection(CharacterController.MovementDirection.BACK);
            } else if (stepY < -0.2) {
                setCurrentDirection(CharacterController.MovementDirection.FACE);
            }
        }

        startWalk();
    }

    public float getHeadX() {
        return getX() + getFrameWidth() / 2;
    }

    public float getHeadY() {
        return getY() + getFrameHeight();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentFrame = animationManager.getCurrentFrame(false);

        if (animationManager.isWalking()) {
            animationManager.update(deltaTime);

            if (animationManager.isAttacking() && currentFrame == 5) {
                animationManager.endAttack();
            }
        }
    }


    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        renderDamageTexts(batch);
    }

}