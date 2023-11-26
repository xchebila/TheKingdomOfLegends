package com.tkol.game.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tkol.game.characters.CharactersManager;
import com.tkol.game.characters.heroes.Heroes;
import com.tkol.game.inventory.items.Items;
import com.tkol.game.inventory.items.Usable;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private Heroes currentHero;
    private List<Items> items;
    private Stage stage;
    private Table table;
    private Skin skin;
    private int currentSelection;
    private boolean isOpen;
    private static final float INVENTORY_HEIGHT_PERCENT = 1f;
    private CharactersManager charactersManager;


    public Inventory() {
        this.items = new ArrayList<>();
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin();
        this.table = new Table();
        this.table.setFillParent(true);
        this.currentSelection = 0;
        setCharactersManager(charactersManager);

        stage.addActor(table);

        table.bottom();

        table.setHeight(Gdx.graphics.getHeight() * INVENTORY_HEIGHT_PERCENT);

        this.isOpen = false;
    }

    public List<Items> getItems() {
        return items;
    }

    public int getCurrentSelection() {
        return currentSelection;
    }

    public void setCurrentSelection(int currentSelection) {
        this.currentSelection = currentSelection;
    }

    public void setCharactersManager(CharactersManager charactersManager) {
        this.charactersManager = charactersManager;
    }
    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setCurrentHero(Heroes hero) {
        this.currentHero = hero;
    }

    public void render(float delta) {

        handleInput();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }


    public void handleInput() {
        if (isOpen) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A) && currentSelection > 0) {
                currentSelection--;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.D) && currentSelection < items.size() - 1) {
                currentSelection++;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                useSelectedItem();
            }

            updateInventoryUI();
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                setOpen(true);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                setOpen(true);
            } else {
                setOpen(false);
            }

        }
    }


    public void useSelectedItem() {
        if (currentSelection >= 0 && currentSelection < items.size()) {
            Items selectedItem = items.get(currentSelection);
            if (selectedItem instanceof Usable) {
                ((Usable) selectedItem).use(currentHero);
                removeItem(selectedItem);
            }
        }
    }

    public void addItem(Items item) {
        items.add(item);
        updateInventoryUI();
    }

    public void removeItem(Items item) {
        items.remove(item);
        updateInventoryUI();
    }

    public void updateInventoryUI() {
        table.clear();

        for (int i = 0; i < items.size(); i++) {
            Items item = items.get(i);
            Image image = new Image(item.getTexture());

            Table cellTable = new Table();
            cellTable.add(image).size(50, 50);

            table.add(cellTable).pad(10);

            if (i == currentSelection) {
                image.setColor(Color.YELLOW);
            } else {
                image.setColor(Color.WHITE);
            }

        }

        table.setHeight(Gdx.graphics.getHeight() * INVENTORY_HEIGHT_PERCENT);
        table.setWidth(Gdx.graphics.getWidth());
        table.bottom();
        table.setFillParent(false);
    }

}