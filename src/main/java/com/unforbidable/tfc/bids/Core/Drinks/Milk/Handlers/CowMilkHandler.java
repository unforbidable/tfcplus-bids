package com.unforbidable.tfc.bids.Core.Drinks.Milk.Handlers;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Entities.Mobs.EntityCowTFC;
import com.dunk.tfc.api.Entities.IAnimal;
import com.unforbidable.tfc.bids.Core.Drinks.Milk.IMilkHandler;
import net.minecraft.entity.player.EntityPlayer;

public class CowMilkHandler implements IMilkHandler<EntityCowTFC> {

    private static final int TICKS_PER_BUCKET = TFC_Time.DAY_LENGTH;

    @Override
    public boolean canAnimalBeMilkedByPlayer(EntityCowTFC animal, EntityPlayer player) {
        return animal.isAdult() &&
            animal.getGender() == IAnimal.GenderEnum.FEMALE &&
            animal.isMilkable() &&
            animal.checkFamiliarity(IAnimal.InteractionEnum.MILK, player);
    }

    @Override
    public boolean doMilkAnimalByPlayer(EntityCowTFC animal, EntityPlayer player, int amount) {
        long time = TFC_Time.getTotalTicks() + Math.round((amount / 1000f) * TICKS_PER_BUCKET);
        animal.setHasMilkTime(time);

        return true;
    }

}
