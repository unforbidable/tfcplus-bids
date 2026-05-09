package com.unforbidable.tfc.bids.features.food.milk.main;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public interface IMilkHandler<TAnimal extends EntityAnimal> {

    boolean canAnimalBeMilkedByPlayer(TAnimal animal, EntityPlayer player);
    boolean doMilkAnimalByPlayer(TAnimal animal, EntityPlayer player, int amount);

}
