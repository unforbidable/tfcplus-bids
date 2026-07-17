package com.unforbidable.tfc.bids.features.utility.heckle.item;

import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.features.handwork.HandworkRecipe;
import com.unforbidable.tfc.bids.features.crafting.handwork.item.ItemHandworkTool;
import com.unforbidable.tfc.bids.features.crafting.handwork.main.HandworkProgress;
import com.unforbidable.tfc.bids.features.crafting.heckling.HecklingConfig;
import com.unforbidable.tfc.bids.features.crafting.heckling.HecklingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemHeckle extends ItemHandworkTool {

    public ItemHeckle(ToolMaterial material) {
        super(material);
    }

    @Override
    protected int getNumStages() {
        return 5;
    }

    @Override
    protected float getActualMaxItemDuration(float duration) {
        return duration * HecklingConfig.hecklingDurationMultiplier;
    }

    @Override
    protected ItemStack onItemBreak(ItemStack itemStack, World world, EntityPlayer player) {
        ItemStack brokenTool = new ItemStack(BidsItems.boneKnifeHead);
        // 80% chance to return both blades
        brokenTool.stackSize = player.getRNG().nextFloat() < 0.8f ? 2 : 1;
        return brokenTool;
    }

    @Override
    protected HandworkRecipe tryMatchIngredient(ItemStack is) {
        return HecklingRegistry.recipes.findMatchingRecipe(is);
    }

    @Override
    protected String getHandworkStartHelpString() {
        return StatCollector.translateToLocal("gui.Help.Heckling.Start");
    }

    @Override
    protected String getHandworkProcessHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Heckling.Process") +
            progress.inputItem.getDisplayName() +
            StatCollector.translateToLocal("gui.Help.Heckling.Process2");
    }

    @Override
    protected String getHandworkFinishHelpString(HandworkProgress progress) {
        return StatCollector.translateToLocal("gui.Help.Heckling.Finish") +
            progress.outputItem.getDisplayName() +
            StatCollector.translateToLocal("gui.Help.Heckling.Finish2");
    }

}
