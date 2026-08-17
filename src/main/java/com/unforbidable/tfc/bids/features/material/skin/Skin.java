package com.unforbidable.tfc.bids.features.material.skin;

import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.api.BidsItems;
import com.unforbidable.tfc.bids.api.names.ItemNames;
import com.unforbidable.tfc.bids.core.features.Feature;
import com.unforbidable.tfc.bids.core.features.annotations.FeatureName;
import com.unforbidable.tfc.bids.core.features.client.FeatureClientSpecBuilder;
import com.unforbidable.tfc.bids.core.features.init.FeatureInitSpecBuilder;
import com.unforbidable.tfc.bids.core.features.registry.FeatureRegistryLookup;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemDehairedSkin;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemFinishedSkin;
import com.unforbidable.tfc.bids.features.material.skin.item.ItemFreshSkin;
import com.unforbidable.tfc.bids.features.material.skin.render.SkinItemRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.core.crafting.actions.DamageTool.damageTool;
import static com.unforbidable.tfc.bids.features.material.skin.crafting.action.CutSkin.cutSkin;
import static com.unforbidable.tfc.bids.features.material.skin.crafting.action.ShearSkin.shearSkin;

/**
 * Fresh skins decay rapidly until scraped.
 */
@FeatureName("skin")
public class Skin extends Feature {

    @Override
    public void init(FeatureInitSpecBuilder init, FeatureRegistryLookup lookup) {
        init.item(ItemNames.GENERIC_SKIN, ItemFreshSkin::new);
        init.item(ItemNames.GENERIC_FUR, ItemFreshSkin::new)
            .apply(i -> i.setSpecialCraftingItem(TFCItems.flatFur));
        init.item(ItemNames.WOLF_FUR, ItemFreshSkin::new)
            .apply(i -> i.setSpecialCraftingItem(TFCItems.flatWolfFur));
        init.item(ItemNames.BEAR_FUR, ItemFreshSkin::new)
            .apply(i -> i.setSpecialCraftingItem(TFCItems.flatBearFur));
        init.item(ItemNames.SHEEP_SKIN, ItemFreshSkin::new);
        init.item(ItemNames.DEHAIRED_SKIN, ItemDehairedSkin::new);
        init.item(ItemNames.RAWHIDE, ItemFinishedSkin::new)
            .apply(i -> i.setSpecialCraftingItem(TFCItems.flatHide));
        init.item(ItemNames.LEATHER, ItemFinishedSkin::new)
            .apply(i -> i.setSpecialCraftingItem(TFCItems.flatLeather));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void client(FeatureClientSpecBuilder client) {
        client.render(new SkinItemRenderer())
            .item(BidsItems.genericSkin)
            .item(BidsItems.genericFur)
            .item(BidsItems.wolfFur)
            .item(BidsItems.bearFur)
            .item(BidsItems.sheepSkin)
            .item(BidsItems.dehairedSkin)
            .item(BidsItems.rawhide)
            .item(BidsItems.leather);
    }
}
