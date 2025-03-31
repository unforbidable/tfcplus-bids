package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.Items.Tools.ItemJavelin;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHardenedWoodenSpear extends ItemJavelin {

    public ItemHardenedWoodenSpear(ToolMaterial par2EnumToolMaterial) {
        super(par2EnumToolMaterial, 0);

        setCreativeTab(BidsCreativeTabs.bidsTools);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block b = world.getBlock(x, y, z);
        return b == TFCBlocks.toolRack;
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "tools/" + this.getUnlocalizedName().replace("item.", ""));
    }

}
