package com.tkol.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tkol.game.TheKingdomOfLegends;
import com.tkol.game.game.GameModel;
import com.tkol.game.screen.GameScreen;

public class ChoiceHeroes implements Screen {
    private final TheKingdomOfLegends game;
    private final SpriteBatch batch;
    private final Texture background;
    private final Texture[] heroTextures;
    private final BitmapFont font;
    private int currentSelection;

    public ChoiceHeroes(TheKingdomOfLegends game, GameModel model) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.background = new Texture("game/backgroundHeroes.jpg");
        this.heroTextures = new Texture[]{
                new Texture("heroes/hero1.png"),
                new Texture("heroes/hero2.png"),
                new Texture("heroes/hero3.png")
        };
        this.currentSelection = 0;
        this.font = new BitmapFont();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (int i = 0; i < heroTextures.length; i++) {
            float scale = (i == currentSelection) ? 0.7f : 0.5f;

            if (i == currentSelection) {
                font.getData().setScale(1.5f);
                font.setColor(Color.YELLOW);
            }

            batch.draw(heroTextures[i], calculateHeroXPosition(i), calculateHeroYPosition(i),
                    heroTextures[i].getWidth() * scale, heroTextures[i].getHeight() * scale);

            font.getData().setScale(1.0f);
            font.setColor(Color.WHITE);
        }

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && currentSelection > 0) {
            currentSelection--;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && currentSelection < heroTextures.length - 1) {
            currentSelection++;

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.getModel().createHero(currentSelection);
            game.getModel().getInventory().setCurrentHero(game.getModel().getCharactersManager().getCurrentHeroes());
            game.getScreenManager().setScreen(new GameScreen(game, game.getModel()));
        }
    }


    public int getCurrentSelection() {
        return currentSelection;
    }

    private float calculateHeroXPosition(int index) {
        return (Gdx.graphics.getWidth() / (heroTextures.length + 1)) * (index + 1) - heroTextures[index].getWidth() / 2;
    }

    private float calculateHeroYPosition(int index) {
        return (Gdx.graphics.getHeight() - heroTextures[index].getHeight()) / 2;
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        background.dispose();
        for (Texture texture : heroTextures) {
            texture.dispose();
        }
    }
}
