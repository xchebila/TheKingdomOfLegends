package com.tkol.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.tkol.game.TheKingdomOfLegends;
import com.tkol.game.characters.heroes.Heroes;
import com.tkol.game.game.GameController;
import com.tkol.game.game.GameModel;

public class GameScreen implements Screen {
    private final TheKingdomOfLegends game;
    private final GameModel model;
    private final GameController controller;
    private boolean gameOverDisplayed;
    private boolean inventoryDisplayed;
    private boolean inventoryFullDisplayed;
    private boolean gameWinDisplayed;
    private final boolean levelUpDisplayed;

    public GameScreen(TheKingdomOfLegends game, GameModel model) {
        this.game = game;
        this.model = model;
        this.controller = new GameController(model);
        this.gameOverDisplayed = false;
        this.inventoryDisplayed = false;
        this.gameWinDisplayed = false;
        this.levelUpDisplayed = false;
    }

    @Override
    public void show() {
        gameOverDisplayed = false;
        inventoryDisplayed = false;
        gameWinDisplayed = false;
    }

    @Override
    public void render(float delta) {
        controller.update(delta);
        controller.render(delta);

        Heroes currentHero = model.getCharactersManager().getCurrentHeroes();

        if (currentHero.getCurrentHP() <= 0 && !gameOverDisplayed) {
            game.showGameOverScreen();
            gameOverDisplayed = true;
        }


        if (game.getModel().getCharactersManager().isWin()) {
            game.showGameWinScreen();
            gameWinDisplayed = true;
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            inventoryDisplayed = !inventoryDisplayed;
            inventoryFullDisplayed = false;
        }

        if (inventoryDisplayed) {
            model.getInventory().render(delta);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            inventoryFullDisplayed = !inventoryFullDisplayed;
            inventoryDisplayed = false;
        }

    }


    @Override
    public void resize(int width, int height) {

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
        controller.dispose();
    }
}