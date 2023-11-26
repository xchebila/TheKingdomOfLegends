package com.tkol.game.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tkol.game.characters.heroes.Heroes;

public class HUD {
    private final BitmapFont font;
    private final Heroes heroes;
    private float timer;

    public HUD(Heroes heroes) {
        this.heroes = heroes;
        this.font = new BitmapFont();
        this.font.getData().setScale(0.5f);
        this.timer = 0.0f;
    }

    private String formatTime(float time) {
        int hours = (int) (time / 3600);
        int minutes = (int) ((time % 3600) / 60);
        int seconds = (int) (time % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void draw(SpriteBatch batch, OrthographicCamera camera) {
        batch.begin();
        font.setColor(Color.WHITE);

        float cameraX = camera.position.x - camera.viewportWidth / 2;
        float cameraY = camera.position.y + camera.viewportHeight / 2;

        font.draw(batch, "Current Time : " + formatTime(timer), cameraX + camera.viewportWidth - 80, cameraY - 10);

        font.draw(batch, "Level: " + heroes.getLevel(), cameraX + 10, cameraY - 10);
        font.draw(batch, "PV: " + heroes.getCurrentHP() + "/" + heroes.getMaxHP(), cameraX + 10, cameraY - 30);
        font.draw(batch, "Kill: " + heroes.getKillCount(), cameraX + 10, cameraY - 50);
        font.draw(batch, "Gold: " + heroes.getGold(), cameraX + 10, cameraY - 70);

        batch.end();
    }

    public void update(float deltaTime) {
        timer += deltaTime;
    }
}
