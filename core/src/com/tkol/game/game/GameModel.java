package com.tkol.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.tkol.game.*;
import com.tkol.game.characters.AnimationManager;
import com.tkol.game.characters.CharacterController;
import com.tkol.game.characters.CharactersManager;
import com.tkol.game.characters.MovementManager;
import com.tkol.game.inventory.items.HealthPotionMedium;
import com.tkol.game.inventory.items.HealthPotionSmall;
import com.tkol.game.inventory.Inventory;
import com.tkol.game.inventory.items.Items;
import com.tkol.game.map.MapManager;

public class GameModel {
    protected AnimationManager animationManager;
    private final MovementManager movementManager;
    private final SpriteBatch batch;
    private OrthographicCamera camera;
    private CameraManager cameraManager;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final MapManager mapManager;
    private final CharacterController characterController;
    private final CharactersManager charactersManager;
    private TheKingdomOfLegends game;
    private final Inventory inventory;
    private Items items;

    public GameModel() {
        batch = new SpriteBatch();
        inventory = new Inventory();
        HealthPotionSmall smallPotion = new HealthPotionSmall();
        HealthPotionMedium mediumPotion = new HealthPotionMedium();

        inventory.addItem(smallPotion);
        inventory.addItem(mediumPotion);

        charactersManager = new CharactersManager(game, inventory);

        mapManager = new MapManager(charactersManager);
        mapManager.setGameModel(this);
        mapManager.addMap("maps/Bar.tmx");
        mapManager.setCurrentMap(0);

        animationManager = new AnimationManager();

        movementManager = new MovementManager(mapManager, charactersManager.getCurrentHeroes(), charactersManager);

        mapRenderer = new OrthogonalTiledMapRenderer(mapManager.getCurrentMap());
        characterController = new CharacterController(charactersManager, mapManager, movementManager);
    }

    public void update(float deltaTime) {
        characterController.update(deltaTime);
        charactersManager.update(deltaTime);
        movementManager.update(deltaTime);

        cameraManager.update();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        mapManager.checkTeleportationCell(charactersManager.getCurrentHeroes());

        batch.begin();
        mapManager.render(camera);
        charactersManager.render(batch);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        mapManager.dispose();
        charactersManager.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public CharacterController getCharacterController() {
        return characterController;
    }

    public CharactersManager getCharactersManager() {
        return charactersManager;
    }

    public void updateMovementManager() {
        movementManager.updateHeroes(charactersManager.getCurrentHeroes());
    }

    public void createHero(int heroIndex) {
        charactersManager.initializeHeroes(mapManager, heroIndex);
        updateMovementManager();
        cameraManager = new CameraManager(charactersManager.getCurrentHeroes(), mapManager);
        camera = cameraManager.getCamera();
    }

    public Inventory getInventory() {
        return inventory;
    }


    public int getHeroGold() {
        return charactersManager.getCurrentHeroes().getGold();
    }



}
