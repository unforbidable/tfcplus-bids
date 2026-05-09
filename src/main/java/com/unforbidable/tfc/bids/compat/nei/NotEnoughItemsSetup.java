package com.unforbidable.tfc.bids.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.features.crafting.carding.nei.CardingNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.carving.nei.CarvingNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.chopping.nei.ChoppingNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.churning.nei.ChurningNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.cooking.nei.CookingNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.cooking.nei.CookingPrepNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.drying.nei.DryingNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.handwork.nei.HandworkNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.heckling.nei.HecklingNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.processing.nei.ProcessingNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.pressing.nei.ScrewPressNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.pressing.nei.StonePressNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.quern.nei.SaddleQuernNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.ropemaking.nei.RopeMakingNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.seasoning.nei.SeasoningNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.soaking.nei.SoakingNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.spinning.nei.SpinningNeiHandler;
import com.unforbidable.tfc.bids.features.crafting.woodworking.nei.WoodworkingNeiHandler;
import com.unforbidable.tfc.bids.features.device.firepit.nei.FirepitFuelNeiHandler;
import com.unforbidable.tfc.bids.features.resource.quarry.nei.QuarryNeiHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class NotEnoughItemsSetup {

    @SideOnly(Side.CLIENT)
    public static void init() {
        hideItemStacks();
        registerHandlers();
    }

    private static void registerHandlers() {
        Bids.LOG.info("Registering NEI handlers");

        registerHandler(new QuarryNeiHandler());
        registerHandler(new SeasoningNeiHandler());
        registerHandler(new FirepitFuelNeiHandler());
        registerHandler(new DryingNeiHandler());
        registerHandler(new CarvingNeiHandler());
        registerHandler(new ChoppingNeiHandler());
        registerHandler(new SaddleQuernNeiHandler());
        registerHandler(new StonePressNeiHandler());
        registerHandler(new CookingNeiHandler());
        registerHandler(new CookingPrepNeiHandler());
        registerHandler(new ScrewPressNeiHandler());
        registerHandler(new ChurningNeiHandler());
        registerHandler(new ProcessingNeiHandler());
        registerHandler(new SoakingNeiHandler());
        registerHandler(new SpinningNeiHandler());
        registerHandler(new RopeMakingNeiHandler());
        registerHandler(new HandworkNeiHandler());
        registerHandler(new CardingNeiHandler());
        registerHandler(new HecklingNeiHandler());
        registerHandler(new WoodworkingNeiHandler());
    }

    private static void registerHandler(TemplateRecipeHandler handler) {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);

        if (handler instanceof IHandlerInfoProvider) {
            registerHandlerHeaderInfo(handler.getOverlayIdentifier(), ((IHandlerInfoProvider)handler).getHandlerInfo());
        }
    }

    private static void registerHandlerHeaderInfo(String handlerId, HandlerInfo handlerInfo) {
        NBTTagCompound handlerMetadata = new NBTTagCompound();
        handlerMetadata.setString("handler", handlerId);
        handlerMetadata.setString("modName", Tags.MOD_NAME);
        handlerMetadata.setString("modId", Tags.MOD_ID);
        handlerMetadata.setBoolean("modRequired", true);
        handlerMetadata.setString("itemName", handlerInfo.getUniqueBlockOrItemId());
        handlerMetadata.setInteger("handlerHeight", handlerInfo.getHeight());
        handlerMetadata.setInteger("maxRecipesPerPage", handlerInfo.getRecipesPerPage());
        FMLInterModComms.sendMessage("NotEnoughItems", "registerHandlerInfo", handlerMetadata);

        Bids.LOG.info("Sent registerHandlerInfo message for: " + handlerId);

        for (HandlerCatalystInfo catalystInfo : handlerInfo.getCatalysts()) {
            NBTTagCompound catalystMetadata = new NBTTagCompound();
            catalystMetadata.setString("handlerID", handlerId);
            catalystMetadata.setString("itemName", catalystInfo.getUniqueBlockOrItemId());
            FMLInterModComms.sendMessage("NotEnoughItems", "registerCatalystInfo", catalystMetadata);

            Bids.LOG.debug("Sent registerCatalystInfo message for: " + handlerId + ", " + catalystInfo.getUniqueBlockOrItemId());
        }
    }

    private static void hideItemStacks() {
        Bids.LOG.info("Hide items and blocks from NEI");

        hideItem(BidsItems.flatGlass);
        hideItem(BidsItems.barkFibreStrip); // OBSOLETE
        hideItem(BidsItems.flatBirchBark);

        hideItem(BidsItems.sedRoughStoneLooseBrick);

        hideItem(BidsItems.stuffedPepper);
        hideItem(BidsItems.stuffedMushroom);
        hideItem(BidsItems.pemmican);
        hideItem(BidsItems.cookingMixture);
        hideItem(BidsItems.stew);
        hideItem(BidsItems.soup);
        hideItem(BidsItems.porridge);

        hideBlock(BidsBlocks.clayCrucible, 2);
        hideBlock(BidsBlocks.carvingRock);
        hideBlock(BidsBlocks.carvingWood);
        hideBlock(BidsBlocks.quarry);
        hideBlock(BidsBlocks.woodPile);
        hideBlock(BidsBlocks.newFirepit);
        hideBlock(BidsBlocks.dryingRack);
        hideBlock(BidsBlocks.dryingSurface);
        hideBlock(BidsBlocks.soakingSurface);
        hideBlock(BidsBlocks.processingSurface);
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

        hideBlock(BidsBlocks.logWallVertAlt);
        hideBlock(BidsBlocks.logWallVertAlt2);
        hideBlock(BidsBlocks.logWallVertAlt3);

        hideBlock(BidsBlocks.steamingMesh);

        hideBlock(BidsBlocks.newCrops);
        hideBlock(BidsBlocks.newTilledSoil);
        hideBlock(BidsBlocks.newTilledSoil2);

        hideBlock(BidsBlocks.screwPressRackMiddle);
        hideBlock(BidsBlocks.screwPressRackTop);
        hideBlock(BidsBlocks.screwPressLeverTop);

        // meta 11 does not exist but NEI still tries to show it and causes errors
        hideBlock(BidsBlocks.unfinishedAnvilStage1, 11);
        hideBlock(BidsBlocks.unfinishedAnvilStage2, 11);
        hideBlock(BidsBlocks.unfinishedAnvilStage3, 11);
        hideBlock(BidsBlocks.unfinishedAnvilStage4, 11);
        hideBlock(BidsBlocks.unfinishedAnvilStage5, 11);
        hideBlock(BidsBlocks.unfinishedAnvilStage6, 11);

        hideBlock(BidsBlocks.light);
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
