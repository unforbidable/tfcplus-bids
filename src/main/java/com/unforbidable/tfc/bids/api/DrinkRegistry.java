package com.unforbidable.tfc.bids.api;

import java.util.ArrayList;
import java.util.List;

import com.unforbidable.tfc.bids.api.Interfaces.IDrinkable;

public class DrinkRegistry {

    private static ArrayList<IDrinkable> list = new ArrayList<IDrinkable>();

    public static void registerDrink(IDrinkable drink) {
        list.add(drink);
    }

    public static List<IDrinkable> getDrinks() {
        return list;
    }

}
