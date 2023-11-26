package com.tkol.game.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DamageText {
    private static final float LIFE_TIME = 1.5f;
    private float x;
    private float y;
    private final String text;
    private final BitmapFont font;
    private float elapsedTime;
    private final boolean isHeroDamage;

    public DamageText(float x, float y, String text, boolean isHeroDamage) {
        this.x = x;
        this.y = y;
        this.text = text;

        this.font = new BitmapFont();
        this.font.getData().setScale(0.5f);

        this.elapsedTime = 0;
        this.isHeroDamage = isHeroDamage;
    }

    public void update(float deltaTime) {
        elapsedTime += deltaTime;
    }

    public boolean isExpired() {
        return elapsedTime >= LIFE_TIME;
    }

    public void draw(SpriteBatch batch) {
        Color color = isHeroDamage ? Color.RED : Color.WHITE;
        font.setColor(color);
        font.draw(batch, text, x, y);

    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
