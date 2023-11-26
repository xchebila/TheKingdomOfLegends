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

public class MainMenuScreen implements Screen {
    private final TheKingdomOfLegends game;
    private final SpriteBatch batch;
    private final Texture background;
    private final String[] menuOptions;
    private final BitmapFont font;
    private int currentSelection;

    public MainMenuScreen(TheKingdomOfLegends game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.background = new Texture("game/backgroundMenu.png");
        this.menuOptions = new String[]{"Jouer", "Règles & buts du jeu", "Quitter"};
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

        for (int i = 0; i < menuOptions.length; i++) {
            if (i == currentSelection) {
                font.getData().setScale(1.5f);
                font.setColor(Color.YELLOW);
            }

            float textWidth = font.draw(batch, menuOptions[i], 0, 0).width;
            float textHeight = font.draw(batch, menuOptions[i], 0, 0).height;

            float textX = (Gdx.graphics.getWidth() - textWidth) / 2;
            float textY = (Gdx.graphics.getHeight() + textHeight) / 2 - i * 50;

            batch.setColor(0, 0, 0, 0.7f);
            batch.draw(background, textX - 20, textY - textHeight, textWidth + 40, textHeight + 10);
            batch.setColor(Color.WHITE);

            font.draw(batch, menuOptions[i], textX, textY);

            font.getData().setScale(1.0f);
            font.setColor(Color.WHITE);
        }

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && currentSelection > 0) {
            currentSelection = (currentSelection - 1) % menuOptions.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && currentSelection < menuOptions.length - 1) {
            currentSelection = (currentSelection + 1) % menuOptions.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (currentSelection == 0) {
                game.getScreenManager().setScreen(new ChoiceHeroes(game, game.getModel()));
            }
            if (currentSelection == 1) {
                game.getScreenManager().setScreen(new RuleAndGoal(game));

            } else if (currentSelection == 2) {
                Gdx.app.exit();
            }
        }
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
    }
}
