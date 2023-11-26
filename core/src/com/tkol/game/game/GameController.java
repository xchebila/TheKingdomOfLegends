package com.tkol.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.tkol.game.hud.HUD;

public class GameController {
    private final GameModel model;
    private final HUD hud;

    public GameController(GameModel model) {
        this.model = model;
        hud = new HUD(model.getCharactersManager().getCurrentHeroes());
    }

    public void update(float deltaTime) {
        model.update(deltaTime);
    }


    public void render(float deltaTime) {

        model.update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        model.getCamera().update();
        model.getBatch().setProjectionMatrix(model.getCamera().combined);

        model.getBatch().begin();
        model.getMapManager().render(model.getCamera());

        model.getCharactersManager().render(model.getBatch());
        model.getBatch().end();
        hud.update(deltaTime);
        hud.draw(model.getBatch(), model.getCamera());

    }

    public void dispose() {
        model.dispose();
    }
}
