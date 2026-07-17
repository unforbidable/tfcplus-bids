package com.unforbidable.tfc.bids.features.crafting.handwork.item;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemTerra;
import com.unforbidable.tfc.bids.api._obsolete.BidsEventFactory;
import com.unforbidable.tfc.bids.api.features.handwork.HandworkRecipe;
import com.unforbidable.tfc.bids.api.features.handwork.HandworkToolMaterial;
import com.unforbidable.tfc.bids.features.crafting.handwork.HandworkConfig;
import com.unforbidable.tfc.bids.features.crafting.handwork.HandworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHandworkMaterial extends ItemTerra implements HandworkToolMaterial {
    private boolean hasMaterialColor = false;
    private int materialColor = 0;

    public ItemHandworkMaterial setMaterialColor(int materialColor) {
        this.materialColor = materialColor;
        this.hasMaterialColor = true;
        return this;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        if (!player.worldObj.isRemote) {
            if (count % 5 == 1) {
                player.worldObj.playSoundAtEntity(player, "step.grass", 0.5F, 1f);
            }

            if (count <= 1 && player.isUsingItem()) {
                HandworkRecipe recipe = HandworkRegistry.recipes.findMatchingRecipe(stack);
                if (recipe != null) {
                    ItemStack outputItem = recipe.getResult(stack);
                    BidsEventFactory.onHandworkItemCrafted(player, stack, outputItem, null);

                    TFC_Core.giveItemToPlayer(outputItem, player);
                    stack.stackSize -= recipe.getInput().stackSize;
                }

                player.stopUsingItem();
            }
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack is) {
        return EnumAction.block;
    }

    public int getMaxItemUseDuration(ItemStack is) {
        HandworkRecipe recipe = HandworkRegistry.recipes.findMatchingRecipe(is);
        if (recipe != null) {
            return getActualMaxItemDuration(recipe.getDuration());
        }

        return 20;
    }

    protected int getActualMaxItemDuration(int duration) {
        return Math.round(duration * HandworkConfig.handworkDurationMultiplier);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (!player.isUsingItem()) {
            HandworkRecipe recipe = HandworkRegistry.recipes.findMatchingRecipe(is);
            if (recipe != null && is.stackSize >= recipe.getInput().stackSize) {
                player.setItemInUse(is, getActualMaxItemDuration(recipe.getDuration()));
            }
        }
        return is;
    }

    @Override
    public int getColorFromMaterial(ItemStack is, int pass) {
        return hasMaterialColor ? materialColor : getColorFromItemStack(is, pass);
    }
}
