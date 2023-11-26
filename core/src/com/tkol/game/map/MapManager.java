package com.tkol.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.tkol.game.characters.CharactersManager;
import com.tkol.game.characters.heroes.Heroes;
import com.tkol.game.game.GameModel;

import java.util.ArrayList;
import java.util.List;

public class MapManager {
    private static final long TELEPORT_DELAY = 1000;
    private final List<TiledMap> mapList;
    private final List<String> mapPaths;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer collisionLayer;
    private TiledMapTileLayer teleportationLayer;
    private int currentMapIndex;
    private final CharactersManager charactersManager;
    private String currentMapName;
    private GameModel model;
    private List<Cell> currentTeleportationCells;
    private long lastTeleportTime;
    private boolean canTeleport = true;


    public MapManager(CharactersManager charactersManager) {
        mapList = new ArrayList<>();
        mapPaths = new ArrayList<>();
        this.charactersManager = charactersManager;

    }

    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public TiledMapTileLayer getTeleportationLayer() {
        return teleportationLayer;
    }

    public int getMapWidthInTiles() {
        return collisionLayer.getWidth();
    }

    public int getMapHeightInTiles() {
        return collisionLayer.getHeight();
    }

    public int getTileWidth() {
        return collisionLayer.getTileWidth();
    }

    public int getTileHeight() {
        return collisionLayer.getTileHeight();
    }

    public int getMapWidthInPixels() {
        return getMapWidthInTiles() * getTileWidth();
    }

    public int getMapHeightInPixels() {
        return getMapHeightInTiles() * getTileHeight();
    }

    public Cell getCollisionCell(int x, int y) {
        return collisionLayer.getCell(x, y);
    }

    public Cell getTeleportationCell(int x, int y) {
        return teleportationLayer.getCell(x + 1, y + 1);
    }

    public void addMap(String mapFilePath) {
        if (!mapPaths.contains(mapFilePath)) {
            TiledMap map = new TmxMapLoader().load(mapFilePath);
            mapList.add(map);
            mapPaths.add(mapFilePath);
            if (currentMapIndex == -1) {
                setCurrentMap(mapPaths.indexOf(mapFilePath));
            }
        }
    }

    private void setupLayers(TiledMap map) {
        collisionLayer = (TiledMapTileLayer) map.getLayers().get("Collision");
        teleportationLayer = (TiledMapTileLayer) map.getLayers().get("Teleportation");

        currentTeleportationCells = new ArrayList<>();
        for (int x = 0; x < teleportationLayer.getWidth(); x++) {
            for (int y = 0; y < teleportationLayer.getHeight(); y++) {
                Cell cell = teleportationLayer.getCell(x, y);
                if (cell != null && cell.getTile() != null) {
                    currentTeleportationCells.add(cell);
                }
            }
        }
    }

    public String getCurrentMapName() {
        return currentMapName;
    }

    public TiledMap getCurrentMap() {
        if (!mapList.isEmpty()) {
            return mapList.get(currentMapIndex);
        } else {
            throw new IllegalStateException("Aucune carte définie");
        }
    }

    public void setCurrentMap(int index) {
        if (index >= 0 && index < mapList.size()) {
            currentMapIndex = index;
            FileHandle mapFile = Gdx.files.internal(mapPaths.get(index));
            currentMapName = mapFile.nameWithoutExtension();
            TiledMap currentMap = mapList.get(currentMapIndex);
            if (mapRenderer == null) {
                mapRenderer = new OrthogonalTiledMapRenderer(currentMap);
            } else {
                mapRenderer.setMap(currentMap);
            }
            setupLayers(currentMap);
        } else {
            System.out.println("Invalid map index: " + index);
            throw new IllegalArgumentException("Index de carte non valide");
        }
    }

    public boolean isCellBlocked(float x, float y) {
        TiledMapTileLayer.Cell cell = getCollisionCell((int) (x / getTileWidth()), (int) (y / getTileHeight()));
        return cell != null;
    }

    public boolean collidesWithMapBounds(float newX, float newY) {
        return !(newX < 0) && !(newX + charactersManager.getCurrentHeroes().getFrameWidth() > getMapWidthInPixels()) &&
                !(newY < 0) && !(newY + charactersManager.getCurrentHeroes().getFrameHeight() > getMapHeightInPixels());
    }

    public void checkTeleportationCell(Heroes heroes) {
        resetTeleportState();

        int characterTileX = (int) (heroes.getX() / getTileWidth());
        int characterTileY = (int) (heroes.getY() / getTileHeight());

        Cell teleportationCell = getTeleportationCell(characterTileX, characterTileY);

        if (teleportationCell != null && canTeleport) {
            TiledMapTile tile = teleportationCell.getTile();
            if (tile != null) {
                String destination = tile.getProperties().get("destination", String.class);
                if (destination != null) {
                    changeMap(destination);
                    lastTeleportTime = TimeUtils.millis();
                    canTeleport = false;
                }
            }
        }
    }

    private void resetTeleportState() {
        long currentTime = TimeUtils.millis();
        if (!canTeleport && currentTime - lastTeleportTime > TELEPORT_DELAY) {
            canTeleport = true;
        }
    }

    public void changeMap(String destination) {
        String newMapFilePath = "maps/" + destination + ".tmx";
        String tmpMap = currentMapName;
        addMap(newMapFilePath);
        setCurrentMap(mapPaths.indexOf(newMapFilePath));
        teleportToNewMap(tmpMap);
        charactersManager.initializeMonsters();
        charactersManager.initializePNJ();
    }

    public void teleportToNewMap(String oldMap) {
        TiledMap newMap = getCurrentMap();
        TiledMapTileLayer newTeleportationLayer = (TiledMapTileLayer) newMap.getLayers().get("Teleportation");

        int tileSize = getTileWidth();

        for (int x = 0; x < newTeleportationLayer.getWidth(); x++) {
            for (int y = 0; y < newTeleportationLayer.getHeight(); y++) {
                Cell currentTeleportationCell = newTeleportationLayer.getCell(x, y);

                if (currentTeleportationCell != null && currentTeleportationCell.getTile() != null) {
                    String destination = currentTeleportationCell.getTile().getProperties().get("destination", String.class);

                    if (destination.equals(oldMap)) {
                        charactersManager.getCurrentHeroes().setX(x * tileSize);
                        charactersManager.getCurrentHeroes().setY(y * tileSize);
                        return;
                    }
                }
            }
        }
    }

    public boolean isHealingZone() {
        return currentMapName.equals("Thermes");
    }

    public boolean isFightingZone() {
        return currentMapName.equals("Manoir") || currentMapName.equals("MonsterZone1") || currentMapName.equals("MonsterZone2") || currentMapName.equals("MonsterZone3") || currentMapName.equals("MonsterZone4") ;
    }


    public void setGameModel(GameModel model) {
        this.model = model;
    }


    public void render(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public void dispose() {
        mapRenderer.dispose();
        for (TiledMap map : mapList) {
            map.dispose();
        }
    }


    public int getCurrentMapIndex() {
        return currentMapIndex;
    }
}
