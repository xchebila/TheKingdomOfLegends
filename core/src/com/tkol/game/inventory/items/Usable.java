package com.tkol.game.inventory.items;

import com.tkol.game.characters.heroes.Heroes;

public interface Usable {
    void use(Heroes hero);

    int getGoldCost(); // Ajoutez cette méthode pour obtenir le coût en or de l'item.

    String getName(); // Vous pouvez également ajouter cette méthode pour obtenir le nom de l'item.
}

