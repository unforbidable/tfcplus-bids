package com.unforbidable.tfc.bids.Items;

import java.util.List;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemGlassBottle;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Tags;
import com.unforbidable.tfc.bids.Core.Drinks.DrinkHelper;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemDrinkingGlass extends ItemGlassBottle {

    public ItemDrinkingGlass() {
        super();
        setMetal(Global.GLASS);
        setCreativeTab(BidsCreativeTabs.BidsDefault);
    }

    @Override
    public void registerIcons(IIconRegister registerer) {
        itemIcon = registerer.registerIcon(Tags.MOD_ID + ":glassware/"
                + getUnlocalizedName().replace("item.", ""));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer entity) {
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, entity, true);
        if (mop == null) {

        } else {
            if (mop.typeOfHit == MovingObjectType.BLOCK) {
                int x = mop.blockX;
                int y = mop.blockY;
                int z = mop.blockZ;

                // Handle flowing water
                int flowX = x;
                int flowY = y;
                int flowZ = z;
                switch (mop.sideHit) {
                    case 0:
                        flowY = y - 1;
                        break;
                    case 1:
                        flowY = y + 1;
                        break;
                    case 2:
                        flowZ = z - 1;
                        break;
                    case 3:
                        flowZ = z + 1;
                        break;
                    case 4:
                        flowX = x - 1;
                        break;
                    case 5:
                        flowX = x + 1;
                        break;
                }

                if (!entity.isSneaking() && !world.isRemote &&
                        TFC_Core.isFreshWater(world.getBlock(x, y, z))
                        || TFC_Core.isFreshWater(world.getBlock(flowX, flowY, flowZ))) {
                    Item item = DrinkHelper.findFluidContainerItem(this,
                            TFCFluids.FRESHWATER);
                    if (item != null) {
                        ItemStack filledDrinkingGlass = new ItemStack(item);

                        --is.stackSize;
                        if (!entity.inventory.addItemStackToInventory(filledDrinkingGlass)) {
                            if (is.stackSize == 0) {
                                return filledDrinkingGlass;
                            } else {
                                entity.dropPlayerItemWithRandomChoice(filledDrinkingGlass, false);
                            }
                        }
                    } else {
                        Bids.LOG.warn("Drinking glass containing fresh water not found!");
                    }
                }

                if (!world.canMineBlock(entity, x, y, z)) {
                    return is;
                }

                if (!entity.canPlayerEdit(x, y, z, mop.sideHit, is)) {
                    return is;
                }

            }
        }
        return is;
    }

}
