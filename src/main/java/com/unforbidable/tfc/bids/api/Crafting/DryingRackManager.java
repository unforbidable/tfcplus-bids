package com.unforbidable.tfc.bids.api.Crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DryingRackManager {

    static final List<DryingRackRecipe> recipes = new ArrayList<DryingRackRecipe>();
    static final List<TyingEquipment> tyingEquipment = new ArrayList<TyingEquipment>();

    public static void addRecipe(DryingRackRecipe recipe) {
        recipes.add(recipe);
    }

    public static DryingRackRecipe getMatchingRecipe(ItemStack itemStack) {
        for (DryingRackRecipe recipe : recipes) {
            if (recipe.matches(itemStack)) {
                return recipe;
            }
        }

        return null;
    }

    public static boolean hasMatchingRecipe(ItemStack itemStack) {
        return getMatchingRecipe(itemStack) != null;
    }

    public static List<DryingRackRecipe> getRecipes() {
        return new ArrayList<DryingRackRecipe>(recipes);
    }

    public static void registerTyingEquipment(Item item, boolean isReusable,
            Block renderBlock, int renderBlockMetadata) {
        registerTyingEquipment(new ItemStack(item, 1, 0), isReusable, renderBlock, renderBlockMetadata);
    }

    public static void registerTyingEquipment(ItemStack itemStack, boolean isReusable,
            Block renderBlock, int renderBlockMetadata) {
        tyingEquipment.add(new TyingEquipment(itemStack, isReusable, renderBlock, renderBlockMetadata));
    }

    public static List<TyingEquipment> getTyingEquipment() {
        return new ArrayList<TyingEquipment>(tyingEquipment);
    }

    public static TyingEquipment findTyingEquipmnt(ItemStack item) {
        for (TyingEquipment te : tyingEquipment) {
            if (te.item.getItem() == item.getItem() && te.item.getItemDamage() == item.getItemDamage()) {
                return te;
            }
        }

        return null;
    }

    public static class TyingEquipment {

        public final ItemStack item;
        public final boolean isReusable;
        public final Block renderBlock;
        public final int renderBlockMetadata;

        public TyingEquipment(ItemStack item, boolean isReusable, Block renderBlock, int renderBlockMetadata) {
            this.item = item;
            this.isReusable = isReusable;
            this.renderBlock = renderBlock;
            this.renderBlockMetadata = renderBlockMetadata;
        }

    }

}
