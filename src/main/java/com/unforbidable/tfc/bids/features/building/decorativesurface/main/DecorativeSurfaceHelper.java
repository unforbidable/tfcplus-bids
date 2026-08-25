package com.unforbidable.tfc.bids.features.building.decorativesurface.main;

import com.unforbidable.tfc.bids.BidsEventFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class DecorativeSurfaceHelper {

    public static boolean isDecorativeSurfaceItem(ItemStack itemStack) {
        int decorativeSurfaceItemOreId = OreDictionary.getOreID("itemDecorativeSurface");
        for (int oreId : OreDictionary.getOreIDs(itemStack)) {
            if (oreId == decorativeSurfaceItemOreId) {
                return true;
            }
        }

        return false;
    }

    public static boolean canPlaceItem(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int face) {
        boolean isDecorativeSurfaceItem = isDecorativeSurfaceItem(itemStack);
        return BidsEventFactory.onDecorativeSurfacePlace(itemStack, player, world, x, y, z, face, isDecorativeSurfaceItem);
    }

}
