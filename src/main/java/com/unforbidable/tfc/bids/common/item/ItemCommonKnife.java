package com.unforbidable.tfc.bids.common.item;

import com.dunk.tfc.Items.Tools.ItemKnife;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCommonKnife extends ItemKnife {

    public ItemCommonKnife(ToolMaterial material, float damage) {
        super(material, damage);

        setMaxDamage(material.getMaxUses());
        setCreativeTab(BidsCreativeTabs.bidsTools);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        String name = getUnlocalizedName().replace("item.", "");
        this.itemIcon = registerer.registerIcon(Tags.MOD_ID + ":" + "tools/" + name);
    }

    @Override
    public boolean canStack() {
        return false;
    }

    public boolean onBlockDestroyed(ItemStack is, World world, Block block, int x, int y, int z, EntityLivingBase entityLiving) {
        // Override to prevent taking double damage from ItemSword
        if ((double)block.getBlockHardness(world, x, y, z) != 0.0) {
            is.damageItem(1, entityLiving);
        }

        return true;
    }

}
