package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.Crafting.ChoppingBlockManager;
import com.unforbidable.tfc.bids.api.Crafting.ChoppingBlockRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChoppingBlockHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "choppingblock";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID,
            "textures/gui/nei/gui_choppingblock.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.ChoppingBlock");
    }

    @Override
    public String getGuiTexture() {
        return guiTexture.toString();
    }

    @Override
    public String getOverlayIdentifier() {
        return HANDLER_ID;
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(71, 23, 24, 18), HANDLER_ID));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(HANDLER_ID) && getClass() == ChoppingBlockHandler.class) {
            for (ChoppingBlockRecipe recipe : ChoppingBlockManager.getRecipes()) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getCraftingResult(input);
                List<ItemStack> tools = OreDictionary.getOres(recipe.getToolOreName(), false);
                List<ItemStack> blocks = OreDictionary.getOres("blockChoppingBlock", false);
                arecipes.add(new CachedChoppingRecipe(input, result, tools, blocks));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack output) {
        for (ChoppingBlockRecipe recipe : ChoppingBlockManager.getRecipes()) {
            final ItemStack output2 = output.copy();
            output2.stackSize = 1;
            final ItemStack input = recipe.getInput();
            final ItemStack result = recipe.getCraftingResult(input);
            if (ItemStack.areItemStacksEqual(result, output2)) {
                List<ItemStack> tools = OreDictionary.getOres(recipe.getToolOreName(), false);
                List<ItemStack> blocks = OreDictionary.getOres("blockChoppingBlock", false);
                arecipes.add(new CachedChoppingRecipe(input, output2, tools, blocks));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (ChoppingBlockRecipe recipe : ChoppingBlockManager.getRecipes()) {
            if (recipe.matchesInput(ingredient)) {
                final ItemStack input = new ItemStack(ingredient.getItem(), 1, ingredient.getItemDamage());
                final ItemStack result = recipe.getCraftingResult(input);
                List<ItemStack> tools = OreDictionary.getOres(recipe.getToolOreName(), false);
                List<ItemStack> blocks = OreDictionary.getOres("blockChoppingBlock", false);
                arecipes.add(new CachedChoppingRecipe(input, result, tools, blocks));
            } else if (recipe.matchesChoppingBlock(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getCraftingResult(input);
                List<ItemStack> tools = OreDictionary.getOres(recipe.getToolOreName(), false);
                List<ItemStack> blocks = new ArrayList<ItemStack>();
                blocks.add(ingredient);
                arecipes.add(new CachedChoppingRecipe(input, result, tools, blocks));
            } else if (recipe.matchesTool(ingredient)) {
                final ItemStack input = recipe.getInput();
                final ItemStack result = recipe.getCraftingResult(input);
                List<ItemStack> tools = new ArrayList<ItemStack>();
                tools.add(ingredient);
                List<ItemStack> blocks = OreDictionary.getOres("blockChoppingBlock", false);
                arecipes.add(new CachedChoppingRecipe(input, result, tools, blocks));
            }
        }
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(BidsBlocks.choppingBlock, 0);
        for (ItemStack is : OreDictionary.getOres("blockChoppingBlock", false)) {
            info.addCatalyst(Block.getBlockFromItem(is.getItem()), is.getItem().getMetadata(is.getItemDamage()));
        }
        return info;
    }

    public class CachedChoppingRecipe extends CachedRecipe {

        final ItemStack ingred;
        final ItemStack result;
        final List<ItemStack> tools;
        final List<ItemStack> blocks;

        public CachedChoppingRecipe(ItemStack ingred, ItemStack result, List<ItemStack> tools, List<ItemStack> blocks) {
            this.ingred = ingred.copy();
            this.result = result.copy();
            this.tools = tools;
            this.blocks = blocks;
        }

        @Override
        public PositionedStack getResult() {
            return new PositionedStack(result, 111, 24);
        }

        @Override
        public PositionedStack getIngredient() {
            return new PositionedStack(ingred, 39, 24);
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            List<PositionedStack> list = new ArrayList<PositionedStack>();
            final int i = cycleticks % (20 * tools.size());
            list.add(new PositionedStack(tools.get(i / 20), 19, 6));
            final int j = cycleticks % (20 * blocks.size());
            list.add(new PositionedStack(blocks.get(j / 20), 19, 24));
            return list;
        }

    }

}
