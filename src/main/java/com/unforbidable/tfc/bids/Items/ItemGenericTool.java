package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

public class ItemGenericTool extends ItemTerra {

    public ItemGenericTool(ToolMaterial material) {
        setMaxStackSize(1);
        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxDamage(material.getMaxUses());
        setNoRepair();
        setFolder("tools");
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String name = getUnlocalizedName().replace("item.", "");
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + textureFolder + "/" + name);
    }

    @Override
    public boolean canStack() {
        return false;
    }

    @Override
    public EnumItemReach getReach(ItemStack arg0) {
        return EnumItemReach.SHORT;
    }

    @Override
    public EnumSize getSize(ItemStack arg0) {
        return EnumSize.VERYSMALL;
    }

    @Override
    public EnumWeight getWeight(ItemStack arg0) {
        return EnumWeight.LIGHT;
    }

}
