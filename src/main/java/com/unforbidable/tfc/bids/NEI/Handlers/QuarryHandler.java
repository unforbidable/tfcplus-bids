package com.unforbidable.tfc.bids.NEI.Handlers;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.unforbidable.tfc.bids.NEI.HandlerInfo;
import com.unforbidable.tfc.bids.NEI.IHandlerInfoProvider;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;
import com.unforbidable.tfc.bids.api.QuarryRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuarryHandler extends TemplateRecipeHandler implements IHandlerInfoProvider {

    static final String HANDLER_ID = "quarry";

    public static ResourceLocation guiTexture = new ResourceLocation(Tags.MOD_ID, "textures/gui/nei/gui_quarry.png");

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("gui.Quarrying");
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
        if (outputId.equals(HANDLER_ID) && getClass() == QuarryHandler.class) {
            loadRecipes(null);
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    private void loadRecipes(ItemStack result) {
        for (IQuarriable quarriable : QuarryRegistry.getQuarriableBlocks()) {
            Block rawBlock = quarriable.getRawBlock();

            List<ItemStack> list = new ArrayList<ItemStack>();
            rawBlock.getSubBlocks(Item.getItemFromBlock(rawBlock), null, list);
            for (ItemStack ingredItemStack : list) {
                Block ingredBlock = Block.getBlockFromItem(ingredItemStack.getItem());
                int ingredMetadata = ingredItemStack.getItem().getMetadata(ingredItemStack.getItemDamage());

                if (quarriable.canQuarryBlock(ingredBlock, ingredMetadata)) {
                    Block outputBlock = quarriable.getQuarriedBlock();
                    int outputMetadata = quarriable.getQuarriedBlockMetadata(outputBlock, ingredMetadata);
                    ItemStack outputItemStack = new ItemStack(outputBlock, 1, outputMetadata);

                    if (result == null || ItemStack.areItemStacksEqual(result, outputItemStack)) {
                        CachedQuarriable cached = new CachedQuarriable(ingredItemStack, outputItemStack);
                        arecipes.add(cached);
                    }
                }
            }
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        loadRecipes(result);
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (ItemStack is : OreDictionary.getOres("itemDrill", false)) {
            if (is.getItem() == ingredient.getItem()) {
                loadRecipes(null);
                break;
            }
        }

        if (BidsItems.plugAndFeather == ingredient.getItem()) {
            loadRecipes(null);
        }
    }

    @Override
    public HandlerInfo getHandlerInfo() {
        HandlerInfo info = new HandlerInfo(BidsItems.sedStoneDrill);
        for (ItemStack is : OreDictionary.getOres("itemDrill", false)) {
            info.addCatalyst(is.getItem());
        }
        return info;
    }

    public class CachedQuarriable extends CachedRecipe {

        final ItemStack ingred;
        final ItemStack result;

        public CachedQuarriable(ItemStack ingred, ItemStack result) {
            this.ingred = ingred;
            this.result = result;
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

            List<ItemStack> drills = OreDictionary.getOres("itemDrill", false);
            final int i = cycleticks % (20 * drills.size());
            list.add(new PositionedStack(drills.get(i / 20), 19, 6));

            List<ItemStack> plugAndFeathers = OreDictionary.getOres("itemPlugAndFeather", false);
            final int j = cycleticks % (20 * plugAndFeathers.size());
            list.add(new PositionedStack(plugAndFeathers.get(j / 20), 19, 24));

            return list;
        }

    }

}
