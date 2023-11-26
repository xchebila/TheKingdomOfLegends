package com.tkol.game.screen;

import com.badlogic.gdx.Screen;

public class ScreenManager {
    private Screen currentScreen;

    public void setScreen(Screen screen) {

        currentScreen = screen;
        currentScreen.show();
    }

    public void render(float delta) {
        currentScreen.render(delta);
    }

    public void resize(int width, int height) {
        currentScreen.resize(width, height);
    }

    public void pause() {
        currentScreen.pause();
    }

    public void resume() {
        currentScreen.resume();
    }

    public void dispose() {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}

