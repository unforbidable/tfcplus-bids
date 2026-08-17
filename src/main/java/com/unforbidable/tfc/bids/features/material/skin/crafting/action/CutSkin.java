package com.unforbidable.tfc.bids.features.material.skin.crafting.action;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.core.crafting.CraftingContext;
import com.unforbidable.tfc.bids.features.material.skin.crafting.SkinCuttingRecipe;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemSkin;
import com.unforbidable.tfc.bids.features.material.skin.main.SkinHelper;
import com.unforbidable.tfc.bids.features.material.skin.main.nbt.SkinTag;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import java.util.function.Consumer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;

public class CutSkin {

    public static Consumer<CraftingContext> cutSkin() {
        return context -> new CutSkin()
            .onItemCrafted(context);
    }

    protected void onItemCrafted(CraftingContext context) {
        findCutAndIncreaseItemStackSize(context.event);
    }

    private void findCutAndIncreaseItemStackSize(PlayerEvent.ItemCraftedEvent event) {
        for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
            if (event.craftMatrix.getStackInSlot(i) != null) {
                ItemStack is = event.craftMatrix.getStackInSlot(i);
                if (is.getItem() instanceof ItemSkin) {
                    float cutWeight = getCutWeight((InventoryCrafting) event.craftMatrix, event.player.worldObj, is);

                    if (cutWeight <= 0) {
                        Bids.LOG.warn("Something went wrong and the cut weight is 0 or less {} - enjoy infinite skins", cutWeight);
                    }

                    SkinTag tag = SkinTag.of(is);
                    float weight = tag.getWeight();
                    float remainingWeight = weight - cutWeight;

                    if (remainingWeight > 0) {
                        tag.setWeight(remainingWeight);
                        is.stackSize = is.stackSize + 1;
                    }

                    break;
                }
            }
        }
    }

    private float getCutWeight(InventoryCrafting craftMatrix, World world, ItemStack itemStack) {
        SkinCuttingRecipe recipe = findCuttingRecipe(craftMatrix, world);
        if (recipe != null) {
            float weight = SkinTag.of(itemStack).getWeight();

            if (recipe.hasLargeOutput() && weight >= SkinHelper.WEIGHT_LARGE) {
                return SkinHelper.WEIGHT_LARGE;
            } else if (recipe.hasMediumOutput() && weight >= SkinHelper.WEIGHT_MEDIUM) {
                return SkinHelper.WEIGHT_MEDIUM;
            } else if (recipe.hasSmallOutput() && weight >= SkinHelper.WEIGHT_SMALL) {
                return SkinHelper.WEIGHT_SMALL;
            } else if (recipe.hasTinyOutput() && weight >= SkinHelper.WEIGHT_VERY_SMALL) {
                return SkinHelper.WEIGHT_VERY_SMALL;
            } else if (recipe.hasRepairPatchOutput()) {
                return weight;
            }
        }

        return 0;
    }

    private SkinCuttingRecipe findCuttingRecipe(InventoryCrafting craftMatrix, World worldObj) {
        for (Object obj : CraftingManager.getInstance().getRecipeList()) {
            if (obj instanceof SkinCuttingRecipe) {
                SkinCuttingRecipe recipe = (SkinCuttingRecipe) obj;
                if (recipe.matches(craftMatrix, worldObj)) {
                    return recipe;
                }
            }
        }

        return null;
    }

}
