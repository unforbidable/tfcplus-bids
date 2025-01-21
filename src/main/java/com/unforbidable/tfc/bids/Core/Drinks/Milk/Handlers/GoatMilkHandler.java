package com.unforbidable.tfc.bids.Core.Drinks.Milk.Handlers;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Entities.Mobs.EntityGoat;
import com.dunk.tfc.api.Entities.IAnimal;
import com.unforbidable.tfc.bids.Core.Drinks.Milk.GoatMilkHelper;
import com.unforbidable.tfc.bids.Core.Drinks.Milk.IMilkHandler;
import com.unforbidable.tfc.bids.api.BidsOptions;
import net.minecraft.entity.player.EntityPlayer;

public class GoatMilkHandler implements IMilkHandler<EntityGoat> {

    private static final int TICKS_PER_BUCKET = TFC_Time.DAY_LENGTH * 5;

    @Override
    public boolean canAnimalBeMilkedByPlayer(EntityGoat animal, EntityPlayer player) {
        return animal.isAdult() &&
            animal.getGender() == IAnimal.GenderEnum.FEMALE &&
            GoatMilkHelper.isMilkable(animal) &&
            GoatMilkHelper.checkFamiliarity(animal, IAnimal.InteractionEnum.MILK, player);
    }

    @Override
    public boolean doMilkAnimalByPlayer(EntityGoat animal, EntityPlayer player, int amount) {
        float multiplier = animal.isDomesticated() ? 1f : BidsOptions.Husbandry.ibexMilkingTimerMultiplier;
        long time = TFC_Time.getTotalTicks() + Math.round((amount / 1000f) * TICKS_PER_BUCKET * multiplier) - BidsOptions.Husbandry.milkingTimerReductionHours * TFC_Time.HOUR_LENGTH;
        GoatMilkHelper.setHasMilkTime(animal, time);

        return true;
    }

}
