package com.unforbidable.tfc.bids.features.device.cookingpot;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.BlockNames;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.compat.tfc.TfcRegistry;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KilnRecipe;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.KnappingRecipe;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.core.features.setup.FeatureSetupBuilder;
import com.unforbidable.tfc.bids.features.device.cookingpot.block.BlockCookingPot;
import com.unforbidable.tfc.bids.features.device.cookingpot.block.BlockCookingPotLid;
import com.unforbidable.tfc.bids.features.device.cookingpot.block.BlockSteamingMesh;
import com.unforbidable.tfc.bids.features.device.cookingpot.block.blockitem.ItemCookingPot;
import com.unforbidable.tfc.bids.features.device.cookingpot.block.blockitem.ItemCookingPotLid;
import com.unforbidable.tfc.bids.features.device.cookingpot.item.ItemSteamingMeshCloth;
import com.unforbidable.tfc.bids.features.device.cookingpot.recipe.RecipeEmptyCookingPot;
import com.unforbidable.tfc.bids.features.device.cookingpot.render.RenderCookingPot;
import com.unforbidable.tfc.bids.features.device.cookingpot.render.RenderCookingPotLid;
import com.unforbidable.tfc.bids.features.device.cookingpot.render.RenderTileCookingPot;
import com.unforbidable.tfc.bids.features.device.cookingpot.tileentity.TileEntityCookingPot;
import com.unforbidable.tfc.bids.features.device.cookingpot.waila.CookingPotWailaProvider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@FeatureName("cookingPot")
public class CookingPot extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.block(BlockNames.COOKING_POT, BlockCookingPot::new, ItemCookingPot.class)
            .apply(b -> b.setBlockTextureName("Cooking Pot"));
        init.block(BlockNames.COOKING_POT_LID, BlockCookingPotLid::new, ItemCookingPotLid.class)
            .apply(b -> b.setBlockTextureName("Cooking Pot Lid"));
        init.block(BlockNames.STEAMING_MESH, BlockSteamingMesh::new)
            .apply(b -> b.setBlockTextureName("Steaming Mesh"));

        init.tileEntity(TileEntityCookingPot.class, "BidsCookingPot");

        init.item(ItemNames.STEAMING_MESH_CLOTH, ItemSteamingMeshCloth::new)
            .apply(i -> i.setMaxDamage(TFCItems.linenUses));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new RenderCookingPot())
            .block(BlockCookingPot.class);
        client.render(new RenderCookingPotLid())
            .block(BlockCookingPotLid.class);
        client.render(new RenderTileCookingPot())
            .tileEntity(TileEntityCookingPot.class);

        client.waila()
            .data(new CookingPotWailaProvider(), TileEntityCookingPot.class);

        client.nei()
            .hide(BidsBlocks.steamingMesh);
    }

    @Override
    public void setup(FeatureSetupBuilder setup) {
        setup.ores("itemCookingPotAccessory")
            .add(BidsItems.steamingMeshCloth);

        setup.ores("itemCookingPotAccessorySteamingMesh")
            .add(BidsItems.steamingMeshCloth);

        setup.recipes().add(new RecipeEmptyCookingPot());

        setup.registry(TfcRegistry.Knapping.recipes)
            .add(KnappingRecipe.add(new ItemStack(BidsBlocks.cookingPot),
                " ### ", " ### ", " ### ", " ### ", "#   #", '#',
                new ItemStack(TFCItems.flatClay, 1, 1)))
            .add(KnappingRecipe.add(new ItemStack(BidsBlocks.cookingPotLid),
                "## ##", "     ", "#####", "#####", "#####", '#',
                new ItemStack(TFCItems.flatClay, 1, 1)));

        setup.registry(TfcRegistry.Kiln.recipes)
            .add(KilnRecipe.add(new ItemStack(BidsBlocks.cookingPot, 1, 0), 0,
                new ItemStack(BidsBlocks.cookingPot, 1, 1)))
            .add(KilnRecipe.add(new ItemStack(BidsBlocks.cookingPotLid, 1, 0), 0,
                new ItemStack(BidsBlocks.cookingPotLid, 1, 1)));

        for (Item flatItem : new Item[]{TFCItems.flatLinen, TFCItems.flatWool, TFCItems.flatSilk, TFCItems.flatCotton, TFCItems.flatBurlap}) {
            setup.registry(TfcRegistry.Knapping.recipes)
                .add(KnappingRecipe.add(new ItemStack(BidsItems.steamingMeshCloth),
                    "#####", "# # #", "#####", "# # #", "#####", '#', flatItem));
        }
    }

}
