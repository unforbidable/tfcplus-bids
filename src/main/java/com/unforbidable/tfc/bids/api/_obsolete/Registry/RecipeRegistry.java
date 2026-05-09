package com.unforbidable.tfc.bids.api._obsolete.Registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RecipeRegistry<T> implements Iterable<T> {

    public final String name;
    private final List<T> recipes = new ArrayList<>();

    public RecipeRegistry(String name) {
        this.name = name;
    }

    public void register(T recipe) {
        recipes.add(recipe);
    }

    public T findMatchingRecipe(Predicate<T> predicate) {
        return recipes.stream()
            .filter(predicate)
            .findFirst().orElse(null);
    }

    public Stream<T> stream() {
        return recipes.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return recipes.iterator();
    }

}
