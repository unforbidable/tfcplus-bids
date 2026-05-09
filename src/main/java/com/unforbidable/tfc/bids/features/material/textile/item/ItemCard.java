package com.unforbidable.tfc.bids.features.material.textile.item;

import com.unforbidable.tfc.bids.features.crafting.handwork.main.HandworkProgress;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api._obsolete.BidsOptions;
import com.unforbidable.tfc.bids.api._obsolete.BidsRegistry;
import com.unforbidable.tfc.bids.api._obsolete.Crafting.HandworkRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemCard extends ItemHandworkTool {

    public ItemCard(ToolMaterial material) {
        super(material);
    }

    @Override
    protected int getNumStages() {
        return 5;
    }

    @Override
    protected ItemStack onItemBreak(ItemStack itemStack, World world, EntityPlayer player) {
        ItemStack brokenTool = new ItemStack(BidsItems.woodenCombPaddle);
        // 90% chance to return the comb paddle without thorns
        brokenTool.stackSize = player.getRNG().nextFloat() < 0.9f ? 1 : 0;
        return brokenTool;
    }

    @Override
    protected float getActualMaxItemDuration(float duration) {
        return duration * BidsOptions.Crafting.cardingDurationMultiplier;
    }

    @Override
    protected HandworkRecipe tryMatchIngredient(ItemStack is) {
        return BidsRegistry.CARDING_RECIPES.findMatchingRecipe(is);
    }

    @Override
    protected String getHandworkStartHelpString() {
        return StatCollector.translateToLocal("gui.Help.Carding.Start");
    }

    @Override
    protected String getHandworkProcessHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Carding.Process") +
            progress.inputItem.getDisplayName() +
            StatCollector.translateToLocal("gui.Help.Carding.Process2");
    }

    @Override
    protected String getHandworkFinishHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Carding.Finish") +
            progress.outputItem.getDisplayName() +
            StatCollector.translateToLocal("gui.Help.Carding.Finish2");
    }

}
