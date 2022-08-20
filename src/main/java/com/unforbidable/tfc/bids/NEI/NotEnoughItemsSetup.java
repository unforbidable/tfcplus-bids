package com.unforbidable.tfc.bids.NEI;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.NEI.Handlers.CarvingHandler;
import com.unforbidable.tfc.bids.NEI.Handlers.DryingRackHandler;
import com.unforbidable.tfc.bids.NEI.Handlers.FirepitFuelHandler;
import com.unforbidable.tfc.bids.NEI.Handlers.QuarryHandler;
import com.unforbidable.tfc.bids.NEI.Handlers.SeasoningHandler;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;

import codechicken.nei.api.API;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class NotEnoughItemsSetup {

    @SideOnly(Side.CLIENT)
    public static void init() {
        hideItemStacks();
        registerHandlers();
    }

    private static void registerHandlers() {
        Bids.LOG.info("Registering NEI handlers");

        API.registerRecipeHandler(new QuarryHandler());
        API.registerUsageHandler(new QuarryHandler());

        API.registerRecipeHandler(new SeasoningHandler());
        API.registerUsageHandler(new SeasoningHandler());

        API.registerRecipeHandler(new FirepitFuelHandler());
        API.registerUsageHandler(new FirepitFuelHandler());

        API.registerRecipeHandler(new DryingRackHandler());
        API.registerUsageHandler(new DryingRackHandler());

        API.registerRecipeHandler(new CarvingHandler());
        API.registerUsageHandler(new CarvingHandler());
    }

    private static void hideItemStacks() {
        Bids.LOG.info("Hide items and blocks from NEI");

        hideItem(BidsItems.flatGlass);

        hideBlock(BidsBlocks.clayCrucible, 2);
        hideBlock(BidsBlocks.carvingRock);
        hideBlock(BidsBlocks.carvingWood);
        hideBlock(BidsBlocks.quarry);
        hideBlock(BidsBlocks.woodPile);
        hideBlock(BidsBlocks.newFirepit);
        hideBlock(BidsBlocks.tiedStickBundle);

        hideBlock(BidsBlocks.logWallEastAlt);
        hideBlock(BidsBlocks.logWallEastAlt2);
        hideBlock(BidsBlocks.logWallEastAlt3);
        hideBlock(BidsBlocks.logWallNorth);
        hideBlock(BidsBlocks.logWallNorth2);
        hideBlock(BidsBlocks.logWallNorth3);
        hideBlock(BidsBlocks.logWallNorthAlt);
        hideBlock(BidsBlocks.logWallNorthAlt2);
        hideBlock(BidsBlocks.logWallNorthAlt3);
        hideBlock(BidsBlocks.logWallCorner);
        hideBlock(BidsBlocks.logWallCorner2);
        hideBlock(BidsBlocks.logWallCorner3);
        hideBlock(BidsBlocks.logWallCornerAlt);
        hideBlock(BidsBlocks.logWallCornerAlt2);
        hideBlock(BidsBlocks.logWallCornerAlt3);
    }

    private static void hideBlock(Block block) {
        hideBlock(block, OreDictionary.WILDCARD_VALUE);
    }

    private static void hideItem(Item item) {
        hideItem(item, OreDictionary.WILDCARD_VALUE);
    }

    private static void hideBlock(Block block, int meta) {
        API.hideItem(new ItemStack(block, 1, meta));
    }

    private static void hideItem(Item item, int damage) {
        API.hideItem(new ItemStack(item, 1, damage));
    }

}
