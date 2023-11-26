package com.tkol.game.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.tkol.game.map.MapManager;
import com.tkol.game.characters.heroes.Heroes;

public class CameraManager {
    private OrthographicCamera camera;
    private final Heroes target;
    private final MapManager mapManager;
    private final float zoom = 1f;

    public CameraManager(Heroes target, MapManager mapManager) {
        this.target = target;
        this.mapManager = mapManager;

        float viewportWidth = (mapManager.getMapWidthInPixels() * zoom);
        float viewportHeight = mapManager.getMapHeightInPixels() * zoom;

        camera = new OrthographicCamera(viewportWidth, viewportHeight);
        update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera newCamera) {
        this.camera = newCamera;
    }

    public void update() {
        if (target != null) {
            float cameraX = target.getX() + target.getFrameWidth() / 2;
            float cameraY = target.getY() + target.getFrameHeight() / 2f;

            float cameraHalfViewportWidth = camera.viewportWidth * 0.5f;
            float cameraHalfViewportHeight = camera.viewportHeight * 0.5f;

            if (cameraX - cameraHalfViewportWidth < 0) {
                cameraX = cameraHalfViewportWidth;
            }
            else if (cameraX + cameraHalfViewportWidth > mapManager.getMapWidthInPixels()) {
                cameraX = mapManager.getMapWidthInPixels() - cameraHalfViewportWidth;
            }

            if (cameraY - cameraHalfViewportHeight < 0) {
                cameraY = cameraHalfViewportHeight;
            }
            else if (cameraY + cameraHalfViewportHeight > mapManager.getMapHeightInPixels()) {
                cameraY = mapManager.getMapHeightInPixels() - cameraHalfViewportHeight;
            }

            camera.position.set(cameraX, cameraY, 0);
            camera.update();
        }
    }
}
