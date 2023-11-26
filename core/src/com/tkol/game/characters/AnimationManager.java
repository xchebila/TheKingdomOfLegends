package com.tkol.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class AnimationManager {
    private final float walkAnimationSpeed;
    private final float attackAnimationSpeed;
    private float stateTime;
    private boolean isAttacking;
    private boolean isWalking;
    private boolean attackAnimationCompleted;


    public AnimationManager() {
        this.walkAnimationSpeed = 0.5f;
        this.attackAnimationSpeed = 0.5f;
        this.stateTime = 0;
        this.isAttacking = false;
        this.isWalking = false;
    }

    public boolean isAttackAnimationCompleted() {
        return attackAnimationCompleted;
    }

    public void setAttackAnimationCompleted(boolean attackAnimationCompleted) {
        this.attackAnimationCompleted = attackAnimationCompleted;
    }

    public void startWalk() {
        if (!isWalking) {
            isWalking = true;
            stateTime = 0;
        }
    }

    public void endWalk() {
        isWalking = false;
        stateTime = 0;
    }

    public boolean isWalking(){
        return isWalking;
    }

    public void startAttack() {
        if (!isAttacking && !attackAnimationCompleted) {
            isAttacking = true;
            stateTime = 0;
        }
    }

    public void endAttack() {
        isAttacking = false;
        attackAnimationCompleted = false;
        stateTime = 0;
    }


    public boolean isAttacking() {
        return isAttacking;
    }

    public int getCurrentFrame(boolean isUserInput) {
        int framesForMovement = 3;

        if (isAttacking) {
            int framesForAttack = 3;
            int attackFrameOffset = 3;
            int attackFrame = (int) ((stateTime) / attackAnimationSpeed) % framesForAttack + 1;
            return attackFrame + attackFrameOffset;
        } else {
            int frame = (int) ((stateTime / 5) / walkAnimationSpeed) % framesForMovement + 1;

            return (isUserInput) ? ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
                    Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) ?
                    frame : 2) : frame;
        }
    }

    public void reset() {
        isWalking = false;
        isAttacking = false;
    }

    public void update(float deltaTime) {
        if (isWalking || isAttacking) {
            stateTime += Gdx.graphics.getDeltaTime();
        } else {
            stateTime = 0;
        }
    }
}
