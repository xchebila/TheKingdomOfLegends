package com.tkol.game.characters.pnj;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tkol.game.characters.CharactersManager;
import com.tkol.game.characters.heroes.Heroes;
import com.tkol.game.inventory.*;
import com.tkol.game.inventory.items.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Trader extends PNJ {
    private static final float PORTÉE_MAXIMALE = 50f;
    private final List<Sellable> availableItems;

    public Trader(float initialX, float initialY, CharactersManager charactersManager) {
        super(initialX, initialY, charactersManager);
        this.availableItems = initializeItems();
    }

    @Override
    protected void initTexture() {
        Texture traderTexture = new Texture(Gdx.files.internal("pnj/Trader.png"));
        setCharacterRegion(new TextureRegion(traderTexture));
    }

    @Override
    public void interact(Heroes hero, Inventory inventory) {
        if (isHeroInRange(hero)) {
            openTraderDialog(hero, inventory);
        }
    }

    private boolean isHeroInRange(Heroes hero) {
        float distance = calculateDistance(hero.getX(), hero.getY());
        return distance < PORTÉE_MAXIMALE;
    }

    private void openTraderDialog(Heroes hero, Inventory inventory) {
        System.out.println("Bienvenue chez le marchand !");

        boolean shopping = true;

        while (shopping) {
            System.out.println("Que souhaitez-vous acheter ?");

            for (int i = 0; i < getAvailableItems().size(); i++) {
                Sellable item = getAvailableItems().get(i);
                System.out.println(i + ". " + item.getName() + " - Coût : " + item.getGoldCost() + " pièces d'or");
            }

            System.out.println("99. Quitter");

            Scanner scanner = new Scanner(System.in);
            int selectedOption = -1;

            try {
                System.out.print("Sélectionnez un objet en entrant le numéro correspondant : ");
                selectedOption = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
            }

            if (selectedOption == 99) {
                System.out.println("Merci de votre visite ! Au revoir !");
                shopping = false;
            } else if (selectedOption >= 0 && selectedOption < getAvailableItems().size()) {
                Sellable selectedItem = getAvailableItems().get(selectedOption);

                if (hero.getGold() >= selectedItem.getGoldCost()) {
                    hero.decreaseGold(selectedItem.getGoldCost());

                    inventory.addItem((Items) selectedItem);

                    System.out.println("Vous avez acheté " + selectedItem.getName() + " pour " + selectedItem.getGoldCost() + " pièces d'or.");
                } else {
                    System.out.println("Vous n'avez pas assez d'or pour acheter cet objet.");
                }
            } else {
                System.out.println("Option invalide.");
            }
        }
    }


    private List<Sellable> getAvailableItems() {
        return availableItems;
    }


    private List<Sellable> initializeItems() {
        List<Sellable> items = new ArrayList<>();
        items.add(new HealthPotionSmall());
        items.add(new HealthPotionMedium());
        items.add(new HealthPotionBig());
        return items;
    }


}
