package com.tkol.game.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelUpText {
    private static final float LIFE_TIME = 2.0f;
    private float x;
    private float y;
    private final String text;
    private final BitmapFont font;
    private float elapsedTime;

    public LevelUpText(float x, float y) {
        this.x = x;
        this.y = y;
        this.text = "Level Up!!";

        this.font = new BitmapFont();
        this.font.getData().setScale(1.0f);

        this.elapsedTime = 0;
    }

    public void update(float deltaTime) {
        elapsedTime += deltaTime;
    }

    public boolean isExpired() {
        return elapsedTime >= LIFE_TIME;
    }

    public void draw(SpriteBatch batch) {
        Color color = Color.GOLD;
        font.setColor(color);
        font.draw(batch, text, x, y + 20);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
