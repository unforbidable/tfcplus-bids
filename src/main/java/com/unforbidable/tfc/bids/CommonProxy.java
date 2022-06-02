package com.unforbidable.tfc.bids;

import java.util.ArrayList;
import java.util.List;

import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRaw;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Crafting.CraftingManagerTFC;
import com.unforbidable.tfc.bids.Blocks.BlockClayCrucible;
import com.unforbidable.tfc.bids.Blocks.BlockFireClayCrucible;
import com.unforbidable.tfc.bids.Handlers.ConfigHandler;
import com.unforbidable.tfc.bids.Handlers.CraftingHandler;
import com.unforbidable.tfc.bids.Handlers.GuiHandler;
import com.unforbidable.tfc.bids.Items.ItemOreBit;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemClayCrucible;
import com.unforbidable.tfc.bids.Items.ItemBlocks.ItemFireClayCrucible;
import com.unforbidable.tfc.bids.Recipes.RecipeCrucibleConversion;
import com.unforbidable.tfc.bids.TileEntities.TileEntityClayCrucible;
import com.unforbidable.tfc.bids.TileEntities.TileEntityFireClayCrucible;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new ConfigHandler(event.getModConfigurationDirectory()));

        GameRegistry.registerTileEntity(TileEntityClayCrucible.class, "BidsClayCrucible");
        GameRegistry.registerTileEntity(TileEntityFireClayCrucible.class, "BidsFireClayCrucible");

        BidsBlocks.clayCrucible = new BlockClayCrucible().setBlockName("ClayCrucible").setHardness(4.0f);
        BidsBlocks.fireClayCrucible = new BlockFireClayCrucible().setBlockName("FireClayCrucible").setHardness(4.0f);

        GameRegistry.registerBlock(BidsBlocks.clayCrucible, ItemClayCrucible.class, "ClayCrucible");
        GameRegistry.registerBlock(BidsBlocks.fireClayCrucible, ItemFireClayCrucible.class, "FireClayCrucible");

        BidsItems.oreBit = new ItemOreBit().setUnlocalizedName("Ore Bit");

        GameRegistry.registerItem(BidsItems.oreBit, BidsItems.oreBit.getUnlocalizedName());

        NetworkRegistry.INSTANCE.registerGuiHandler(Bids.instance, new GuiHandler());

        FMLCommonHandler.instance().bus().register(new CraftingHandler());
    }

    public void init(FMLInitializationEvent event) {
        FMLInterModComms.sendMessage("Waila", "register",
                "com.unforbidable.tfc.bids.WAILA.WailaCrucible.callbackRegister");

        GameRegistry.addRecipe(new RecipeCrucibleConversion(true));
        GameRegistry.addRecipe(new RecipeCrucibleConversion(false));

        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.clayCrucible, 1),
                new Object[] { "#####", " ### ", " ### ", " ### ", "     ", '#',
                        new ItemStack(TFCItems.flatClay, 1, 1) });
        CraftingManagerTFC.getInstance().addRecipe(new ItemStack(BidsBlocks.fireClayCrucible, 1),
                new Object[] { "#####", " ### ", " ### ", " ### ", "     ", '#',
                        new ItemStack(TFCItems.flatClay, 1, 3) });

        for (int i = 0; i < Global.ORE_METAL.length; i++) {
            ItemStack small = new ItemStack(TFCItems.smallOreChunk, 1, i);
            ItemStack poor = new ItemStack(TFCItems.oreChunk, 1, Global.oreGrade2Offset + i);
            ItemStack normal = new ItemStack(TFCItems.oreChunk, 1, i);
            ItemStack rich = new ItemStack(TFCItems.oreChunk, 1, Global.oreGrade1Offset + i);

            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 2, i),
                    small, "itemHammer"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 3, i),
                    poor, "itemHammer"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 5, i),
                    normal, "itemHammer"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BidsItems.oreBit, 7, i),
                    rich, "itemHammer"));
        }

    }

    public void postInit(FMLPostInitializationEvent event) {
        HeatRegistry manager = HeatRegistry.getInstance();

        Bids.LOG.info("Registering heat indices");
        List<HeatIndex> heatList = new ArrayList<HeatIndex>();
        for (HeatIndex index : manager.getHeatList()) {
            if (index.input.getItem() == TFCItems.smallOreChunk) {
                ItemStack is = new ItemStack(BidsItems.oreBit, 1, index.input.getItemDamage());
                HeatRaw raw = new HeatRaw(index.specificHeat, index.meltTemp);
                heatList.add(new HeatIndex(is, raw, new ItemStack(index.getOutputItem(), 1)));

                Bids.LOG.info("Registered heat index for: " + is.getDisplayName());
            }
        }

        for (HeatIndex index : heatList) {
            manager.addIndex(index);
        }
    }

}
