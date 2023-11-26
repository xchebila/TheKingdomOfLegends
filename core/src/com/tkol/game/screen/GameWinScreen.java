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
import com.tkol.game.characters.CharacterController;
import com.tkol.game.screen.GameScreen;

public class GameWinScreen implements Screen {
    private final TheKingdomOfLegends game;
    private final SpriteBatch batch;
    private final Texture background;
    private final String[] menuOptions;
    private final BitmapFont font;
    private int currentSelection;

    public GameWinScreen(TheKingdomOfLegends game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.background = new Texture("game/backgroundGameWin.png");
        this.menuOptions = new String[]{"New Game +", "Exit"};
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

            font.draw(batch, menuOptions[i], 100, 200 - i * 50);

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
                game.getModel().getCharactersManager().newGame(40, 130);
                game.getModel().getCharactersManager().getCurrentHeroes().setCurrentDirection(CharacterController.MovementDirection.SIDE_LEFT);
                game.getScreenManager().setScreen(new GameScreen(game, game.getModel()));
            } else if (currentSelection == 1) {
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
