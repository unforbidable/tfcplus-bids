package com.unforbidable.tfc.bids.features.resource.flora.block.blockitem;

import com.dunk.tfc.Core.ColorizerFoliageTFC;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.features.resource.flora.block.BlockMoreGrass;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemMoreGrass extends ItemBlock {

    public ItemMoreGrass(Block block) {
        super(block);
    }

    @Override
    public int getItemStackLimit(ItemStack itemStack) {
        return 1;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        Block block = Block.getBlockFromItem(this);
        if (block instanceof BlockMoreGrass) {
            String[] names = ((BlockMoreGrass) block).getMetaNames();
            if (names != null && itemStack.getItemDamage() < names.length) {
                return getUnlocalizedName().concat("." + names[itemStack.getItemDamage()]);
            }
        }

        return super.getUnlocalizedName(itemStack);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack is, int damage)
    {
        return ColorizerFoliageTFC.getFoliageColorBasic();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int damage)
    {
        return BidsBlocks.moreGrass.getIcon(0, damage);
    }

}
