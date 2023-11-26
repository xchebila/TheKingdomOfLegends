package com.tkol.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.tkol.game.game.GameController;
import com.tkol.game.game.GameModel;
import com.tkol.game.hud.HUD;
import com.tkol.game.screen.GameOverScreen;
import com.tkol.game.screen.GameWinScreen;
import com.tkol.game.screen.MainMenuScreen;
import com.tkol.game.screen.ScreenManager;

public class TheKingdomOfLegends extends ApplicationAdapter {
    private GameModel model;
    private GameController controller;
    private ScreenManager screenManager;
    private Music backgroundMusic;
    private HUD hud;

    @Override
    public void create() {
        model = new GameModel();
        controller = new GameController(model);
        screenManager = new ScreenManager();
        screenManager.setScreen(new MainMenuScreen(this));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/Requiem.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();
    }

    public void showGameOverScreen() {
        screenManager.setScreen(new GameOverScreen(this));
    }

    public void showGameWinScreen() {
        screenManager.setScreen(new GameWinScreen(this));
    }


    @Override
    public void render() {
        screenManager.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height) {
        screenManager.resize(width, height);
    }

    @Override
    public void pause() {
        screenManager.pause();
    }

    @Override
    public void resume() {
        screenManager.resume();
    }

    @Override
    public void dispose() {
        model.dispose();
        screenManager.dispose();
        backgroundMusic.dispose();
    }

    public GameModel getModel() {
        return model;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

}
